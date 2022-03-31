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
import com.example.project_skripsi.core.model.local.HomeMainSection
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
    private val _assignedAssignmentStudent = MutableLiveData<List<AssignedTaskForm>>()
    private val _assignedExamStudent = MutableLiveData<List<AssignedTaskForm>>()
    private val _taskForms = MutableLiveData<List<TaskForm>>()
    private val listForm = arrayListOf<TaskForm>()

    companion object {
        const val tabCount = 3
        val tabHeader = arrayOf("Nilai", "Absensi", "Pencapaian")
    }

    init {
        val listData = arrayListOf<ScoreMainSection>()
        loadCurrentStudent(AuthRepository.instance.getCurrentUser().uid)
        _subjects.observeOnce {
            it.map { subject ->
                subject.subjectName?.let {
                    // Section Item -> Score Section Data By Subject *Hint via _taskForms
                    _taskForms.observeOnce {
                        it.map {
                            if (it.subjectName == subject.subjectName ) {

                            }
                        }
                    }
                    // For the score section
                    _assignedAssignmentStudent.observeOnce {

                    }
               //     listData.add(ScoreMainSection(subjectName = it, mid_exam = 0.0, exam = 0.0, total_assignment = 0.0, total_score = 0.0, sectionItem = ))
                }
            }
        //    _sectionDatas.postValue()
        }
    }

    private fun loadCurrentStudent(uid: String) {
        FireRepository.instance.getStudent(uid).let {
                response ->
            response.first.observeOnce {
                    student ->
                Log.d("Data Student", "${student}")
                // TODO: Take the Class id
                student.studyClass?.let {
                    loadStudyClass(it)
                }
                student.assignedAssignments?.let {
                    _assignedAssignmentStudent.postValue(it)
                }
                student.assignedExams?.let {
                    _assignedExamStudent.postValue(it)
                }

                // MARK: Link to the task Forms
                // Assignment
                _assignedAssignmentStudent.value?.map {
                    it.id?.let { id -> loadTaskForm(id) }
                }
                // Exam
                _assignedExamStudent.value?.map {
                    it.id?.let { id ->
                        loadTaskForm(id)
                    }
                }

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

    private fun loadTaskForm(uid: String) {
        FireRepository.instance.getTaskForm(uid).let {
            response ->
            response.first.observeOnce {
                listForm.add(it)
            }
            _taskForms.postValue(listForm)
        }
    }

    // Absent

}