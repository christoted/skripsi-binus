package com.example.project_skripsi.module.student.main.score.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.AssignedTaskForm
import com.example.project_skripsi.core.model.firestore.Subject
import com.example.project_skripsi.core.model.local.ScoreMainSection
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

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

        const val MID_EXAM_WEIGHT = 40
        const val FINAL_EXAM_WEIGHT = 40
        const val ASSIGNMENT_WEIGHT = 20

        const val TYPE_MID_EXAM = "ujian_tengah_semester"
        const val TYPE_FINAL_EXAM = "ujian_akhir_semester"
        const val TYPE_ASSIGNMENT = "tugas"
    }

    init {
        val listData = arrayListOf<ScoreMainSection>()
        _subjects.observeOnce { subjects ->
            subjects.forEach { subject ->
                subject.subjectName?.let { subjectName ->
                    val midExam: Int? = mutableListOfTask.filter {
                        getTaskFilter(it, TYPE_MID_EXAM, subjectName)
                    }.let {
                        if (it.isEmpty()) null else it[0].score ?: 0
                    }

                    val finalExam: Int? = mutableListOfTask.filter {
                        getTaskFilter(it, TYPE_FINAL_EXAM, subjectName)
                    }.let {
                        if (it.isEmpty()) null else it[0].score ?: 0
                    }

                    val totalAssignment : Int? = mutableListOfTask.filter {
                        getTaskFilter(it, TYPE_ASSIGNMENT, subjectName)
                    }.let {
                        if (it.isEmpty()) null else it.averageOf { task -> task.score ?: 0 }
                    }

                    val totalScore = MID_EXAM_WEIGHT * (midExam ?: 0) +
                            FINAL_EXAM_WEIGHT * (finalExam ?: 0) +
                            ASSIGNMENT_WEIGHT * (totalAssignment ?: 0)

                    val scoreWeight = MID_EXAM_WEIGHT * (midExam?.let { 1 } ?: 0) +
                            FINAL_EXAM_WEIGHT * (finalExam?.let { 1 } ?: 0) +
                            ASSIGNMENT_WEIGHT * (totalAssignment?.let { 1 } ?: 0)
                    
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

    private fun getTaskFilter(atf: AssignedTaskForm, taskType : String, subjectName : String) =
        atf.type == taskType && atf.taskChecked == true && atf.subjectName == subjectName

}

private fun <T> Iterable<T>.averageOf(selector: (T) -> Int): Int {
    var sum = 0
    var count = 0
    for (element in this) {
        sum += selector(element)
        count++
    }
    return sum / count
}
