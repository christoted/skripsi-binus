package com.example.project_skripsi.module.teacher.study_class.resource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.Resource
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class TcStudyClassResourceViewModel : ViewModel() {

    private val _studyClass = MutableLiveData<StudyClass>()
    val studyClass: LiveData<StudyClass> = _studyClass

    private val _resourceList = MutableLiveData<List<Resource>>()
    val resourceList: LiveData<List<Resource>> = _resourceList


    fun setClassAndSubject(studyClassId: String, subjectName: String) {
        loadStudyClass(studyClassId, subjectName)
    }

    private fun loadStudyClass(uid: String, subjectName: String) {
        FireRepository.inst.getStudyClass(uid).let { response ->
            response.first.observeOnce { studyClass ->
                _studyClass.postValue(studyClass)
                studyClass.subjects?.firstOrNull { it.subjectName == subjectName }?.classResources?.let {
                    loadResources(it)
                }
            }
        }
    }


    private fun loadResources(uids: List<String>) {
        val resourceList = ArrayList<Resource>()
        uids.map { uid ->
            FireRepository.inst.getResource(uid).let { response ->
                response.first.observeOnce {
                    resourceList.add(it)
                    if (resourceList.size == uids.size) _resourceList.postValue(resourceList.toList())
                }
            }
        }
    }


}