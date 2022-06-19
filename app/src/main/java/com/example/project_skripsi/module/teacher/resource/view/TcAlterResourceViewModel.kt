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
        val meetingNumbers = List(65) { it + 1 }
        val mapOfMeetingLink = mapOf(
            "1" to "https://drive.google.com/drive/folders/1jCFAuBEyTom1Yr0e_iILHqioNZfmYQUr?usp=sharing",
            "2" to "https://drive.google.com/drive/folders/11JhZKcB84TZgKGhpYs2kq9TynYdtXb4C?usp=sharing",
            "3" to "https://drive.google.com/drive/folders/1eOqA-RUmnr1QRTRstl9KPDPAXjnn-88e?usp=sharing",
            "4" to "https://drive.google.com/drive/folders/1bhQzv-5R9TpKfJG7H2XtI1DkVU1HyVKw?usp=sharing",
            "5" to "https://drive.google.com/drive/folders/1FEiUQGa7o6Uibl_tTsHT4KcZ4jEB2-p6?usp=sharing",
            "6" to "https://drive.google.com/drive/folders/1BlKne3pMEPRlHKIBLloy3I9dDYsTcCUN?usp=sharing",
            "7" to "https://drive.google.com/drive/folders/1tKbYNXCVlZS11uhgStd8rKd3Hcmixqyg?usp=sharing",
            "8" to "https://drive.google.com/drive/folders/1WBlBsDR70BuybUZXjLRGmp58hgB-_G-s?usp=sharing",
            "9" to "https://drive.google.com/drive/folders/1A7uD6v3s1QctIAidElDt8oYZtbypMsom?usp=sharing",
            "10" to "https://drive.google.com/drive/folders/1tyDuP4VfDcBPexAlpgacGIGXFoQFvgKv?usp=sharing",
        )
    }

    private lateinit var subjectGroup: SubjectGroup
    private lateinit var currentTeacher: Teacher

    private val resourceIds = mutableListOf<String>()
    private val _resourceList = MutableLiveData<List<Resource>>()
    val resourceList : LiveData<List<Resource>> = _resourceList

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
    var isFirstTimeCreated = true

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
                        teachingGroup.createdResources?.let { ids -> resourceIds.addAll(ids) }
                        teachingGroup.teachingClasses?.let { ids -> classIds.addAll(ids) }
                    }
                }
            }
        }
    }

    // Load Class
    fun loadClass() {
        FireRepository.inst.getItems<StudyClass>(classIds).first.observeOnce { list ->
            _classList.postValue(list.sortedBy { it.name })
        }
    }

    // Load Resource
    fun loadResource() {
        FireRepository.inst.getItems<Resource>(resourceIds).first.observeOnce { list ->
            _resourceList.postValue(list.sortedBy { it.meetingNumber })
        }
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
                it.gradeLevel == subjectGroup.gradeLevel &&
                        selectedClass.contains(it.id)
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
                    subject.classMeetings
                        ?.sortedBy {
                            it.startTime
                        }?.getOrNull(meetingNumber-1)?.let {
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