package com.example.project_skripsi.module.teacher.main.calendar

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.model.local.*
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.custom.comparator.CalendarComparator
import com.example.project_skripsi.utils.generic.GenericExtension.Companion.compareTo
import com.example.project_skripsi.utils.generic.GenericLinkHandler
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.helper.DateHelper
import com.prolificinteractive.materialcalendarview.CalendarDay

class TcCalendarViewModel : ViewModel() {

    companion object {
        const val TYPE_MEETING = 0
        const val TYPE_EXAM = 1
        const val TYPE_ASSIGNMENT = 2
        const val TYPE_PAYMENT = 3
        const val TYPE_ANNOUNCEMENT = 4
    }

    var currentSelectedDate: CalendarDay = DateHelper.getCurrentDate()

    private val _eventList = MutableLiveData<Map<CalendarDay, List<DayEvent>>>()
    val eventList: LiveData<Map<CalendarDay, List<DayEvent>>> = _eventList

    private val currentList = mutableMapOf<CalendarDay, MutableList<DayEvent>>()
    val currentDataList = mutableMapOf<CalendarDay, MutableList<CalendarItem>>()

    lateinit var curTeacher: Teacher

    init {
        loadTeacher(AuthRepository.inst.getCurrentUser().uid)
        loadAnnouncements()
    }

    private fun loadTeacher(uid: String) {
        FireRepository.inst.getItem<Teacher>(uid).first.observeOnce { teacher ->
            curTeacher = teacher
            teacher.payments?.let { propagateEvent(it, TYPE_PAYMENT) }

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
        FireRepository.inst.getItems<StudyClass>(uids.map { it.studyClassId }).first.observeOnce { list ->

            val meetings = mutableListOf<TeacherAgendaMeeting>()
            val exams = mutableListOf<ClassTaskFormId>()
            val assignments = mutableListOf<ClassTaskFormId>()

            uids.map { classIdSubject ->
                list.firstOrNull { it.id == classIdSubject.studyClassId }?.let { studyClass ->
                    studyClass.subjects?.firstOrNull { item -> item.subjectName == classIdSubject.subjectName }
                        .let { subject ->
                            subject?.classMeetings?.map { meeting ->
                                meetings.add(TeacherAgendaMeeting(studyClass.name ?: "", meeting))
                            }
                            subject?.classAssignments?.map { asgId ->
                                assignments.add(
                                    ClassTaskFormId(
                                        studyClass.id!!,
                                        studyClass.name ?: "",
                                        asgId
                                    )
                                )
                            }
                            subject?.classExams?.map { examId ->
                                exams.add(
                                    ClassTaskFormId(
                                        studyClass.id!!,
                                        studyClass.name ?: "",
                                        examId
                                    )
                                )
                            }
                        }
                }
            }
            propagateEvent(meetings, TYPE_MEETING)
            loadTaskForms(exams, TYPE_EXAM)
            loadTaskForms(assignments, TYPE_ASSIGNMENT)
        }
    }

    private fun loadTaskForms(uids: List<ClassTaskFormId>, type: Int) {
        FireRepository.inst.getItems<TaskForm>(uids.map { it.taskFormId }).first.observeOnce { list ->
            propagateEvent(uids.mapNotNull { classTaskFormId ->
                list.firstOrNull { it.id == classTaskFormId.taskFormId }?.let {
                    TeacherAgendaTaskForm(
                        classTaskFormId.studyClassId,
                        classTaskFormId.studyClassName,
                        it
                    )
                }
            }, type)
        }
    }

    private fun loadAnnouncements() {
        FireRepository.inst.getAllItems<Announcement>().first.observeOnce { list ->
            propagateEvent(
                list.filter { DateHelper.convertDateToCalendarDay(it.date) <= DateHelper.getCurrentDate() },
                TYPE_ANNOUNCEMENT
            )
        }
    }

    private fun propagateEvent(item: List<HomeSectionData>, type: Int) {
        item.map {
            when (it) {
                is TeacherAgendaMeeting -> it.classMeeting.startTime
                is TeacherAgendaTaskForm -> it.taskForm.startTime
                is Payment -> it.paymentDeadline
                is Announcement -> it.date
                else -> null
            }?.let { date ->
                currentList.getOrPut(CalendarDay.from(date)) { ArrayList() }
                    .add(DayEvent(date, type))
                currentDataList.getOrPut(CalendarDay.from(date)) { ArrayList() }
                    .add(CalendarItem(it, date, type))
            }
        }

        currentList.map { it.value.sortWith(CalendarComparator.comp) }
        currentDataList.map { it.value.sortWith(CalendarComparator.compData) }

        _eventList.postValue(currentList)
    }

    fun openLink(context: Context, uid: String) {
        FireRepository.inst.getItem<Resource>(uid).first.observeOnce { resource ->
            resource.link?.let { GenericLinkHandler.goToLink(context, it) }
        }
    }

}