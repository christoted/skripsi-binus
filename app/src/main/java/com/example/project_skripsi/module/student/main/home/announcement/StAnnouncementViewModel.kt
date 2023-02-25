package com.example.project_skripsi.module.student.main.home.announcement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.Announcement
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericExtension.Companion.compareTo
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.helper.DateHelper

class StAnnouncementViewModel : ViewModel() {

    private val _announcementList = MutableLiveData<List<Announcement>>()
    val announcementList: LiveData<List<Announcement>> = _announcementList

    init {
        loadAnnouncement()
    }

    private fun loadAnnouncement() {
        FireRepository.inst.getAllItems<Announcement>().first.observeOnce { list ->
            _announcementList.postValue(
                list.filter { DateHelper.convertDateToCalendarDay(it.date) <= DateHelper.getCurrentDate() }
                    .sortedByDescending { it.date }
            )
        }
    }
}