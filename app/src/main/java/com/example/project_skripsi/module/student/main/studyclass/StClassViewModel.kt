package com.example.project_skripsi.module.student.main.studyclass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.core.model.firestore.Subject
import com.example.project_skripsi.core.model.firestore.Teacher
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import kotlin.math.min

class StClassViewModel : ViewModel() {

    private val _studyClass = MutableLiveData<StudyClass>()
    val studyClass: LiveData<StudyClass> = _studyClass

    private val _teacher = MutableLiveData<Teacher>()
    val teacher: LiveData<Teacher> = _teacher

    private val _classChief = MutableLiveData<Student>()
    val classChief: LiveData<Student> = _classChief

    init {
        loadStudent(AuthRepository.instance.getCurrentUser().uid)
    }

    private fun loadStudent(uid: String) {
        FireRepository.inst.getStudent(uid).let { response ->
            response.first.observeOnce { student ->
                student.studyClass?.let { loadStudyClass(it) }
            }
        }
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.inst.getStudyClass(uid).let { response ->
            response.first.observeOnce { studyClass ->
                with(studyClass) {
                    _studyClass.postValue(this)
                    homeroomTeacher?.let { loadTeacher(it) }
                    classChief?.let { loadClassChief(it) }
                }
            }
        }
    }

    private fun loadTeacher(uid: String) {
        FireRepository.inst.getTeacher(uid).let { response ->
            response.first.observeOnce { _teacher.postValue(it) }
        }
    }

    private fun loadClassChief(uid: String) {
        FireRepository.inst.getStudent(uid).let { response ->
            response.first.observeOnce { _classChief.postValue(it) }
        }
    }


    fun getSubjects(page: Int): List<Subject> {
        val startIdx = page * 8
        val endIdx = min(startIdx + 8, studyClass.value?.subjects?.size?:0)
        return studyClass.value?.subjects?.subList(startIdx, endIdx)?: emptyList()
    }

    fun getSubjectPageCount() : Int{
        val subjects = studyClass.value?.subjects?.size ?: 0
        return (subjects + 8 - 1) / 8
    }

}