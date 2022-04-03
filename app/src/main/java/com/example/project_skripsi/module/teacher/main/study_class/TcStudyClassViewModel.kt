package com.example.project_skripsi.module.teacher.main.study_class

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.Resource
import com.example.project_skripsi.core.model.firestore.StudyClass
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

    private val subjectsDetail : MutableMap<String, List<String>> = mutableMapOf()

    init {
        loadTeacher(AuthRepository.instance.getCurrentUser().uid)
    }

    private fun loadTeacher(uid : String) {
        FireRepository.instance.getTeacher(uid).let { response ->
            response.first.observeOnce { teacher ->
                with(teacher) {
                    homeroomClass?.let { loadHomeroomClass(it) }

                    val subjects : MutableList<String> = mutableListOf()
                    teachingSubjects?.map { subject ->
                        with(subject) {
                            subjectName?.let { subjectName ->
                                subjects.add(subjectName)
                                teaching_class?.let { it -> subjectsDetail.put(subjectName, it) }
                            }
                        }
                    }
                    _teachingSubjects.postValue(subjects)
                }
            }
        }
    }

    private fun loadHomeroomClass(uid: String) {
        FireRepository.instance.getStudyClass(uid).let { response ->
            response.first.observeOnce { studyClass ->
                _homeroomClass.postValue(studyClass)
            }
        }
    }

    fun loadClasses(subjectName: String) {
        val uids = subjectsDetail[subjectName]
        val classList = mutableListOf<StudyClass>()
        uids?.map { uid ->
            FireRepository.instance.getStudyClass(uid).let { response ->
                response.first.observeOnce {
                    classList.add(it)
                    if (classList.size == uids.size) {
                        _teachingClasses.postValue(Pair(subjectName, classList.toList()))
                    }
                }
            }
        }
    }

}