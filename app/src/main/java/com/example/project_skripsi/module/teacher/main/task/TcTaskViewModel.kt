package com.example.project_skripsi.module.teacher.main.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.model.firestore.Teacher
import com.example.project_skripsi.core.model.local.SubjectGroup
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.module.teacher.form.alter.TcAlterTaskViewModel
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class TcTaskViewModel : ViewModel() {

    companion object {
        const val TAB_EXAM = 0
        const val TAB_ASSIGNMENT = 1
        const val tabCount = 2
        val tabHeader = arrayOf("Ujian", "Tugas")
    }

    private val _subjectGroupList = MutableLiveData<List<SubjectGroup>>()
    val subjectGroupList: LiveData<List<SubjectGroup>> = _subjectGroupList

    private val _assignmentList = MutableLiveData<List<TaskForm>>()
    val assignmentList: LiveData<List<TaskForm>> = _assignmentList

    private val _examList = MutableLiveData<List<TaskForm>>()
    val examList: LiveData<List<TaskForm>> = _examList

    private val examIds = mutableMapOf<SubjectGroup, MutableList<String>>()
    private val assignmentIds = mutableMapOf<SubjectGroup, MutableList<String>>()

    var currentSubjectGroup: SubjectGroup? = null

    fun refreshData() {
        examIds.clear()
        assignmentIds.clear()
        loadTeacher(AuthRepository.inst.getCurrentUser().uid)
    }

    private fun loadTeacher(uid: String) {
        FireRepository.inst.getItem<Teacher>(uid).first.observeOnce { teacher ->
            val subjectGroups = mutableListOf<SubjectGroup>()
            with(teacher) {
                teachingGroups?.map { group ->
                    val sg = SubjectGroup(group.subjectName!!, group.gradeLevel!!)
                    subjectGroups.add(sg)
                    group.createdExams?.map { examIds.getOrPut(sg) { mutableListOf() }.add(it) }
                    group.createdAssignments?.map {
                        assignmentIds.getOrPut(sg) { mutableListOf() }.add(it)
                    }
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


    private fun loadExam(subjectGroup: SubjectGroup) {
        loadTaskForm((examIds[subjectGroup]?.toList() ?: emptyList()), _examList)
    }

    private fun loadAssignment(subjectGroup: SubjectGroup) {
        loadTaskForm((assignmentIds[subjectGroup]?.toList() ?: emptyList()), _assignmentList)
    }

    private fun loadTaskForm(uids: List<String>, mutableLiveData: MutableLiveData<List<TaskForm>>) {
        FireRepository.inst.getItems<TaskForm>(uids).first.observeOnce { list ->
            mutableLiveData.postValue(
                list.filter { it.isFinalized == true }
                    .sortedBy { it.startTime }
            )
        }
    }

    fun getTaskFormType(taskFormId: String): Int? {
        examIds[currentSubjectGroup]?.let { if (it.contains(taskFormId)) return TcAlterTaskViewModel.TYPE_EXAM }
        assignmentIds[currentSubjectGroup]?.let { if (it.contains(taskFormId)) return TcAlterTaskViewModel.TYPE_ASSIGNMENT }
        return null
    }

    fun isChipPositionTop(position: Int): Boolean {
        if (position < 4) return true
        if (position < 8) return false
        return position % 2 == 0
    }

}