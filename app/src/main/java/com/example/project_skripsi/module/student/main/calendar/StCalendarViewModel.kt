package com.example.project_skripsi.module.student.main.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.model.local.CalendarItem
import com.example.project_skripsi.core.model.local.DayEvent
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
    val currentDataList : MutableMap<CalendarDay, ArrayList<CalendarItem>> = mutableMapOf()

    private val _meetingList = MutableLiveData<List<ClassMeeting>>()
    private val _examList = MutableLiveData<List<TaskForm>>()
    private val _assignmentList = MutableLiveData<List<TaskForm>>()
    private val _paymentList = MutableLiveData<List<Payment>>()
    private val _announcementList = MutableLiveData<List<Announcement>>()

    init {

        _meetingList.observeOnce {
            it.map { meeting ->
                meeting.startTime?.let { date ->
                    propagateCalendarEvent(date, TYPE_MEETING)
                    currentDataList.getOrPut(CalendarDay.from(date)) { ArrayList() }
                        .add(CalendarItem(meeting, TYPE_MEETING))
                }
            }
            _eventList.postValue(currentList)
        }

        _examList.observeOnce {
            it.map { taskForm ->
                taskForm.startTime?.let { date ->
                    propagateCalendarEvent(date, TYPE_EXAM)
                    currentDataList.getOrPut(CalendarDay.from(date)) { ArrayList() }
                        .add(CalendarItem(taskForm, TYPE_EXAM))
                }
            }
            _eventList.postValue(currentList)
        }

        _assignmentList.observeOnce {
            it.map { taskForm ->
                taskForm.startTime?.let { date ->
                    propagateCalendarEvent(date, TYPE_ASSIGNMENT)
                    currentDataList.getOrPut(CalendarDay.from(date)) { ArrayList() }
                        .add(CalendarItem(taskForm, TYPE_ASSIGNMENT))
                }
            }
            _eventList.postValue(currentList)
        }

        _paymentList.observeOnce {
            it.map { payment ->
                payment.paymentDeadline?.let { date ->
                    propagateCalendarEvent(date, TYPE_PAYMENT)
                    currentDataList.getOrPut(CalendarDay.from(date)) { ArrayList() }
                        .add(CalendarItem(payment, TYPE_PAYMENT))
                }
            }
            _eventList.postValue(currentList)
        }

        _announcementList.observeOnce {
            it.map { announcement ->
                announcement.date?.let { date ->
                    propagateCalendarEvent(date, TYPE_ANNOUNCEMENT)
                    currentDataList.getOrPut(CalendarDay.from(date)) { ArrayList() }
                        .add(CalendarItem(announcement, TYPE_ANNOUNCEMENT))
                }
            }
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

    private fun propagateCalendarEvent(date : Date, type: Int) {
         currentList.getOrPut(CalendarDay.from(date)) { ArrayList() }.add(DayEvent(date, type))
    }


}