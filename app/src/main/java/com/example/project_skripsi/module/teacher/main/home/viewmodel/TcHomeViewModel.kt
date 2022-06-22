package com.example.project_skripsi.module.teacher.main.home.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.model.local.*
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.Constant.Companion.SECTION_ANNOUNCEMENT
import com.example.project_skripsi.utils.Constant.Companion.SECTION_ASSIGNMENT
import com.example.project_skripsi.utils.Constant.Companion.SECTION_EXAM
import com.example.project_skripsi.utils.Constant.Companion.SECTION_MEETING
import com.example.project_skripsi.utils.Constant.Companion.SECTION_PAYMENT
import com.example.project_skripsi.utils.generic.GenericLinkHandler
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.helper.DateHelper
import com.example.project_skripsi.utils.helper.DateHelper.Companion.convertDateToCalendarDay
import com.example.project_skripsi.utils.helper.DateHelper.Companion.getCurrentDate

class TcHomeViewModel: ViewModel() {

    private val _teacherData: MutableLiveData<Teacher> = MutableLiveData()
    val teacherData: LiveData<Teacher> = _teacherData

    private val _studyClass: MutableLiveData<StudyClass> = MutableLiveData()
    val studyClass: LiveData<StudyClass> = _studyClass

    private val _listMeeting: MutableLiveData<List<TeacherAgendaMeeting>> = MutableLiveData()
    val listMeeting: LiveData<List<TeacherAgendaMeeting>> = _listMeeting

    private val _announcements: MutableLiveData<List<Announcement>> = MutableLiveData()

    private val _examList: MutableLiveData<List<TeacherAgendaTaskForm>> = MutableLiveData()
    val examList: LiveData<List<TeacherAgendaTaskForm>> = _examList

    private val _assignmentList: MutableLiveData<List<TeacherAgendaTaskForm>> = MutableLiveData()
    val assignmentList: LiveData<List<TeacherAgendaTaskForm>> = _assignmentList

    private val _paymentList = MutableLiveData<List<Payment>>()

    private val _sectionData = MutableLiveData<List<HomeMainSection>>()
    val sectionData: LiveData<List<HomeMainSection>> = _sectionData

    private val _isDataFetchFinished = MutableLiveData<Boolean>()
    val isFetchDataCompleted : LiveData<Boolean> = _isDataFetchFinished

    var counterData = 0

    init {
        initData()
        refreshData()
    }

    fun refreshData() {
        counterData = 0
        _isDataFetchFinished.postValue(false)

        loadTeacher(AuthRepository.inst.getCurrentUser().uid)
        loadAnnouncement()
    }

    private fun initData() {
        val listData = mutableListOf(
            HomeMainSection(SECTION_MEETING, emptyList()),
            HomeMainSection(SECTION_EXAM, emptyList()),
            HomeMainSection(SECTION_ASSIGNMENT, emptyList()),
            HomeMainSection(SECTION_PAYMENT, emptyList()),
            HomeMainSection(SECTION_ANNOUNCEMENT, emptyList())
        )

        _listMeeting.observeForever {
            listData[0] = HomeMainSection(SECTION_MEETING, it)
            _sectionData.postValue(listData)
            if (++counterData == 5) _isDataFetchFinished.postValue(true)
        }
        _examList.observeForever {
            listData[1] = HomeMainSection(SECTION_EXAM, it)
            _sectionData.postValue(listData)
            if (++counterData == 5) _isDataFetchFinished.postValue(true)
        }
        _assignmentList.observeForever {
            listData[2] = HomeMainSection(SECTION_ASSIGNMENT, it)
            _sectionData.postValue(listData)
            if (++counterData == 5) _isDataFetchFinished.postValue(true)
        }
        _paymentList.observeForever {
            listData[3] = HomeMainSection(SECTION_PAYMENT, it)
            _sectionData.postValue(listData)
            if (++counterData == 5) _isDataFetchFinished.postValue(true)
        }
        _announcements.observeForever {
            listData[4] = HomeMainSection(SECTION_ANNOUNCEMENT, it)
            _sectionData.postValue(listData)
            if (++counterData == 5) _isDataFetchFinished.postValue(true)
        }
        _sectionData.postValue(listData)
    }

    private fun loadTeacher(uid: String) {
        FireRepository.inst.getItem<Teacher>(uid).first.observeOnce {
            _teacherData.postValue(it)

            it.payments?.let { payments ->
                _paymentList.postValue(
                    payments
                        .filter { payment -> convertDateToCalendarDay(payment.paymentDeadline) == getCurrentDate() }
                        .sortedBy { payment -> payment.paymentDeadline }
                )
            }

            val classes = mutableListOf<ClassIdSubject>()
            it.teachingGroups?.map { teachingGroup ->
                teachingGroup.teachingClasses?.map { classId ->
                    classes.add(ClassIdSubject(classId, teachingGroup.subjectName!!))
                }
            }

            loadStudyClasses(classes)
        }
    }
    // Load Study Classes
    private fun loadStudyClasses(uids: List<ClassIdSubject>) {
        FireRepository.inst.getItems<StudyClass>(uids.map {
            it.studyClassId
        }).first.observeOnce { list ->
            val meetings = mutableListOf<TeacherAgendaMeeting>()
            val exams = mutableListOf<ClassTaskFormId>()
            val assignments = mutableListOf<ClassTaskFormId>()

            uids.map {  classIdSubject ->
                list.firstOrNull { it.id == classIdSubject.studyClassId}?.let { studyClass ->
                    studyClass.subjects?.firstOrNull { item -> item.subjectName == classIdSubject.subjectName }
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
            }
            _listMeeting.postValue(
                meetings
                    .filter { convertDateToCalendarDay(it.classMeeting.startTime) == getCurrentDate() }
                    .sortedBy { it.classMeeting.startTime }
            )
            loadTaskForm(exams, _examList)
            loadTaskForm(assignments, _assignmentList)
        }
    }

    private fun loadTaskForm(uids: List<ClassTaskFormId>, mutableLiveData: MutableLiveData<List<TeacherAgendaTaskForm>>) {
        FireRepository.inst.getItems<TaskForm>(uids.map { it.taskFormId }).first.observeOnce { list ->
            val taskFormList =
                uids.mapNotNull { classTaskFormId ->
                    list.firstOrNull {it.id == classTaskFormId.taskFormId}?.let { taskForm ->
                        TeacherAgendaTaskForm(classTaskFormId.studyClassId, classTaskFormId.studyClassName, taskForm)
                    }
                }

            mutableLiveData.postValue(
                taskFormList
                    .filter { convertDateToCalendarDay(it.taskForm.startTime) == getCurrentDate() }
                    .sortedBy { it.taskForm.startTime }
            )
        }
    }

    // Load Announcement
    private fun loadAnnouncement(){
        FireRepository.inst.getAllItems<Announcement>().first.observeOnce { list ->
            _announcements.postValue(
                list.filter { convertDateToCalendarDay(it.date) == getCurrentDate() }
                    .sortedBy { it.date }
            )
        }
    }

    fun openLink(context: Context, uid: String) {
        FireRepository.inst.getItem<Resource>(uid).first.observeOnce { resource ->
            resource.link?.let { GenericLinkHandler.goToLink(context, it) }
        }
    }
}
