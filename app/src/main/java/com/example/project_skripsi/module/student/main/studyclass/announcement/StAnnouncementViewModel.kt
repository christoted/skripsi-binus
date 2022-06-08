package com.example.project_skripsi.module.student.main.studyclass.announcement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.Announcement
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class StAnnouncementViewModel : ViewModel() {

    private val _announcementList = MutableLiveData<List<Announcement>>()
    val announcementList : LiveData<List<Announcement>> = _announcementList

    init {
        loadAnnouncement()
    }

    private fun loadAnnouncement() {
        FireRepository.inst.getAllItems<Announcement>().first.observeOnce {
            _announcementList.postValue(it)
        }
    }
}