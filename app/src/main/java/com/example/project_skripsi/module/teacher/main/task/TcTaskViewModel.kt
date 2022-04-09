package com.example.project_skripsi.module.teacher.main.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.model.local.SubjectGroup
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class TcTaskViewModel : ViewModel() {

    private val _subjectGroupList = MutableLiveData<List<SubjectGroup>>()
    val subjectGroupList : LiveData<List<SubjectGroup>> = _subjectGroupList

    private val _assignmentList = MutableLiveData<List<TaskForm>>()
    val assignmentList : LiveData<List<TaskForm>> = _assignmentList

    private val _examList = MutableLiveData<List<TaskForm>>()
    val examList : LiveData<List<TaskForm>> = _examList

    private val examIds = mutableMapOf<SubjectGroup, MutableList<String>>()
    private val assignmentIds = mutableMapOf<SubjectGroup, MutableList<String>>()

    var currentSubjectGroup : SubjectGroup? = null

    init {
        loadTeacher(AuthRepository.instance.getCurrentUser().uid)
    }

    private fun loadTeacher(uid : String) {
        FireRepository.instance.getTeacher(uid).first.observeOnce { teacher ->
            val subjectGroups = mutableListOf<SubjectGroup>()
            with(teacher) {
                teachingGroups?.map { group ->
                    val sg = SubjectGroup(group.subjectName!!, group.gradeLevel!!)
                    subjectGroups.add(sg)
                    group.createdExams?.map { examIds.getOrPut(sg) { mutableListOf()}.add(it) }
                    group.createdAssignments?.map { assignmentIds.getOrPut(sg) { mutableListOf()}.add(it) }
                }
            }
            _subjectGroupList.postValue(subjectGroups)
        }
    }

    fun selectSubjectGroup(subjectGroup: SubjectGroup) {
        currentSubjectGroup = subjectGroup
        loadExam(subjectGroup)
        loadAssignment(subjectGroup)
    }

    private fun loadExam(subjectGroup : SubjectGroup) {
        examIds[subjectGroup]?.toList()?.let { loadTaskForm(it, _examList) }
    }

    private fun loadAssignment(subjectGroup : SubjectGroup) {
        assignmentIds[subjectGroup]?.toList()?.let { loadTaskForm(it, _assignmentList) }
    }

    private fun loadTaskForm(uids: List<String>, mutableLiveData: MutableLiveData<List<TaskForm>>) {
        val taskFormList = mutableListOf<TaskForm>()
        uids.map { uid ->
            FireRepository.instance.getTaskForm(uid).first.observeOnce {
                taskFormList.add(it)
                if (taskFormList.size == uids.size) mutableLiveData.postValue(taskFormList)
            }
        }
    }

}