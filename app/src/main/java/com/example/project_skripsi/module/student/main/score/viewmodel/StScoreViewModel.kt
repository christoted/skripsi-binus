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
    private var _mapAssignmentExamBySubject = MutableLiveData<Map<String, List<AssignedTaskForm>>>()
    private var _listExamAssignment = MutableLiveData<List<AssignedTaskForm>>()
    private var listExamAssignment = arrayListOf<AssignedTaskForm>()

    companion object {
        const val tabCount = 3
        val tabHeader = arrayOf("Nilai", "Absensi", "Pencapaian")
    }

    init {
        val listData = arrayListOf<ScoreMainSection>()

        loadCurrentStudent(AuthRepository.instance.getCurrentUser().uid)
        _subjects.observeOnce {
            it.forEach { subject ->
                subject.subjectName?.let {
                    // Section Item -> Score Section Data By Subject
                    _assignedAssignmentStudent.observeOnce {
                      _mapAssignmentExamBySubject.postValue(
                          it.groupBy {
                              it.subjectName!!
                          }
                      )
                    }

                    _assignedExamStudent.observeOnce {
                        _mapAssignmentExamBySubject.postValue(
                            it.groupBy {
                                it.subjectName!!
                            }
                        )
                    }

                    _mapAssignmentExamBySubject.observeOnce {
                       val datas = it[subject.subjectName]
                        datas?.let {
                            _listExamAssignment.postValue(it)
                        }
                    }
                    _listExamAssignment.observeOnce {
                        listExamAssignment.addAll(it)
                    }
                }
                    listData.add(ScoreMainSection(subjectName = subject.subjectName!!, mid_exam = 0.0, exam = 0.0, total_assignment = 0.0, total_score = 0.0, sectionItem = listExamAssignment ))
            }
            _sectionDatas.postValue(listData)
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

    // Absent

}