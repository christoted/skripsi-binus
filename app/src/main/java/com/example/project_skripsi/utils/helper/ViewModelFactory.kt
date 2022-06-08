package com.example.project_skripsi.utils.helper

import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider
import com.example.project_skripsi.module.student.main.progress.viewmodel.StScoreViewModel


class MyViewModelFactory() :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StScoreViewModel() as T
    }
}