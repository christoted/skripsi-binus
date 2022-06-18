package com.example.project_skripsi.module.teacher.resource.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.Resource
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.model.firestore.Teacher
import com.example.project_skripsi.core.model.local.SubjectGroup
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.helper.UUIDHelper

class TcAlterResourceViewModel: ViewModel() {

    companion object {
        const val QUERY_CLASS = 0
        const val QUERY_RESOURCE = 1
        val meetingNumbers = List(65) { it+1 }
    }

    private lateinit var subjectGroup: SubjectGroup
    private lateinit var currentTeacher: Teacher

    private val resourceIds = mutableListOf<String>()
    private val _resourceList = MutableLiveData<List<Resource>>()
    val resourceList : LiveData<List<Resource>> = _resourceList

    private val assignmentIds = mutableListOf<String>()
    private val _assignmentList = MutableLiveData<List<TaskForm>>()
    val assignmentList : LiveData<List<TaskForm>> = _assignmentList

    private val classIds = mutableListOf<String>()
    private val _classList = MutableLiveData<List<StudyClass>>()
    var classList: LiveData<List<StudyClass>> = _classList
    var selectedClass = listOf<String>()
    var selectedResource = listOf<String>()

    // Update Resource
    private val _singleResource = MutableLiveData<Resource>()
    val singleResource: LiveData<Resource> = _singleResource

    private val _status = MutableLiveData<Boolean>()
    val status: LiveData<Boolean> = _status

    var isValid = true
    var resourceDocumentId = ""
    private var isFirstTimeCreated = true

    init {
       loadTeacher(AuthRepository.inst.getCurrentUser().uid)
    }

    fun initData(subjectName: String, gradeLevel: Int) {
        subjectGroup = SubjectGroup(subjectName, gradeLevel)
    }

    fun getAlterResourceData() {
        isFirstTimeCreated = false
        FireRepository.inst.getItem<Resource>(resourceDocumentId).first.observeOnce {
            _singleResource.postValue(it)
            selectedClass = it.assignedClasses ?: emptyList()
            selectedResource = it.prerequisites ?: emptyList()
        }
    }

    private fun loadTeacher(uid: String) {
        FireRepository.inst.getItem<Teacher>(uid).first.observeOnce {
            currentTeacher = it
            it.teachingGroups.let { teachingGroups ->
                teachingGroups?.map { teachingGroup ->
                    if (teachingGroup.subjectName == subjectGroup.subjectName && teachingGroup.gradeLevel == subjectGroup.gradeLevel) {
                        teachingGroup.createdAssignments?.let { ids ->
                            assignmentIds.addAll(ids)
                        }
                        teachingGroup.createdResources?.let { ids ->
                            resourceIds.addAll(ids)
                        }
                        teachingGroup.teachingClasses?.let { ids ->
                            classIds.addAll(ids)
                        }
                    }
                }
            }
        }
    }

    // Load Class
    fun loadClass() {
        FireRepository.inst.getItems<StudyClass>(classIds).first.observeOnce { _classList.postValue(it) }
    }

    // Load Resource
    fun loadResource() {
        FireRepository.inst.getItems<Resource>(resourceIds).first.observeOnce { _resourceList.postValue(it) }
    }


    fun submitResource(title: String, meetingNumber: Int, link: String) {
        val items = mutableListOf<Any>()
        val resourceId = if (isFirstTimeCreated) UUIDHelper.getUUID() else resourceDocumentId
        if (isFirstTimeCreated) {
            currentTeacher.teachingGroups?.firstOrNull { it.subjectName == subjectGroup.subjectName && it.gradeLevel == subjectGroup.gradeLevel }
                ?.let {
                    it.createdResources?.add(resourceId)
                }
            items.add(currentTeacher)
        }

        val resource = Resource(
            id = resourceId,
            title = title,
            gradeLevel = subjectGroup.gradeLevel,
            meetingNumber = meetingNumber,
            link = link,
            subjectName = subjectGroup.subjectName,
            prerequisites = selectedResource,
            assignedClasses = selectedClass
        )
        items.add(resource)

        FireRepository.inst.getAllItems<StudyClass>().first.observeOnce { list ->
            list.filter {
                it.gradeLevel == subjectGroup.gradeLevel
            }.map { studyClass ->
                var needUpdate = false

                studyClass.subjects?.filter {
                    it.subjectName == subjectGroup.subjectName
                }?.map { subject ->
                    subject.classMeetings?.filter {
                        it.meetingResource == resourceId
                    }?.map {
                        needUpdate = true
                        it.meetingResource = null
                    }
                }

                studyClass.subjects?.filter {
                    it.subjectName == subjectGroup.subjectName
                }?.map { subject ->
                    subject.classMeetings?.sortedBy {
                        it.startTime
                    }?.getOrNull(meetingNumber)?.let {
                        needUpdate = true
                        it.meetingResource = resourceId
                    }
                }

                if (needUpdate) items.add(studyClass)
            }

            FireRepository.inst.alterItems(items).first.observeOnce{
                _status.postValue(it)
            }
        }
    }
}