package com.example.project_skripsi.module.student.task.form

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.AssignedTaskForm
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.model.local.AssignedQuestion
import com.example.project_skripsi.core.model.local.TaskFormStatus
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class StTaskFormViewModel : ViewModel() {

    companion object {
        val TASK_FORM_TYPE = mapOf(
            "pilihan berganda" to 0,
            "essai" to 1,
        )
    }

    private val _taskForm = MutableLiveData<TaskForm>()
    val taskForm: LiveData<TaskForm> = _taskForm

    private val _formStatus = MutableLiveData<Pair<String, Int>>()
    val formStatus: LiveData<Pair<String, Int>> = _formStatus

    private val _studyClass = MutableLiveData<StudyClass>()
    val studyClass: LiveData<StudyClass> = _studyClass

    private val _questionList = MutableLiveData<List<AssignedQuestion>>()
    val questionList: LiveData<List<AssignedQuestion>> = _questionList

    private var taskFormId = ""

    fun setTaskForm(taskFormId : String) {
        this.taskFormId = taskFormId
        loadTaskForm(taskFormId)
    }

    private fun loadTaskForm(uid: String) {
        FireRepository.instance.getTaskForm(uid).let { response ->
            response.first.observeOnce {
                _taskForm.postValue(it)
                loadStudent(AuthRepository.instance.getCurrentUser().uid)
            }
        }
    }

    private fun loadStudent(uid: String) {
        FireRepository.instance.getStudent(uid).let { response ->
            response.first.observeOnce { student ->
                student.studyClass?.let { loadStudyClass(it) }

                var assignedTaskForm : AssignedTaskForm? = null
                student.assignedExams?.firstOrNull { it.id == taskFormId }.let {
                    it?.let { item -> assignedTaskForm = item }
                }
                student.assignedAssignments?.firstOrNull { it.id == taskFormId }.let {
                    it?.let { item -> assignedTaskForm = item }
                }

                _formStatus.postValue(
                    Pair(
                        TaskFormStatus.getStatus(taskForm.value!!,
                            assignedTaskForm!!),
                        TaskFormStatus.getStatusColor(taskForm.value!!,
                            assignedTaskForm!!)
                    )
                )

                val questionList = ArrayList<AssignedQuestion>()
                taskForm.value?.questions?.mapIndexed { index, question ->
                    questionList.add(AssignedQuestion(question, assignedTaskForm?.answers?.getOrNull(index)))
                }
                _questionList.postValue(questionList.toList())
            }
        }
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.instance.getStudyClass(uid).let { response ->
            response.first.observeOnce { _studyClass.postValue(it) }
        }
    }

}