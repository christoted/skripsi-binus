package com.example.project_skripsi.module.parent.student_detail.assignment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.AssignedTaskForm
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.model.local.TaskFormStatus
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.helper.DateHelper

class PrAssignmentViewModel : ViewModel() {

    companion object {
        const val tabCount = 2
        val tabHeader = arrayOf("Berlangsung", "Selesai")
    }

    private val _subjects = MutableLiveData<List<String>>()
    val subjects: LiveData<List<String>> = _subjects

    private val _ongoingList = MutableLiveData<List<TaskFormStatus>>()
    val ongoingList: LiveData<List<TaskFormStatus>> = _ongoingList

    private val _pastList = MutableLiveData<List<TaskFormStatus>>()
    val pastList: LiveData<List<TaskFormStatus>> = _pastList

    private var className = ""
    private val mAssignedTaskForms = HashMap<String, AssignedTaskForm>()

    private val ongoingTaskForms = ArrayList<TaskFormStatus>()
    private val pastTaskForms = ArrayList<TaskFormStatus>()

    fun setStudent(studentId: String) {
        loadStudent(studentId)
    }

    private fun loadStudent(uid: String) {
        FireRepository.inst.getItem<Student>(uid).first.observeOnce { student ->
            with(student) {
                studyClass?.let { uid -> loadStudyClass(uid) }
                assignedAssignments?.map { exam ->
                    exam.id?.let {
                        mAssignedTaskForms.put(
                            it,
                            exam
                        )
                    }
                }
            }
        }
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.inst.getItem<StudyClass>(uid).first.observeOnce { studyClass ->
            val allAssignments = mutableListOf<String>()
            with(studyClass) {
                name?.let { className = it }
                subjects?.map { subject -> subject.classAssignments?.let { allAssignments.addAll(it) } }
                subjects?.map { it.subjectName!! }?.let {
                    val list = mutableListOf("Semua")
                    list.addAll(it)
                    _subjects.postValue(list)
                }
            }
            loadTaskForms(allAssignments)
        }
    }

    private fun loadTaskForms(uids: List<String>) {
        ongoingTaskForms.clear()
        pastTaskForms.clear()
        FireRepository.inst.getItems<TaskForm>(uids).first.observeOnce { list ->
            list.map { taskForm ->
                mAssignedTaskForms[taskForm.id]?.let {
                    if (taskForm.endTime!! > DateHelper.getCurrentTime()) {
                        ongoingTaskForms
                    } else {
                        pastTaskForms
                    }.add(TaskFormStatus(className, taskForm, it))
                }
            }
            _ongoingList.postValue(ongoingTaskForms.sortedBy { it.endTime })
            _pastList.postValue(pastTaskForms.sortedByDescending { it.endTime })
        }
    }

    fun filter(subjectName: String) {
        if (subjectName == "Semua") {
            _ongoingList.postValue(ongoingTaskForms.sortedBy { it.endTime })
            _pastList.postValue(pastTaskForms.sortedByDescending { it.endTime })
        } else {
            _ongoingList.postValue(ongoingTaskForms.filter { it.subjectName == subjectName }
                .sortedBy { it.endTime })
            _pastList.postValue(pastTaskForms.filter { it.subjectName == subjectName }
                .sortedByDescending { it.endTime })
        }
    }

}