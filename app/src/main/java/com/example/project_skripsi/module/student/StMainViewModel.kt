package com.example.project_skripsi.module.student

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_skripsi.core.model.firestore.AssignedTaskForm
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.model.local.AssignedQuestion
import com.example.project_skripsi.core.model.local.TaskFormStatus
import com.example.project_skripsi.core.model.local.TaskFormTimer
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.Constant.Companion.TASK_FORM_ESSAY
import com.example.project_skripsi.utils.Constant.Companion.TASK_FORM_MC
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.generic.HandledEvent
import com.example.project_skripsi.utils.helper.DateHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StMainViewModel : ViewModel() {

    private val _isImageCaptured = MutableLiveData<HandledEvent<Boolean>>()
    val isImageCaptured : LiveData<HandledEvent<Boolean>> = _isImageCaptured

    fun captureImage(){
        _isImageCaptured.postValue(HandledEvent(true))
    }

}