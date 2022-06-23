package com.example.project_skripsi.module.teacher.form.alter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.Question
import com.example.project_skripsi.core.model.firestore.Resource
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.model.firestore.Teacher
import com.example.project_skripsi.core.model.local.SubjectGroup
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.generic.HandledEvent
import com.example.project_skripsi.utils.helper.DateHelper
import com.example.project_skripsi.utils.helper.DateHelper.Companion.getDateWithZeroSecond
import com.example.project_skripsi.utils.helper.UUIDHelper
import java.util.*

class TcAlterTaskViewModel : ViewModel() {

    companion object {
        const val QUERY_RESOURCE = 0
        const val QUERY_ASSIGNMENT = 1

        const val TYPE_EXAM = 0
        const val TYPE_ASSIGNMENT = 1

        val TASK_FORM_TYPE = mapOf(
            "pilihan berganda" to 0,
            "essai" to 1,
        )
    }

    private val _startDate = MutableLiveData<Date>()
    val startDate: LiveData<Date> = _startDate

    private val _endDate = MutableLiveData<Date>()
    val endDate: LiveData<Date> = _endDate

    private val _oldTaskForm = MutableLiveData<TaskForm>()
    val oldTaskForm: LiveData<TaskForm> = _oldTaskForm

    private val _resourceList = MutableLiveData<List<Resource>>()
    val resourceList: LiveData<List<Resource>> = _resourceList

    private val _assignmentList = MutableLiveData<List<TaskForm>>()
    val assignmentList: LiveData<List<TaskForm>> = _assignmentList

    private val _questionList = MutableLiveData<List<Question>>()
    val questionList: LiveData<List<Question>> = _questionList

    // isSuccess, continueToFinalize
    private val _draftSaved = MutableLiveData<HandledEvent<Pair<Boolean, Boolean>>>()
    val draftSaved: LiveData<HandledEvent<Pair<Boolean, Boolean>>> = _draftSaved

    var taskType = ""
    var selectedResource = listOf<String>()
    var selectedAssignment = listOf<String>()

    lateinit var subjectGroup: SubjectGroup
    private lateinit var currentTeacher: Teacher

    private var isNewForm = true
    var formType: Int = -1
    private val resourceIds = mutableListOf<String>()
    private val assignmentIds = mutableListOf<String>()
    lateinit var savedTaskFormId: String


    fun initData(subjectName: String, gradeLevel: Int, formType: Int, taskFormId: String?) {
        _startDate.postValue(DateHelper.getCurrentTime().getDateWithZeroSecond())
        _endDate.postValue(DateHelper.getCurrentTime().getDateWithZeroSecond())
        _questionList.postValue(emptyList())
        subjectGroup = SubjectGroup(subjectName, gradeLevel)
        this.formType = formType
        loadTeacher(AuthRepository.inst.getCurrentUser().uid)
        if (taskFormId != null) {
            loadTaskForm(taskFormId)
        }
    }

    private fun loadTaskForm(uid: String) {
        isNewForm = false
        FireRepository.inst.getItem<TaskForm>(uid).first.observeOnce {
            _oldTaskForm.postValue(it)
            with(it) {
                startTime?.let { time -> _startDate.postValue(time) }
                endTime?.let { time -> _endDate.postValue(time) }
                type?.let { item -> taskType = item }
                prerequisiteResources?.let { list -> selectedResource = list }
                prerequisiteTaskForms?.let { list -> selectedAssignment = list }
                questions?.let { list -> _questionList.postValue(list) }
            }
        }
    }

    private fun loadTeacher(uid: String) {
        FireRepository.inst.getItem<Teacher>(uid).first.observeOnce { teacher ->
            currentTeacher = teacher
            teacher.teachingGroups?.firstOrNull { it.subjectName == subjectGroup.subjectName && it.gradeLevel == subjectGroup.gradeLevel }
                ?.let { group ->
                    with(group) {
                        createdResources?.let { resourceIds.addAll(it) }
                        createdAssignments?.let { assignmentIds.addAll(it) }
                    }
                }
        }
    }

    fun loadResource() {
        FireRepository.inst.getItems<Resource>(resourceIds).first.observeOnce { list ->
            _resourceList.postValue(list.sortedBy { it.meetingNumber })
        }
    }

    fun loadAssignment() {
        FireRepository.inst.getItems<TaskForm>(assignmentIds).first.observeOnce { list ->
            _assignmentList.postValue(list.sortedBy { it.startTime })
        }
    }

    fun updateStartDate(date: Date) = _startDate.postValue(date)

    fun updateEndDate(date: Date) = _endDate.postValue(date)

    fun updateQuestions(questions: List<Question>) = _questionList.postValue(questions)


    fun submitForm(title: String, continueToFinalize: Boolean) {
        val items = mutableListOf<Any>()
        val taskFormId = UUIDHelper.getUUID()
        if (isNewForm) {
            currentTeacher.teachingGroups
                ?.firstOrNull { it.subjectName == subjectGroup.subjectName && it.gradeLevel == subjectGroup.gradeLevel }
                ?.let { if (formType == TYPE_EXAM) it.createdExams else it.createdAssignments }
                ?.add(taskFormId)
            items.add(currentTeacher)
        }
        val taskForm = TaskForm(
            id = if (isNewForm) taskFormId else oldTaskForm.value?.id,
            title = title,
            gradeLevel = subjectGroup.gradeLevel,
            type = taskType,
            startTime = startDate.value,
            endTime = endDate.value,
            location = "Online",
            subjectName = subjectGroup.subjectName,
            questions = questionList.value ?: emptyList(),
            assignedClasses = emptyList(),
            prerequisiteResources = selectedResource,
            prerequisiteTaskForms = selectedAssignment,
            isFinalized = false
        )
        items.add(taskForm)

        FireRepository.inst.alterItems(items).first.observeOnce {
            savedTaskFormId = taskForm.id ?: ""
            _draftSaved.postValue(HandledEvent(Pair(it, continueToFinalize)))
        }
    }

}