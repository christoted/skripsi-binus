package com.example.project_skripsi.module.teacher.form.preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.model.firestore.Teacher
import com.example.project_skripsi.core.model.local.SubjectGroup
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.Constant.Companion.TASK_TYPE_ASSIGNMENT
import com.example.project_skripsi.utils.Constant.Companion.TASK_TYPE_FINAL_EXAM
import com.example.project_skripsi.utils.Constant.Companion.TASK_TYPE_MID_EXAM
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class TcPreviewTaskViewModel : ViewModel() {

    companion object {
        val mapFormType = mapOf(
            TASK_TYPE_MID_EXAM to "Ujian Tengah Semester",
            TASK_TYPE_FINAL_EXAM to "Ujian Akhir Semester",
            TASK_TYPE_ASSIGNMENT to "Tugas"
        )
    }


    private val _oldTaskForm = MutableLiveData<TaskForm>()
    val oldTaskForm: LiveData<TaskForm> = _oldTaskForm

    private lateinit var subjectGroup: SubjectGroup
    private val classIds = mutableListOf<String>()

    private var formType: Int = -1


    fun initData(subjectName: String, gradeLevel: Int, formType: Int, taskFormId: String) {
        subjectGroup = SubjectGroup(subjectName, gradeLevel)
        this.formType = formType
        loadTeacher(AuthRepository.inst.getCurrentUser().uid)
        loadTaskForm(taskFormId)
    }

    private fun loadTeacher(uid: String) {
        FireRepository.inst.getItem<Teacher>(uid).first.observeOnce { teacher ->
            teacher.teachingGroups?.firstOrNull { it.subjectName == subjectGroup.subjectName && it.gradeLevel == subjectGroup.gradeLevel }
                ?.let { group ->
                    group.teachingClasses?.let { classIds.addAll(it) }
                }
        }
    }

    private fun loadTaskForm(uid: String) {
        FireRepository.inst.getItem<TaskForm>(uid).first.observeOnce { _oldTaskForm.postValue(it) }
    }

}