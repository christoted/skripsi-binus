package com.example.project_skripsi.module.teacher.main.home.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.School
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.core.model.firestore.Teacher
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class TcProfileViewModel : ViewModel() {

    private val _teacher = MutableLiveData<Teacher>()
    val teacher: LiveData<Teacher> = _teacher

    private val _studyClass = MutableLiveData<StudyClass>()
    val studyClass: LiveData<StudyClass> = _studyClass

    private val _school = MutableLiveData<School>()
    val school: LiveData<School> = _school

    init {
        loadCurrentTeacher(AuthRepository.inst.getCurrentUser().uid)
    }

    private fun loadCurrentTeacher(uid: String) {
        FireRepository.inst.getItem<Teacher>(uid).first.observeOnce { teacher ->
            _teacher.postValue(teacher)
            teacher.homeroomClass?.let { it -> loadStudyClass(it) }
            teacher.school?.let { loadSchool(it) }
        }
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.inst.getItem<StudyClass>(uid).first.observeOnce { _studyClass.postValue(it) }
    }

    private fun loadSchool(uid: String) {
        FireRepository.inst.getItem<School>(uid).first.observeOnce { _school.postValue(it) }
    }
}