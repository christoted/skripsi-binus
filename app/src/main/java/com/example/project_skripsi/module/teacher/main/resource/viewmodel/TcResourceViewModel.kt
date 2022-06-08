package com.example.project_skripsi.module.teacher.main.resource.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.Resource
import com.example.project_skripsi.core.model.local.SubjectGroup
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class TcResourceViewModel: ViewModel() {
    private val _subjectGroupList = MutableLiveData<List<SubjectGroup>>()
    var subjectGroupList : LiveData<List<SubjectGroup>> = _subjectGroupList

    // Resources
    private val _resources: MutableLiveData<List<Resource>> = MutableLiveData()
    var resources: LiveData<List<Resource>> = _resources
    // New Approach
    private val mapResourceIdsBySubjectGroup = mutableMapOf<SubjectGroup, MutableList<String>>()

    var currentSubjectGroup : SubjectGroup? = null

    private fun loadTeacher(uid: String) {
        mapResourceIdsBySubjectGroup.clear()
        FireRepository.inst.getTeacher(uid).first.observeOnce {
            val subjectGroups = mutableListOf<SubjectGroup>()
            it.teachingGroups?.map { teachingGroup ->
                val sg = SubjectGroup(teachingGroup.subjectName!!, teachingGroup.gradeLevel!!)
                subjectGroups.add(sg)
                teachingGroup.createdResources?.map { mapResourceIdsBySubjectGroup.getOrPut(sg) { mutableListOf()}.add(it) }
            }
            _subjectGroupList.postValue(subjectGroups)
        }
    }

    fun loadResource(subjectGroup: SubjectGroup) {
        val listResourceIds = mapResourceIdsBySubjectGroup[subjectGroup]
        if (listResourceIds == null) {
            _resources.postValue(mutableListOf())
        } else {
            mapResourceIdsBySubjectGroup[subjectGroup]?.toList()?.let {
                loadResourceForm(it, _resources)
            }
        }
        currentSubjectGroup = subjectGroup

    }

    private fun loadResourceForm(uids: List<String>, mutableLiveData: MutableLiveData<List<Resource>>) {
        val resourceFormList = mutableListOf<Resource>()
        if (uids.isNotEmpty()) {
            uids.map {
                FireRepository.inst.getResource(it).first.observeOnce {
                    resourceFormList.add(it)
                    if (resourceFormList.size == uids.size) mutableLiveData.postValue(resourceFormList)
                }
            }
        } else {
            mutableLiveData.postValue(mutableListOf())
        }

    }

    fun refreshData() {
        mapResourceIdsBySubjectGroup.clear()
        loadTeacher(AuthRepository.inst.getCurrentUser().uid)
    }

    fun isChipPositionTop(position: Int): Boolean {
        if (position < 4) return true;
        if (position < 8) return false;
        return position % 2 == 0;
    }
}