package com.example.project_skripsi.module.teacher.main.resource.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_skripsi.core.model.firestore.Resource
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TcResourceViewModel: ViewModel() {
    // Resources
    private val resourceList: MutableList<Resource> = mutableListOf()
    private val _resources: MutableLiveData<List<Resource>> = MutableLiveData()
    private val _selectedResources: MutableLiveData<List<Resource>> = MutableLiveData()
    val resources: LiveData<List<Resource>> = _resources
    val selectedResources: LiveData<List<Resource>> = _selectedResources

    // Subject by class for Chip
    private val mapResourceBySubject: MutableLiveData<Map<String, List<Resource>>> = MutableLiveData()
    private val _subjectByClass: MutableLiveData<List<Resource>> = MutableLiveData()
    val subjectByClass: LiveData<List<Resource>> = _subjectByClass

    init {
        viewModelScope.launch(Dispatchers.Main) {
            loadTeacher(AuthRepository.instance.getCurrentUser().uid)
        }
    }

    private fun loadTeacher(uid: String) {
        FireRepository.instance.getTeacher(uid).first.observeOnce {
            it.teachingGroups?.map { teachingGroup ->
                teachingGroup.createdResources?.map { id ->
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
        val resources: MutableList<Resource> = mutableListOf()
        mapResourceBySubject.observeOnce {
                it.keys.forEach { key ->
                    val data = mapResourceBySubject.value?.get(key)
                    data?.distinctBy { it.subjectName }.let {
                        resources.addAll(it!!)
                    }
                }
            _subjectByClass.postValue(resources)
        }

    }

    fun loadResourceBySubjectNameAndGradeLevel(resource: Resource?, isChecked: Boolean) {
        if (isChecked) {
            Log.d("Check", "onViewCreated: " + _resources.value)
            _selectedResources.postValue(_resources.value?.filter { it.gradeLevel == resource?.gradeLevel && it.subjectName == resource?.subjectName })
            Log.d("Check", "selected resource : " + _selectedResources.value)
        }
    }
}