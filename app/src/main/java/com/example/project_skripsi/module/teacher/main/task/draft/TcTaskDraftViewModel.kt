package com.example.project_skripsi.module.teacher.main.task.draft

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

class TcTaskDraftViewModel : ViewModel() {

    companion object {
        const val TAB_EXAM = 0
        const val TAB_ASSIGNMENT = 1
        const val tabCount = 2
        val tabHeader = arrayOf("Ujian", "Tugas")
    }

    private val _assignmentList = MutableLiveData<List<TaskForm>>()
    val assignmentList: LiveData<List<TaskForm>> = _assignmentList

    private val _examList = MutableLiveData<List<TaskForm>>()
    val examList: LiveData<List<TaskForm>> = _examList

    private val examIds = mutableListOf<String>()
    private val assignmentIds = mutableListOf<String>()

    lateinit var currentSubjectGroup: SubjectGroup

    fun setSubjectGroup(subjectGroup: SubjectGroup) {
        currentSubjectGroup = subjectGroup
    }

    fun refreshData() {
        loadTeacher(AuthRepository.inst.getCurrentUser().uid)
    }

    private fun loadTeacher(uid: String) {
        FireRepository.inst.getItem<Teacher>(uid).first.observeOnce { teacher ->
            with(teacher) {
                teachingGroups?.firstOrNull { group ->
                    SubjectGroup(group.subjectName!!, group.gradeLevel!!) == currentSubjectGroup
                }?.let { group ->
                    group.createdExams?.let {
                        examIds.addAll(it)
                        loadTaskForm(it, _examList)
                    }
                    group.createdAssignments?.let {
                        assignmentIds.addAll(it)
                        loadTaskForm(it, _assignmentList)
                    }
                }
            }
        }
    }

    private fun loadTaskForm(uids: List<String>, mutableLiveData: MutableLiveData<List<TaskForm>>) {
        FireRepository.inst.getItems<TaskForm>(uids).first.observeOnce {
            mutableLiveData.postValue(
                it.filter { taskForm -> taskForm.isFinalized == false }
            )
        }
    }

    fun getTaskFormType(taskFormId: String): Int? {
        examIds.let { if (it.contains(taskFormId)) return TcAlterTaskViewModel.TYPE_EXAM }
        assignmentIds.let { if (it.contains(taskFormId)) return TcAlterTaskViewModel.TYPE_ASSIGNMENT }
        return null
    }

}