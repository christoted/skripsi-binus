package com.example.project_skripsi.module.student.main.score.viewmodel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.AssignedTaskForm
import com.example.project_skripsi.core.model.firestore.Subject
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.model.local.DayEvent
import com.example.project_skripsi.core.model.local.HomeMainSection
import com.example.project_skripsi.core.model.local.ScoreMainSection
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StScoreViewModel : ViewModel() {

    // TODO:
    //  - Display the subject
    //  - Display the score average
    //  - Display the list of score

    private val _text = MutableLiveData<String>().apply {
        value = "This is score Fragment"
    }
    val text: LiveData<String> = _text

    private val _sectionDatas = MutableLiveData<List<ScoreMainSection>>()
    val sectionDatas: LiveData<List<ScoreMainSection>> = _sectionDatas
    private val _subjects = MutableLiveData<List<Subject>>()
    private var _mapAssignmentExamBySubject = MutableLiveData<Map<String, List<AssignedTaskForm>>>()
    private val mutableListOfTask: MutableList<AssignedTaskForm> = mutableListOf()

    companion object {
        const val tabCount = 3
        val tabHeader = arrayOf("Nilai", "Absensi", "Pencapaian")
    }

    init {
        val listData = arrayListOf<ScoreMainSection>()
        _subjects.observeOnce {
            Log.d("Subject", ": " + it.map {
                it.subjectName
            })

            it.forEach { subject ->
                subject.subjectName?.let { subjectName ->
                    val midExam: Int? = mutableListOfTask.filter { it.type == "ujian_tengah_semester" && it.taskChecked == true && it.subjectName == subjectName}.let {
                        if (it.isEmpty()) null else it[0].score ?: 0
                    }
                    val finalExam: Int? = mutableListOfTask.filter { it.type == "ujian_akhir_semester" && it.taskChecked == true && it.subjectName == subjectName}.let {
                        if (it.isEmpty()) null else it[0].score ?: 0
                    }
                    val totalAssignment : Int? = mutableListOfTask.filter { it.type == "tugas" && it.taskChecked == true && it.subjectName == subjectName}.let {
                        if (it.isEmpty()) null else it.sumOf { it.score ?: 0  } / it.size
                    }

                    var totalScore = 0
                    var scoreWeight = 0

                    if (midExam != null) {
                        scoreWeight += 40
                        totalScore += 40 * midExam
                    }

                    if (finalExam != null) {
                        scoreWeight += 40
                        totalScore += 40 * finalExam
                    }

                    if (totalAssignment != null) {
                        scoreWeight += 20
                        totalScore += 20 * totalAssignment
                    }

                    listData.add(ScoreMainSection(
                        subjectName = subjectName,
                        mid_exam = midExam,
                        final_exam = finalExam,
                        total_assignment = totalAssignment,
                        total_score = if (scoreWeight == 0) null else totalScore / scoreWeight,
                        sectionItem = mutableListOfTask.filter { it.subjectName == subjectName }))
                }
            }
            _sectionDatas.postValue(listData)
        }
        loadCurrentStudent(AuthRepository.instance.getCurrentUser().uid)
    }

    private fun loadCurrentStudent(uid: String) {
        FireRepository.instance.getStudent(uid).let {
                response ->
            response.first.observeOnce {
                    student ->
                student.studyClass?.let {
                    loadStudyClass(it)
                }
                val assignedTask: MutableMap<String, MutableList<AssignedTaskForm>> = mutableMapOf()

                student.assignedAssignments?.filter { it.taskChecked == true }?.let {
                    mutableListOfTask.addAll(it)
                }

                student.assignedExams?.filter { it.taskChecked == true }?.let {
                    mutableListOfTask.addAll(it)
                }
                _mapAssignmentExamBySubject.postValue(assignedTask)
            }
        }
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.instance.getStudyClass(uid).let {
            response ->
            response.first.observeOnce {
                it.subjects.let { subjects ->
                    _subjects.postValue(subjects)
                }
            }
        }
    }

}