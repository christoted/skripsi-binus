package com.example.project_skripsi.module.parent.student_detail

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class PrStudentDetailViewModel : ViewModel() {

    private val _student = MutableLiveData<Student>()
    val student : LiveData<Student> = _student

    fun setStudent(studentId: String) {
        loadStudent(studentId)
    }

    private fun loadStudent(uid: String) {
        FireRepository.instance.getItem<Student>(uid).first.observeOnce{ _student.postValue(it) }
    }

}