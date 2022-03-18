package com.example.project_skripsi.module.student.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.ActivityStMainBinding
import com.example.project_skripsi.databinding.ActivityStTaskBinding

class StTaskViewModel : ViewModel() {

    companion object {
        const val NAVIGATION_EXAM = 0
        const val NAVIGATION_ASSIGNMENT = 1
        const val NAVIGATION_FORM = 2
    }

    private val _navType = MutableLiveData<Int>()
    val navType : LiveData<Int> = _navType

    fun setNavigationType(type : Int) {
        _navType.value = type
    }


}