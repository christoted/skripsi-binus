package com.example.project_skripsi.module.teacher.main.resource.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_skripsi.core.model.firestore.Resource
import com.example.project_skripsi.core.model.local.SubjectGroup
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TcResourceViewModel: ViewModel() {
    private val _subjectGroupList = MutableLiveData<List<SubjectGroup>>()
    val subjectGroupList : LiveData<List<SubjectGroup>> = _subjectGroupList

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
    private val _selectedChip: MutableLiveData<Resource> = MutableLiveData()
    val selectedChip: LiveData<Resource> = _selectedChip

    // New Approach
    private val mapResourceIdsBySubjectGroup = mutableMapOf<SubjectGroup, MutableList<String>>()

    init {
        loadTeacher(AuthRepository.instance.getCurrentUser().uid)
    }

    private fun loadTeacher(uid: String) {
        FireRepository.instance.getTeacher(uid).first.observeOnce {
            val subjectGroups = mutableListOf<SubjectGroup>()
            it.teachingGroups?.map { teachingGroup ->
                val sg = SubjectGroup(teachingGroup.subjectName!!, teachingGroup.gradeLevel!!)
                subjectGroups.add(sg)
                teachingGroup.createdResources?.map { mapResourceIdsBySubjectGroup.getOrPut(sg) { mutableListOf()}.add(it) }
            }
            _subjectGroupList.postValue(subjectGroups)
        }
    }

    fun loadResource(subjectGroup: SubjectGroup, isChecked: Boolean) {
        if (isChecked) {
            mapResourceIdsBySubjectGroup[subjectGroup]?.toList()?.let {
                loadResourceForm(it, _resources)
            }
        }
    }

    private fun loadResourceForm(uids: List<String>, mutableLiveData: MutableLiveData<List<Resource>>) {
        val resourceFormList = mutableListOf<Resource>()
        if (uids.isNotEmpty()) {
            uids.map {
                Log.d("Test Muncul", "loadResourceForm: atas " + it)
                FireRepository.instance.getResource(it).first.observeOnce {
                    resourceFormList.add(it)
                    _selectedChip.postValue(it)
                    Log.d("Test Muncul", "loadResourceForm: " + it)
                    if (resourceFormList.size == uids.size) mutableLiveData.postValue(resourceFormList)
                }
            }
        } else {
//            _selectedChip.postValue(it)
            mutableLiveData.postValue(mutableListOf())
        }

    }
}