package com.example.project_skripsi.module.teacher.form.assessment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.Answer
import com.example.project_skripsi.core.model.firestore.AssignedTaskForm
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.model.local.AssignedQuestion
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.Constant
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.helper.DateHelper

class TcAssessmentTaskFormViewModel : ViewModel() {

    companion object {
        val TASK_FORM_TYPE = mapOf(
            "pilihan berganda" to 0,
            "essai" to 1,
        )
    }

    private lateinit var taskForm: TaskForm
    private lateinit var studentId: String

    private lateinit var currentStudent: Student

    lateinit var assignedAnswers: List<Answer>

    private val _student = MutableLiveData<Student>()
    val student: LiveData<Student> = _student

    private val _assignedTaskForm = MutableLiveData<AssignedTaskForm>()
    val assignedTaskForm: LiveData<AssignedTaskForm> = _assignedTaskForm

    private val _questionList = MutableLiveData<Pair<List<AssignedQuestion>, Boolean>>()
    val questionList: LiveData<Pair<List<AssignedQuestion>, Boolean>> = _questionList

    private val _assessmentComplete = MutableLiveData<Boolean>()
    val assessmentCompleted: LiveData<Boolean> = _assessmentComplete

    fun setAssignedTaskForm(studentId: String, taskFormId: String) {
        this.studentId = studentId
        loadTaskForm(taskFormId)
    }

    private fun loadTaskForm(uid: String) {
        FireRepository.inst.getItem<TaskForm>(uid).first.observeOnce {
            taskForm = it
            loadStudent(studentId)
        }
    }

    private fun loadStudent(uid: String) {
        FireRepository.inst.getItem<Student>(uid).first.observeOnce { student ->
            currentStudent = student
            _student.postValue(student)
            when (taskForm.type) {
                Constant.TASK_TYPE_ASSIGNMENT -> student.assignedAssignments
                else -> student.assignedExams
            }?.firstOrNull { item ->
                item.id == taskForm.id
            }?.let {
                _assignedTaskForm.postValue(it)
                val questions = mutableListOf<AssignedQuestion>()
                taskForm.questions?.mapIndexed { index, question ->
                    questions.add(AssignedQuestion(question, it.answers?.get(index)))
                }
                _questionList.postValue(Pair(questions, it.endTime!! < DateHelper.getCurrentTime()))
            }
        }
    }

    fun submitResult() {
        assignedTaskForm.value?.let {
            val newAssignedTaskForm = AssignedTaskForm(
                id = it.id,
                title = it.title,
                type = it.type,
                startTime = it.startTime,
                endTime = it.endTime,
                isSubmitted = it.isSubmitted,
                isChecked = true,
                subjectName = it.subjectName,
                score = assignedAnswers.sumOf { answer -> answer.score ?: 0 },
                answers = assignedAnswers
            )

            when (taskForm.type) {
                Constant.TASK_TYPE_ASSIGNMENT -> currentStudent.assignedAssignments
                else -> currentStudent.assignedExams
            }?.let { list ->
                val pos = list.indexOfFirst { item -> item.id == taskForm.id }
                list.set(pos, newAssignedTaskForm)
            }
            FireRepository.inst.alterItems(listOf(currentStudent)).first.observeOnce { status ->
                _assessmentComplete.postValue(status)
            }
        }
    }

}