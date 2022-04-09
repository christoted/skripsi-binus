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
    var selectedClass = listOf<StudyClass>()
    var selectedResource = listOf<Resource>()
    var selectedAssignment = listOf<TaskForm>()

    private val _status = MutableLiveData<Boolean>()
    val status: LiveData<Boolean> = _status

    var isValid = true
    var materialType = ""

    init {
       loadTeacher(AuthRepository.instance.getCurrentUser().uid)
    }

    fun initData(subjectName: String, gradeLevel: Int) {
        subjectGroup = SubjectGroup(subjectName, gradeLevel)
    }

    private fun loadTeacher(uid: String) {
        FireRepository.instance.getTeacher(uid).let { response ->
            response.first.observeOnce {
                currentTeacher = it
                it.teachingGroups.let { teachingGroups ->
                    teachingGroups?.map {  teachingGroup ->
                        teachingGroup.createdAssignments?.let { ids ->
                            assignmentIds.addAll(ids)
                        }
                        teachingGroup.createdResources?.let { ids ->
                            resourceIds.addAll(ids)
                        }
                        teachingGroup.teaching_classes?.let { ids ->
                            classIds.addAll(ids)
                        }
                    }
                }
            }
        }
    }
    // Load Class
    fun loadClass() {
        val itemList = mutableListOf<StudyClass>()
        classIds.map { uid ->
            FireRepository.instance.getStudyClass(uid).first.observeOnce {
                itemList.add(it)
                if (itemList.size == classIds.size)  _classList.postValue(itemList)
            }
        }
    }
    // Load Resource
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
    fun submitResource(title: String, type: String, link: String) {
            val id = UUIDHelper.getUUID()
            currentTeacher.teachingGroups?.firstOrNull { it.subjectName == subjectGroup.subjectName && it.gradeLevel == subjectGroup.gradeLevel }?.let {
                it.createdResources?.add(id)
            }
            val resource = Resource(
                id = id,
                title = title,
                gradeLevel = subjectGroup.gradeLevel,
                type = type,
                link = link,
                subjectName = subjectGroup.subjectName,
                // MARK -
                prerequisites = mutableListOf(),
//            assignedClasses = selectedClass.map {
//                it.id!!
//            }
                assignedClasses = mutableListOf()
            )

            FireRepository.instance.addResource(resource, currentTeacher).let { response ->
                response.first.observeOnce {
                    _status.postValue(it)
                }
            }


    }
}