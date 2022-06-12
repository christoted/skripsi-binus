package com.example.project_skripsi.module.teacher.study_class.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class TcStudyClassTaskViewModel : ViewModel() {

    companion object {
        const val TAB_EXAM = 0
        const val TAB_ASSIGNMENT = 1
        const val tabCount = 2
        val tabHeader = arrayOf("Ujian", "Tugas")
    }

    private val _studyClass = MutableLiveData<StudyClass>()
    val studyClass: LiveData<StudyClass> = _studyClass

    private val _examList = MutableLiveData<List<TaskForm>>()
    val examList: LiveData<List<TaskForm>> = _examList

    private val _assignmentList = MutableLiveData<List<TaskForm>>()
    val assignmentList: LiveData<List<TaskForm>> = _assignmentList

    var studyClassId = ""
    var subjectName = ""

    fun setClassAndSubject(studyClassId: String, subjectName: String) {
        loadStudyClass(studyClassId)
        this.studyClassId = studyClassId
        this.subjectName = subjectName
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.inst.getItem<StudyClass>(uid).let { response ->
            response.first.observeOnce { studyClass ->
                _studyClass.postValue(studyClass)
                studyClass.subjects?.firstOrNull { it.subjectName == subjectName }?.classExams?.let {
                    loadTaskForms(it, _examList)
                }
                studyClass.subjects?.firstOrNull { it.subjectName == subjectName }?.classAssignments?.let {
                    loadTaskForms(it, _assignmentList)
                }
            }
        }
    }


    private fun loadTaskForms(uids: List<String>, _taskFormList: MutableLiveData<List<TaskForm>>) {
        FireRepository.inst.getItems<TaskForm>(uids).first.observeOnce { list ->
            _taskFormList.postValue(list.sortedByDescending { it.endTime })
        }
    }


}