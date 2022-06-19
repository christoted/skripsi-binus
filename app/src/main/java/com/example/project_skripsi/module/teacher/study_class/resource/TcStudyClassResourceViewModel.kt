package com.example.project_skripsi.module.teacher.study_class.resource

import android.util.Log
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
        FireRepository.inst.getItem<StudyClass>(uid).first.observeOnce { studyClass ->
            _studyClass.postValue(studyClass)
            loadResources(
                studyClass.subjects
                    ?.firstOrNull { it.subjectName == subjectName }?.classMeetings?.mapNotNull {
                        if (it.meetingResource.isNullOrEmpty()) null else it.meetingResource
                    } ?: emptyList()
            )
        }
    }

    private fun loadResources(uids: List<String>) {
        FireRepository.inst.getItems<Resource>(uids).first.observeOnce { list ->
            _resourceList.postValue(
                list.sortedBy { it.meetingNumber }
            )
        }
    }

}