package com.example.project_skripsi.module.student.main.score.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.module.student.main.home.viewmodel.HomeMainSection

class StScoreViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is score Fragment"
    }
    val text: LiveData<String> = _text

    private val _sectionDatas = MutableLiveData<List<String>>()
    val sectionDatas: LiveData<List<String>> = _sectionDatas

    companion object {
        const val tabCount = 3
        val tabHeader = arrayOf("Nilai", "Absensi", "Pencapaian")
    }

    init {
        Handler(Looper.getMainLooper()).postDelayed({
            val listExam = arrayListOf<String>()
            listExam.add("90")
            listExam.add("100")
            listExam.add("101")
            _sectionDatas.value = listExam
        }, 1000)
    }

}