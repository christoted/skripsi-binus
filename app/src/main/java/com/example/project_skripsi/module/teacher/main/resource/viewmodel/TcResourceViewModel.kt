package com.example.project_skripsi.module.teacher.main.resource.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.Resource
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class TcResourceViewModel: ViewModel() {
    // Resources
    private val resourceList: MutableList<Resource> = mutableListOf()
    private val _resources: MutableLiveData<List<Resource>> = MutableLiveData()
    val resources: LiveData<List<Resource>> = _resources

    // Subject by class
    private val mapResourceBySubject: MutableLiveData<Map<String, List<Resource>>> = MutableLiveData()
    private val _subjectByClass: MutableLiveData<List<Resource>> = MutableLiveData()
    val subjectByClass: LiveData<List<Resource>> = _subjectByClass

    init {
        loadTeacher(AuthRepository.instance.getCurrentUser().uid)
    }

    private fun loadTeacher(uid: String) {
        FireRepository.instance.getTeacher(uid).first.observeForever {
            it.createdResources?.let { ids ->
                ids.map { id ->
                    loadResource(id)
                }
            }
        }
    }

    private fun loadResource(uid: String) {
        FireRepository.instance.getResource(uid).first.observeOnce {
            resourceList.add(it)
            _resources.postValue(resourceList)
        }
        _resources.observeOnce {
            it?.let {
                mapResourceBySubject.postValue(it.groupBy { it.gradeLevel.toString() })
            }
        }
        mapResourceBySubject.observeOnce {
            val resources: MutableList<Resource> = mutableListOf()
            it.keys.forEach { key ->
                val data = mapResourceBySubject.value?.get(key)
                data?.distinctBy { it.subjectName }.let {
                    resources.addAll(it!!)
                }
            }
            _subjectByClass.postValue(resources)
        }
    }
}