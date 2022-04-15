package com.example.project_skripsi.module.teacher.form.alter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.model.local.SubjectGroup
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.helper.DateHelper
import com.example.project_skripsi.utils.helper.UUIDHelper
import java.util.*

class TcAlterTaskViewModel : ViewModel() {

    companion object {
        const val QUERY_CLASS = 0
        const val QUERY_RESOURCE = 1
        const val QUERY_ASSIGNMENT = 2

        const val TYPE_EXAM = 0
        const val TYPE_ASSIGNMENT = 1

        val TASK_FORM_TYPE = mapOf(
            "pilihan berganda" to 0,
            "essai" to 1,
        )
    }

    var taskType = ""
    var selectedClass = listOf<String>()
    var selectedResource = listOf<String>()
    var selectedAssignment = listOf<String>()

    private val _startDate = MutableLiveData<Date>()
    val startDate : LiveData<Date> = _startDate

    private val _endDate = MutableLiveData<Date>()
    val endDate : LiveData<Date> = _endDate

    private val _oldTaskForm = MutableLiveData<TaskForm>()
    val oldTaskForm : LiveData<TaskForm> = _oldTaskForm

    private val _classList = MutableLiveData<List<StudyClass>>()
    val classList : LiveData<List<StudyClass>> = _classList

    private val _resourceList = MutableLiveData<List<Resource>>()
    val resourceList : LiveData<List<Resource>> = _resourceList

    private val _assignmentList = MutableLiveData<List<TaskForm>>()
    val assignmentList : LiveData<List<TaskForm>> = _assignmentList

    private val _questionList = MutableLiveData<List<Question>>()
    val questionList : LiveData<List<Question>> = _questionList

    private val _taskFormCreated = MutableLiveData<Boolean>()
    val taskFormCreated : LiveData<Boolean> = _taskFormCreated

    private lateinit var subjectGroup : SubjectGroup
    private lateinit var currentTeacher: Teacher
    private val classIds = mutableListOf<String>()

    var isNewForm = true
    private var formType: Int = -1
    private val resourceIds = mutableListOf<String>()
    private val assignmentIds = mutableListOf<String>()


    fun initData(subjectName: String, gradeLevel: Int, formType : Int, taskFormId : String?) {
        _startDate.postValue(DateHelper.getCurrentDate())
        _endDate.postValue(DateHelper.getCurrentDate())
        _questionList.postValue(emptyList())
        subjectGroup = SubjectGroup(subjectName, gradeLevel)
        this.formType = formType
        loadTeacher(AuthRepository.instance.getCurrentUser().uid)
        if (taskFormId != null) { loadTaskForm(taskFormId) }
    }

    private fun loadTaskForm(uid: String) {
        isNewForm = false
        FireRepository.instance.getTaskForm(uid).first.observeOnce{
            _oldTaskForm.postValue(it)
            with(it) {
                startTime?.let { time -> _startDate.postValue(time) }
                endTime?.let { time -> _endDate.postValue(time) }
                type?.let { item -> taskType = item }
                assignedClasses?.let { list -> selectedClass = list }
                prerequisiteResources?.let { list -> selectedResource = list }
                prerequisiteTaskForms?.let { list -> selectedAssignment = list }
                questions?.let { list -> _questionList.postValue(list) }
            }
//
        }
    }

    private fun loadTeacher(uid : String) {
        FireRepository.instance.getTeacher(uid).first.observeOnce { teacher ->
            currentTeacher = teacher
            teacher.teachingGroups?.firstOrNull { it.subjectName == subjectGroup.subjectName && it.gradeLevel == subjectGroup.gradeLevel }
                ?.let { group ->
                    with(group) {
                        teachingClasses?.let { classIds.addAll(it) }
                        createdResources?.let { resourceIds.addAll(it) }
                        createdAssignments?.let { assignmentIds.addAll(it) }
                    }
                }
        }
    }

    fun loadClass() {
        val itemList = mutableListOf<StudyClass>()
        classIds.map { uid ->
            FireRepository.instance.getStudyClass(uid).first.observeOnce {
                itemList.add(it)
                if (itemList.size == classIds.size) _classList.postValue(itemList)
            }
        }
    }

    fun loadResource() {
        val itemList = mutableListOf<Resource>()
        resourceIds.map { uid ->
            FireRepository.instance.getResource(uid).first.observeOnce {
                itemList.add(it)
                if (itemList.size == resourceIds.size) _resourceList.postValue(itemList)
            }
        }
    }

    fun loadAssignment() {
        val itemList = mutableListOf<TaskForm>()
        assignmentIds.map { uid ->
            FireRepository.instance.getTaskForm(uid).first.observeOnce {
                itemList.add(it)
                if (itemList.size == assignmentIds.size) _assignmentList.postValue(itemList)
            }
        }
    }

    fun updateStartDate(date : Date) {
        _startDate.postValue(date)
    }

    fun updateEndDate(date : Date) {
        _endDate.postValue(date)
    }

    fun updateQuestions(questions : List<Question>) {
        _questionList.postValue(questions)
    }

    fun submitForm(title: String) {
        val items = mutableListOf<Any>()
        val taskFormId = UUIDHelper.getUUID()
        if (isNewForm) {
            currentTeacher.teachingGroups
                ?.firstOrNull{it.subjectName == subjectGroup.subjectName && it.gradeLevel == subjectGroup.gradeLevel}
                ?.let { if(formType == TYPE_EXAM) it.createdExams else it.createdAssignments }
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
            assignedClasses = selectedClass,
            prerequisiteResources = selectedResource,
            prerequisiteTaskForms = selectedAssignment,
        )
        items.add(taskForm)

        FireRepository.instance.alterFirestoreItems(items).first.observeOnce{
            _taskFormCreated.postValue(true)
        }
    }

}