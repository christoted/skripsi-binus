package com.example.project_skripsi.module.student.main.home.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.School
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class StProfileViewModel : ViewModel() {

    private val _student = MutableLiveData<Student>()
    val student: LiveData<Student> = _student

    private val _studyClass = MutableLiveData<StudyClass>()
    val studyClass: LiveData<StudyClass> = _studyClass

    private val _school = MutableLiveData<School>()
    val school: LiveData<School> = _school

    init {
        loadCurrentStudent(AuthRepository.inst.getCurrentUser().uid)
    }

    private fun loadCurrentStudent(uid: String) {
        FireRepository.inst.getItem<Student>(uid).first.observeOnce { student ->
            _student.postValue(student)
            student.studyClass?.let { it -> loadStudyClass(it) }
            student.school?.let { loadSchool(it) }
        }
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.inst.getItem<StudyClass>(uid).first.observeOnce { _studyClass.postValue(it) }
    }

    private fun loadSchool(uid: String) {
        FireRepository.inst.getItem<School>(uid).first.observeOnce { _school.postValue(it) }
    }
}