package com.example.project_skripsi.module.student.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StTaskViewModel : ViewModel() {

    companion object {
        const val NAVIGATION_EXAM = 0
        const val NAVIGATION_ASSIGNMENT = 1
        const val NAVIGATION_FORM = 2
    }

    private val _navType = MutableLiveData<Int>()
    val navType : LiveData<Int> = _navType

    private val _taskFormId = MutableLiveData<String?>()
    val taskFormId : LiveData<String?> = _taskFormId

    fun setNavigationData(type : Int, taskFormId : String?) {
        _navType.value = type
        _taskFormId.value = taskFormId
    }


}