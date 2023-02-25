package com.example.project_skripsi.module.teacher.form.preview.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class TcPreviewTaskFormViewModel : ViewModel() {

    companion object {
        val TASK_FORM_TYPE = mapOf(
            "pilihan berganda" to 0,
            "essai" to 1,
        )
    }

    private val _taskForm = MutableLiveData<TaskForm>()
    val taskForm: LiveData<TaskForm> = _taskForm

    fun setTaskForm(taskFormId: String) {
        loadTaskForm(taskFormId)
    }

    private fun loadTaskForm(uid: String) {
        FireRepository.inst.getItem<TaskForm>(uid).first.observeOnce { _taskForm.postValue(it) }
    }

}