package com.example.project_skripsi.module.parent.student_detail.announcement

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.Announcement
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class PrAnnouncementViewModel : ViewModel() {

    private val _announcementList = MutableLiveData<List<Announcement>>()
    val announcementList : LiveData<List<Announcement>> = _announcementList

    init {
        loadAnnouncement()
    }

    private fun loadAnnouncement() {
        FireRepository.instance.getAllItems<Announcement>().first.observeOnce {
            Log.d("12345-", it.toString())
            _announcementList.postValue(it)
        }
    }
}