package com.example.project_skripsi.module.parent.student_detail.exam

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.AssignedTaskForm
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.model.local.TaskFormStatus
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.helper.DateHelper

class PrExamViewModel : ViewModel() {

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

    fun setStudent(studentId: String) {
        loadStudent(studentId)
    }

    private fun loadStudent(uid: String) {
        FireRepository.instance.getItem<Student>(uid).first.observeOnce { student ->
            with(student) {
                studyClass?.let { uid -> loadStudyClass(uid) }
                assignedExams?.map { exam -> exam.id?.let { mAssignedTaskForms.put(it, exam) }}
            }
        }
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.instance.getItem<StudyClass>(uid).first.observeOnce { studyClass ->
            val allExams = mutableListOf<String>()
            with(studyClass) {
                name?.let { className = it }
                subjects?.map { subject -> subject.classExams?.let { allExams.addAll(it) } }
            }
            loadTaskForms(allExams)
        }
    }

    private fun loadTaskForms(uids: List<String>) {
        FireRepository.instance.getItems<TaskForm>(uids).first.observeOnce {
            val ongoingList = mutableListOf<TaskFormStatus>()
            val pastList = mutableListOf<TaskFormStatus>()
            it.map { taskForm ->
                mAssignedTaskForms[taskForm.id]?.let { assignTaskForm ->
                    if (taskForm.endTime!! > DateHelper.getCurrentDate()) {
                        ongoingList
                    } else {
                        pastList
                    }.add(TaskFormStatus(className, taskForm, assignTaskForm))
                }
            }
            _ongoingList.postValue(ongoingList)
            _pastList.postValue(pastList)
        }
    }

}