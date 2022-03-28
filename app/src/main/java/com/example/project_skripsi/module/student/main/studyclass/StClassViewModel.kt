package com.example.project_skripsi.module.student.main.studyclass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.Subject
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import kotlin.math.min

class StClassViewModel : ViewModel() {

    private val _className = MutableLiveData<String>()
    val className: LiveData<String> = _className

    private val _teacherName = MutableLiveData<String>()
    val teacherName: LiveData<String> = _teacherName

    private val _teacherPhoneNumber = MutableLiveData<String>()
    val teacherPhoneNumber: LiveData<String> = _teacherPhoneNumber

    private val _classChiefName = MutableLiveData<String>()
    val classChiefName: LiveData<String> = _classChiefName

    private val _classChiefPhoneNumber = MutableLiveData<String>()
    val classChiefPhoneNumber: LiveData<String> = _classChiefPhoneNumber

    private val _subjectList = MutableLiveData<List<Subject>>()
    val subjectList: LiveData<List<Subject>> = _subjectList

    init {
        loadStudent(AuthRepository.instance.getCurrentUser().uid)
    }

    private fun loadStudent(uid: String) {
        FireRepository.instance.getStudent(uid).let { response ->
            response.first.observeForever { student ->
                student.studyClass?.let { loadStudyClass(it) }
            }
        }
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.instance.getStudyClass(uid).let { response ->
            response.first.observeForever { studyClass ->
                studyClass.name?.let { _className.postValue(it) }
                studyClass.homeroomTeacher?.let { loadTeacher(it) }
                studyClass.classChief?.let { loadClassChief(it) }
                studyClass.subjects?.let { _subjectList.postValue(it) }
            }
        }
    }

    private fun loadTeacher(uid: String) {
        FireRepository.instance.getTeacher(uid).let { response ->
            response.first.observeForever { teacher ->
                teacher.name?.let { _teacherName.postValue(it) }
                teacher.phoneNumber?.let { _teacherPhoneNumber.postValue(it) }
            }
        }
    }

    private fun loadClassChief(uid: String) {
        FireRepository.instance.getStudent(uid).let { response ->
            response.first.observeForever { classChief ->
                classChief.name?.let { _classChiefName.postValue(it) }
                classChief.phoneNumber?.let { _classChiefPhoneNumber.postValue(it) }
            }
        }
    }


    fun getSubjects(page: Int): List<Subject> {
        val startIdx = page * 8
        val endIdx = min(startIdx + 8, subjectList.value?.size?:0)
        return subjectList.value?.subList(startIdx, endIdx)?: emptyList()
    }

    fun getSubjectPageCount() : Int{
        val subjects = subjectList.value?.size ?: 0
        return (subjects + 8 - 1) / 8
    }

}