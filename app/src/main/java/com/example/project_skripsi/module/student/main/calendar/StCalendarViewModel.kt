package com.example.project_skripsi.module.student.main.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.model.local.*
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.module.teacher.main.calendar.TcCalendarViewModel
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.helper.DateHelper
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*
import kotlin.collections.ArrayList

class StCalendarViewModel : ViewModel() {

    companion object {
        const val TYPE_MEETING = 0
        const val TYPE_EXAM = 1
        const val TYPE_ASSIGNMENT = 2
        const val TYPE_PAYMENT = 3
        const val TYPE_ANNOUNCEMENT = 4
        const val TYPE_MORE = 10
    }

    var currentSelectedDate: CalendarDay = DateHelper.getCurrentDateNow()
    private val _eventList = MutableLiveData<Map<CalendarDay, List<DayEvent>>>()
    val eventList : LiveData<Map<CalendarDay, List<DayEvent>>> = _eventList



    private val currentList : MutableMap<CalendarDay, MutableList<DayEvent>> = mutableMapOf()
    val currentDataList : MutableMap<CalendarDay, MutableList<CalendarItem>> = mutableMapOf()

    private val _meetingList = MutableLiveData<List<ClassMeeting>>()
    private val _examList = MutableLiveData<List<TaskForm>>()
    private val _assignmentList = MutableLiveData<List<TaskForm>>()
    private val _paymentList = MutableLiveData<List<Payment>>()
    private val _announcementList = MutableLiveData<List<Announcement>>()

    init {

        _meetingList.observeOnce {
            propagateEvent(it, TYPE_MEETING)
            _eventList.postValue(currentList)
        }

        _examList.observeOnce {
            propagateEvent(it, TYPE_EXAM)
            _eventList.postValue(currentList)
        }

        _assignmentList.observeOnce {
            propagateEvent(it, TYPE_ASSIGNMENT)
            _eventList.postValue(currentList)
        }

        _paymentList.observeOnce {
            propagateEvent(it, TYPE_PAYMENT)
            _eventList.postValue(currentList)
        }

        _announcementList.observeOnce {
            propagateEvent(it, TYPE_ANNOUNCEMENT)
            _eventList.postValue(currentList)
        }

        loadStudent(AuthRepository.instance.getCurrentUser().uid)
        loadAnnouncements()

    }

    private fun loadStudent(uid: String) {
        FireRepository.instance.getItem<Student>(uid).let { response ->
            response.first.observeOnce { student ->
                student.studyClass?.let { loadStudyClass(it) }
                student.payments?.let { _paymentList.postValue(it) }
            }
        }
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.instance.getItem<StudyClass>(uid).let { response ->
            response.first.observeOnce { studyClass ->
                val meetings = ArrayList<ClassMeeting>()
                val examIds = ArrayList<String>()
                val assignmentIds = ArrayList<String>()
                studyClass.subjects?.map { subject ->
                    with(subject) {
                        classMeetings?.let { meetings.addAll(it) }
                        classExams?.let { examIds.addAll(it) }
                        classAssignments?.let { assignmentIds.addAll(it) }
                    }
                }
                _meetingList.postValue(meetings)
                loadTaskForms(examIds, _examList)
                loadTaskForms(assignmentIds, _assignmentList)
            }
        }
    }

    private fun loadTaskForms(uids: List<String>, _taskFormList: MutableLiveData<List<TaskForm>>) {
        val taskFormList = ArrayList<TaskForm>()
        uids.map { uid ->
            FireRepository.instance.getItem<TaskForm>(uid).let { response ->
                response.first.observeOnce { taskForm ->
                    taskFormList.add(taskForm)
                    if (taskFormList.size == uids.size)
                        _taskFormList.postValue(taskFormList.toList())
                }
            }
        }
    }

    private fun loadAnnouncements() {
        FireRepository.instance.getAnnouncements().let { response ->
            response.first.observeOnce { _announcementList.postValue(it) }
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
                propagateCalendarEvent(date, type)
                currentDataList.getOrPut(CalendarDay.from(date)) { mutableListOf() }.add(
                    CalendarItem(it, type)
                )
            }
        }
    }

    private fun propagateCalendarEvent(date : Date, type: Int) {
         currentList.getOrPut(CalendarDay.from(date)) { ArrayList() }.add(DayEvent(date, type))
    }


}