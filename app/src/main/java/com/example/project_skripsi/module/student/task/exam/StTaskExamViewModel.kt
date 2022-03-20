package com.example.project_skripsi.module.student.task.exam

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.FieldPosition

class StTaskExamViewModel : ViewModel() {

    companion object {
        const val EXAM_ONGOING = 0
        const val EXAM_PAST = 1
        const val tabCount = 2
        val tabHeader = arrayOf("Berlangsung", "Selesai")
    }

    private val _list = MutableLiveData<List<String>>()
    val list : LiveData<List<String>> = _list


    init {
        Handler(Looper.getMainLooper()).postDelayed({
            _list.value = listOf("XII-IPA-3", "XII-IPA-5")
        }, 1000)
    }

    fun getExamList(position: Int) : List<String> =
        when (position) {
            EXAM_ONGOING -> list.value ?: emptyList()
            EXAM_PAST -> list.value ?: emptyList()
            else -> emptyList()
        }


}