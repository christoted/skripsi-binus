package com.example.project_skripsi.module.teacher.main.study_class

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.core.model.firestore.Teacher
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class TcStudyClassViewModel : ViewModel() {


    private val _homeroomClass = MutableLiveData<StudyClass>()
    val homeroomClass : LiveData<StudyClass> = _homeroomClass

    private val _teachingSubjects = MutableLiveData<List<String>>()
    val teachingSubjects : LiveData<List<String>> = _teachingSubjects

    private val _teachingClasses = MutableLiveData<Pair<String, List<StudyClass>>>()
    val teachingClasses : LiveData<Pair<String, List<StudyClass>>> = _teachingClasses

    private val subjectClasses = mutableMapOf<String, MutableList<String>>()

    init {
        loadTeacher(AuthRepository.inst.getCurrentUser().uid)
    }

    private fun loadTeacher(uid : String) {
        FireRepository.inst.getItem<Teacher>(uid).first.observeOnce { teacher ->
            with(teacher) {
                homeroomClass?.let { loadHomeroomClass(it) }

                val subjects: MutableList<String> = mutableListOf()
                teachingGroups?.map { group ->
                    group.teachingClasses?.map {
                        if (!subjects.contains(group.subjectName!!)) subjects.add(group.subjectName!!)
                        subjectClasses.getOrPut(group.subjectName!!) { mutableListOf() }.add(it)
                    }
                }
                _teachingSubjects.postValue(subjects)
            }
        }
    }

    private fun loadHomeroomClass(uid: String) {
        FireRepository.inst.getItem<StudyClass>(uid).first.observeOnce { _homeroomClass.postValue(it) }
    }

    fun loadClasses(subjectName: String) {
        subjectClasses[subjectName]?.let { uids ->
            FireRepository.inst.getItems<StudyClass>(uids).first.observeOnce {
                _teachingClasses.postValue(Pair(subjectName, it))
            }
        }
    }

}