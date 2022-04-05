package com.example.project_skripsi.module.teacher.main.resource.viewmodel

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
    private val _subjectByClass: MutableLiveData<List<String>> = MutableLiveData()
    val subjectByClass: LiveData<List<String>> = _subjectByClass

    init {
        loadTeacher(AuthRepository.instance.getCurrentUser().uid)
    }

    private fun loadTeacher(uid: String) {
        FireRepository.instance.getTeacher(uid).first.observeOnce {
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
        }
        _resources.postValue(resourceList)
    }
}