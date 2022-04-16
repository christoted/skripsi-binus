package com.example.project_skripsi.module.teacher.form.alter_task

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
    }

    var selectedClass = listOf<StudyClass>()
    var selectedResource = listOf<Resource>()
    var selectedAssignment = listOf<TaskForm>()
    var taskType = ""

    private val _startDate = MutableLiveData<Date>()
    val startDate : LiveData<Date> = _startDate

    private val _endDate = MutableLiveData<Date>()
    val endDate : LiveData<Date> = _endDate

    private val _classList = MutableLiveData<List<StudyClass>>()
    val classList : LiveData<List<StudyClass>> = _classList

    private val _resourceList = MutableLiveData<List<Resource>>()
    val resourceList : LiveData<List<Resource>> = _resourceList

    private val _assignmentList = MutableLiveData<List<TaskForm>>()
    val assignmentList : LiveData<List<TaskForm>> = _assignmentList

    private lateinit var subjectGroup : SubjectGroup
    private lateinit var currentTeacher: Teacher
    private val classIds = mutableListOf<String>()

    private var formType: Int = -1
    private val resourceIds = mutableListOf<String>()
    private val assignmentIds = mutableListOf<String>()


    fun initData(subjectName: String, gradeLevel: Int, formType : Int) {
        _startDate.postValue(DateHelper.getCurrentDate())
        _endDate.postValue(DateHelper.getCurrentDate())
        subjectGroup = SubjectGroup(subjectName, gradeLevel)
        this.formType = formType
        loadTeacher(AuthRepository.instance.getCurrentUser().uid)
    }

    private fun loadTeacher(uid : String) {
        FireRepository.instance.getTeacher(uid).first.observeOnce { teacher ->
            currentTeacher = teacher
            teacher.teachingGroups?.firstOrNull { it.subjectName == subjectGroup.subjectName && it.gradeLevel == subjectGroup.gradeLevel }
                ?.let { group ->
                    with(group) {
                        teaching_classes?.let { classIds.addAll(it) }
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


    fun submitForm(title: String) {
        val taskFormId = UUIDHelper.getUUID()
        currentTeacher.teachingGroups
            ?.firstOrNull{it.subjectName == subjectGroup.subjectName && it.gradeLevel == subjectGroup.gradeLevel}
            ?.let { if(formType == TYPE_EXAM) it.createdExams else it.createdAssignments }
            ?.add(taskFormId)

        val newTask = TaskForm(
            id = taskFormId,
            title = title,
            gradeLevel = subjectGroup.gradeLevel,
            type = taskType,
            startTime = startDate.value,
            endTime = endDate.value,
            location = "Online",
            subjectName = subjectGroup.subjectName,
            questions = emptyList(),
            assignedClasses = selectedClass.map { it.id!! },
        )

        FireRepository.instance.addTaskForm(
            newTask,
            currentTeacher
        )
    }



}