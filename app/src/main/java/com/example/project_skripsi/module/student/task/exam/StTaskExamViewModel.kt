package com.example.project_skripsi.module.student.task.exam

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.AssignedTaskForm
import com.example.project_skripsi.core.model.local.TaskFormStatus
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.helper.DateHelper

class StTaskExamViewModel : ViewModel() {

    companion object {
        const val EXAM_ONGOING = 0
        const val EXAM_PAST = 1
        const val tabCount = 2
        val tabHeader = arrayOf("Berlangsung", "Selesai")
    }

    private val _ongoingList = MutableLiveData<List<TaskFormStatus>>()
    val ongoingList : LiveData<List<TaskFormStatus>> = _ongoingList

    private val _pastList = MutableLiveData<List<TaskFormStatus>>()
    val pastList : LiveData<List<TaskFormStatus>> = _pastList

    private var className = ""
    private val mAssignedTaskForms = HashMap<String, AssignedTaskForm>()

    init {
        loadStudent(AuthRepository.inst.getCurrentUser().uid)
    }

    private fun loadStudent(uid: String) {
        FireRepository.inst.getStudent(uid).let { response ->
            response.first.observeOnce { student ->
                with(student) {
                    studyClass?.let { uid -> loadStudyClass(uid) }
                    assignedExams?.map { exam -> exam.id?.let { mAssignedTaskForms.put(it, exam) }}
                }
            }
        }
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.inst.getStudyClass(uid).let { response ->
            response.first.observeOnce { studyClass ->
                val allExams = ArrayList<String>()
                with(studyClass) {
                    name?.let { className = it }
                    subjects?.map { subject -> subject.classExams?.let { allExams.addAll(it) } }
                }
                loadTaskForms(allExams)
            }
        }
    }

    private fun loadTaskForms(uids: List<String>) {
        val ongoingList = ArrayList<TaskFormStatus>()
        val pastList = ArrayList<TaskFormStatus>()
        uids.map { uid ->
            FireRepository.inst.getTaskForm(uid).let { response ->
                response.first.observeOnce { taskForm ->
                    mAssignedTaskForms[uid]?.let {
                        if (taskForm.endTime!! > DateHelper.getCurrentDate()) {
                            ongoingList
                        } else {
                            pastList
                        }.add(TaskFormStatus(className, taskForm, it))
                    }
                    if (ongoingList.size + pastList.size == uids.size) {
                        _ongoingList.postValue(ongoingList)
                        _pastList.postValue(pastList)
                    }
                }
            }
        }
    }

}