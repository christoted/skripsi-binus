package com.example.project_skripsi.module.student.main.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
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

    private val _eventList = MutableLiveData<Map<CalendarDay, List<DayEvent>>>()
    val eventList : LiveData<Map<CalendarDay, List<DayEvent>>> = _eventList


    private val currentList : MutableMap<CalendarDay, ArrayList<DayEvent>> = mutableMapOf()

    private val _meetingList = MutableLiveData<List<ClassMeeting>>()
    private val _examList = MutableLiveData<List<TaskForm>>()
    private val _assignmentList = MutableLiveData<List<TaskForm>>()
    private val _paymentList = MutableLiveData<List<Payment>>()
    private val _announcementList = MutableLiveData<List<Announcement>>()

    init {

        _meetingList.observeOnce {
            it.map { meeting -> propagateCalendarEvent(meeting.startTime, TYPE_MEETING) }
            _eventList.postValue(currentList)
        }

        _examList.observeOnce {
            it.map { taskForm -> propagateCalendarEvent(taskForm.startTime, TYPE_EXAM) }
            _eventList.postValue(currentList)
        }

        _assignmentList.observeOnce {
            it.map { taskForm -> propagateCalendarEvent(taskForm.startTime, TYPE_ASSIGNMENT) }
            _eventList.postValue(currentList)
        }

        _paymentList.observeOnce {
            it.map { payment -> propagateCalendarEvent(payment.paymentDeadline, TYPE_PAYMENT) }
            _eventList.postValue(currentList)
        }

        _announcementList.observeOnce {
            it.map { announcement -> propagateCalendarEvent(announcement.date, TYPE_ANNOUNCEMENT) }
            _eventList.postValue(currentList)
        }

        loadStudent(AuthRepository.instance.getCurrentUser().uid)
        loadAnnouncements()

    }

    private fun loadStudent(uid: String) {
        FireRepository.instance.getStudent(uid).let { response ->
            response.first.observeOnce { student ->
                student.studyClass?.let { loadStudyClass(it) }
                student.payments?.let { _paymentList.postValue(it) }
            }
        }
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.instance.getStudyClass(uid).let { response ->
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
            FireRepository.instance.getTaskForm(uid).let { response ->
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

    fun propagateCalendarEvent(date : Date?, type: Int) {
        date?.let { currentList.getOrPut(CalendarDay.from(it)) { ArrayList() }.add(DayEvent(it, type)) }
    }


}