package com.example.project_skripsi.module.parent.student_detail.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.model.local.*
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.module.student.main.calendar.StCalendarViewModel
import com.example.project_skripsi.utils.custom.comparator.CalendarComparator
import com.example.project_skripsi.utils.generic.GenericExtension.Companion.compareTo
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.helper.DateHelper
import com.example.project_skripsi.utils.helper.DateHelper.Companion.convertDateToCalendarDay
import com.example.project_skripsi.utils.helper.DateHelper.Companion.getCurrentDate
import com.prolificinteractive.materialcalendarview.CalendarDay

class PrCalendarViewModel : ViewModel() {

    companion object {
        const val TYPE_MEETING = 0
        const val TYPE_EXAM = 1
        const val TYPE_ASSIGNMENT = 2
        const val TYPE_PAYMENT = 3
        const val TYPE_ANNOUNCEMENT = 4
    }

    var currentSelectedDate: CalendarDay = getCurrentDate()
    private val _eventList = MutableLiveData<Map<CalendarDay, List<DayEvent>>>()
    val eventList : LiveData<Map<CalendarDay, List<DayEvent>>> = _eventList

    private val currentList : MutableMap<CalendarDay, MutableList<DayEvent>> = mutableMapOf()
    val currentDataList : MutableMap<CalendarDay, MutableList<CalendarItem>> = mutableMapOf()

    fun setStudent(studentId: String) {
        loadStudent(studentId)
        loadAnnouncements()
    }

    private fun loadStudent(uid: String) {
        FireRepository.inst.getItem<Student>(uid).first.observeOnce { student ->
            student.studyClass?.let { loadStudyClass(it) }
            student.payments?.let { propagateEvent(it, TYPE_PAYMENT) }
        }
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.inst.getItem<StudyClass>(uid).let { response ->
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
        FireRepository.inst.getItems<TaskForm>(uids).first.observeOnce { propagateEvent(it, type) }
    }

    private fun loadAnnouncements() {
        FireRepository.inst.getAllItems<Announcement>().first.observeOnce { list ->
            propagateEvent(
                list.filter { convertDateToCalendarDay(it.date) <= getCurrentDate() },
                TYPE_ANNOUNCEMENT
            )
        }
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
                    CalendarItem(it, date, type)
                )
            }
        }
        currentList.map { it.value.sortWith(CalendarComparator.comp) }
        currentDataList.map { it.value.sortWith(CalendarComparator.compData) }

        _eventList.postValue(currentList)
    }
}
