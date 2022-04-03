package com.example.project_skripsi.utils.helper

import android.app.Application
import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider
import com.example.project_skripsi.module.student.main.score.viewmodel.StScoreViewModel


class MyViewModelFactory() :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StScoreViewModel() as T
    }
}