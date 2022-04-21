package com.example.project_skripsi.module.parent.student_detail.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.model.local.*
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*

class PrCalendarViewModel : ViewModel() {

    companion object {
        const val TYPE_MEETING = 0
        const val TYPE_EXAM = 1
        const val TYPE_ASSIGNMENT = 2
        const val TYPE_PAYMENT = 3
        const val TYPE_ANNOUNCEMENT = 4
        const val TYPE_MORE = 10
    }

    private val _eventList = MutableLiveData<Map<CalendarDay, List<DayEvent>>>()
    val eventList : LiveData<Map<CalendarDay, List<DayEvent>>> = _eventList

    private val currentList : MutableMap<CalendarDay, MutableList<DayEvent>> = mutableMapOf()
    val currentDataList : MutableMap<CalendarDay, MutableList<CalendarItem>> = mutableMapOf()

    fun setStudent(studentId: String) {
        loadStudent(studentId)
        loadAnnouncements()
    }

    private fun loadStudent(uid: String) {
        FireRepository.instance.getItem<Student>(uid).first.observeOnce { student ->
            student.studyClass?.let { loadStudyClass(it) }
            student.payments?.let { propagateEvent(it, TYPE_PAYMENT) }
        }
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.instance.getItem<StudyClass>(uid).let { response ->
            response.first.observeOnce { studyClass ->
                val meetings = mutableListOf<ClassMeeting>()
                val examIds = mutableListOf<String>()
                val assignmentIds = mutableListOf<String>()
                studyClass.subjects?.map { subject ->
                    with(subject) {
                        classMeetings?.let { meetings.addAll(it) }
                        classExams?.let { examIds.addAll(it) }
                        classAssignments?.let { assignmentIds.addAll(it) }
                    }
                }
                propagateEvent(meetings, TYPE_MEETING)
                loadTaskForms(examIds, TYPE_EXAM)
                loadTaskForms(assignmentIds, TYPE_ASSIGNMENT)
            }
        }
    }

    private fun loadTaskForms(uids: List<String>, type: Int) {
        FireRepository.instance.getItems<TaskForm>(uids).first.observeOnce { propagateEvent(it, type) }
    }

    private fun loadAnnouncements() {
        FireRepository.instance.getAllItems<Announcement>().first.observeOnce { propagateEvent(it, TYPE_ANNOUNCEMENT) }
    }

    private fun propagateEvent(item : List<HomeSectionData>, type: Int) {
        item.map {
            when (it) {
                is ClassMeeting -> it.startTime
                is TaskForm -> it.startTime
                is Payment -> it.paymentDeadline
                is Announcement -> it.date
                else -> null
            }?.let { date ->
                // push to calendar view
                currentList.getOrPut(CalendarDay.from(date)) { mutableListOf() }.add(DayEvent(date, type))

                // push to recycler view
                currentDataList.getOrPut(CalendarDay.from(date)) { mutableListOf() }.add(
                    CalendarItem(it, type)
                )
            }
        }
        _eventList.postValue(currentList)
    }
}
