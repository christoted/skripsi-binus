package com.example.project_skripsi.module.teacher.main.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.model.local.*
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*
import kotlin.collections.ArrayList

class TcCalendarViewModel : ViewModel() {

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

    private val _meetingList = MutableLiveData<List<TeacherAgendaMeeting>>()
    private val _examList = MutableLiveData<List<TeacherAgendaTaskForm>>()
    private val _assignmentList = MutableLiveData<List<TeacherAgendaTaskForm>>()
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

        loadTeacher(AuthRepository.instance.getCurrentUser().uid)
        loadAnnouncements()

    }

    private fun loadTeacher(uid: String) {
        FireRepository.instance.getItem<Teacher>(uid).first.observeOnce { teacher ->
            teacher.payments?.let { _paymentList.postValue(it) }

            val classes = mutableListOf<ClassIdSubject>()
            teacher.teachingGroups?.map {
                it.teachingClasses?.map { classId ->
                    classes.add(ClassIdSubject(classId, it.subjectName!!))
                }
            }
            loadStudyClasses(classes)
        }

    }

    private fun loadStudyClasses(uids: List<ClassIdSubject>) {
        FireRepository.instance.getItems<StudyClass>(uids.map { it.studyClassId }).first.observeOnce{
            val meetings = mutableListOf<TeacherAgendaMeeting>()
            val exams = mutableListOf<ClassTaskFormId>()
            val assignments = mutableListOf<ClassTaskFormId>()

            it.mapIndexed { index, studyClass ->
                studyClass.subjects?.firstOrNull { item -> item.subjectName == uids[index].subjectName }
                    .let { subject ->
                        subject?.classMeetings?.map { meeting ->
                            meetings.add(TeacherAgendaMeeting(studyClass.name ?: "", meeting))
                        }
                        subject?.classAssignments?.map { asgId ->
                            assignments.add(ClassTaskFormId(studyClass.id!!,studyClass.name ?: "", asgId))
                        }
                        subject?.classExams?.map { examId ->
                            exams.add(ClassTaskFormId(studyClass.id!!,studyClass.name ?: "", examId))
                        }
                    }
            }

            _meetingList.postValue(meetings)
            loadTaskForms(exams, _examList)
            loadTaskForms(assignments, _assignmentList)
        }
    }


    private fun loadTaskForms(uids: List<ClassTaskFormId>, _liveData: MutableLiveData<List<TeacherAgendaTaskForm>>) {
        FireRepository.instance.getItems<TaskForm>(uids.map { it.taskFormId }).first.observeOnce {
            val taskFormList = ArrayList<TeacherAgendaTaskForm>()
            it.mapIndexed { index, taskForm ->
                taskFormList.add(TeacherAgendaTaskForm(uids[index].studyClassId, uids[index].studyClassName, taskForm))
            }
            _liveData.postValue(taskFormList)
        }
    }

    private fun loadAnnouncements() {
        FireRepository.instance.getAnnouncements().first.observeOnce { _announcementList.postValue(it) }
    }

    private fun propagateEvent(item : List<HomeSectionData>, type: Int) {
        item.map {
            when (it) {
                is TeacherAgendaMeeting -> it.classMeeting.startTime
                is TeacherAgendaTaskForm -> it.taskForm.startTime
                is Payment -> it.paymentDeadline
                is Announcement -> it.date
                else -> null
            }?.let { date ->
                propagateCalendarEvent(date, type)
                currentDataList.getOrPut(CalendarDay.from(date)) { ArrayList() }
                    .add(CalendarItem(it, type))
            }
        }
    }

    private fun propagateCalendarEvent(date : Date, type: Int) {
         currentList.getOrPut(CalendarDay.from(date)) { ArrayList() }.add(DayEvent(date, type))
    }


}