package com.example.project_skripsi.module.teacher.form.preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.StudyClass
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

    private val _studyClass = MutableLiveData<StudyClass>()
    val studyClass: LiveData<StudyClass> = _studyClass


    fun setTaskForm(studyClassId : String, taskFormId : String) {
        loadTaskForm(taskFormId)
        loadStudyClass(studyClassId)
    }

    private fun loadTaskForm(uid: String) {
        FireRepository.instance.getTaskForm(uid).first.observeOnce { _taskForm.postValue(it) }
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.instance.getStudyClass(uid).first.observeOnce { _studyClass.postValue(it) }
    }

}