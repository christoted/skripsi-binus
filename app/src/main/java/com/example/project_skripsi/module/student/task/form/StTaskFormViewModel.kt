package com.example.project_skripsi.module.student.task.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_skripsi.core.model.firestore.AssignedTaskForm
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.model.local.AssignedQuestion
import com.example.project_skripsi.core.model.local.TaskFormStatus
import com.example.project_skripsi.core.model.local.TaskFormTimer
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.Constant.Companion.TASK_FORM_ESSAY
import com.example.project_skripsi.utils.Constant.Companion.TASK_FORM_MC
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.helper.DateHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StTaskFormViewModel : ViewModel() {

    companion object {
        val TASK_FORM_TYPE = mapOf(
            TASK_FORM_MC to 0,
            TASK_FORM_ESSAY to 1,
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

    private val _timeLeft = MutableLiveData<TaskFormTimer>()
    val timerLeft: LiveData<TaskFormTimer> = _timeLeft

    private val _isSubmitted = MutableLiveData<Boolean>()
    val isSubmitted: LiveData<Boolean> = _isSubmitted

    private var taskFormId = ""
    private lateinit var curStudent: Student

    fun setTaskForm(taskFormId : String) {
        this.taskFormId = taskFormId
        loadTaskForm(taskFormId)
    }

    private fun loadTaskForm(uid: String) =
        FireRepository.inst.getItem<TaskForm>(uid).first.observeOnce {
            _taskForm.postValue(it)
            loadStudent(AuthRepository.inst.getCurrentUser().uid)

            viewModelScope.launch {
                it.endTime?.let { endTime ->
                    while (true) {
                        val difTime = endTime.time - DateHelper.getCurrentTime().time
                        val s = difTime / 1000
                        val m = s / 60
                        val h = m / 60
                        val forceSubmit = false
//                        val forceSubmit = difTime <= 0
                        _timeLeft.postValue(TaskFormTimer(forceSubmit, h, m%60, s%60))
                        delay(1000)
                    }
                }

            }
        }


    private fun loadStudent(uid: String) {
        FireRepository.inst.getItem<Student>(uid).first.observeOnce { student ->
            curStudent = student
            student.studyClass?.let { loadStudyClass(it) }

            var assignedTaskForm: AssignedTaskForm? = null
            student.assignedExams?.firstOrNull { it.id == taskFormId }?.let { assignedTaskForm = it }
            student.assignedAssignments?.firstOrNull { it.id == taskFormId }?.let { assignedTaskForm = it }

            _formStatus.postValue(
                Pair(
                    TaskFormStatus.getStatus(
                        taskForm.value!!,
                        assignedTaskForm!!
                    ),
                    TaskFormStatus.getStatusColor(
                        taskForm.value!!,
                        assignedTaskForm!!
                    )
                )
            )

            val questionList = ArrayList<AssignedQuestion>()
            taskForm.value?.questions?.mapIndexed { index, question ->
                questionList.add(
                    AssignedQuestion(
                        question,
                        assignedTaskForm?.answers?.getOrNull(index)
                    )
                )
            }
            _questionList.postValue(questionList.toList())

        }
    }

    private fun loadStudyClass(uid: String) =
        FireRepository.inst.getItem<StudyClass>(uid).first.observeOnce { _studyClass.postValue(it) }

    fun submitAnswer(newAnswer: List<String>) {
        curStudent.assignedAssignments?.firstOrNull { it.id == taskFormId }?.let {
            it.answers?.mapIndexed { index, answer -> answer.answerText = newAnswer[index] }
        }
        curStudent.assignedExams?.firstOrNull { it.id == taskFormId }?.let {
            it.answers?.mapIndexed { index, answer -> answer.answerText = newAnswer[index] }
        }
        FireRepository.inst.alterItems(listOf(curStudent)).first.observeOnce {
            _isSubmitted.postValue(it)
        }
    }

}