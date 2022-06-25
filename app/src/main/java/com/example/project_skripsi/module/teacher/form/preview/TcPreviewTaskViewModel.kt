package com.example.project_skripsi.module.teacher.form.preview

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

        const val GROUP_CLASS = 0
        const val GROUP_RESOURCE = 1
        const val GROUP_ASSIGNMENT = 2
    }


    private val _oldTaskForm = MutableLiveData<TaskForm>()
    val oldTaskForm: LiveData<TaskForm> = _oldTaskForm

    private val _studyClasses = MutableLiveData<List<String>>()
    val studyClasses : LiveData<List<String>> = _studyClasses

    private val _prerequisiteResource = MutableLiveData<List<String>>()
    val prerequisiteResource : LiveData<List<String>> = _prerequisiteResource

    private val _prerequisiteTaskForm = MutableLiveData<List<String>>()
    val prerequisiteTaskForm : LiveData<List<String>> = _prerequisiteTaskForm

    var isExpanded = BooleanArray(3) {false}

    private var formType: Int = -1


    fun initData(formType: Int, taskFormId: String) {
        this.formType = formType
        loadTaskForm(taskFormId)
    }

    private fun loadTaskForm(uid: String) {
        FireRepository.inst.getItem<TaskForm>(uid).first.observeOnce { taskForm ->
            _oldTaskForm.postValue(taskForm)
            taskForm.assignedClasses?.let { loadClasses(it) }
            taskForm.prerequisiteResources?.let { loadResources(it) }
            taskForm.prerequisiteTaskForms?.let { loadTaskForms(it) }
        }
    }

    private fun loadClasses(uids: List<String>) {
        FireRepository.inst.getItems<StudyClass>(uids).first.observeOnce { list ->
            _studyClasses.postValue(
                list.map { it.name ?: "" }.sortedBy { it }
            )
        }
    }

    private fun loadResources(uids: List<String>) {
        FireRepository.inst.getItems<Resource>(uids).first.observeOnce { list ->
            _prerequisiteResource.postValue(
                list.sortedBy { it.meetingNumber }.map { it.title ?: "" }
            )
        }
    }

    private fun loadTaskForms(uids: List<String>) {
        FireRepository.inst.getItems<TaskForm>(uids).first.observeOnce { list ->
            _prerequisiteTaskForm.postValue(
                list.sortedBy { it.startTime }.map { it.title ?: "" }
            )
        }
    }

}