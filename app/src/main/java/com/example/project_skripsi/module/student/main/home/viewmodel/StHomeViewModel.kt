package com.example.project_skripsi.module.student.main.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.model.local.HomeMainSection
import com.example.project_skripsi.core.model.local.TeacherAgendaTaskForm
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.Constant.Companion.SECTION_MEETING
import com.example.project_skripsi.utils.Constant.Companion.SECTION_PAYMENT
import com.example.project_skripsi.utils.Constant.Companion.SECTION_ANNOUNCEMENT
import com.example.project_skripsi.utils.Constant.Companion.SECTION_ASSIGNMENT
import com.example.project_skripsi.utils.Constant.Companion.SECTION_EXAM
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.helper.DateHelper
import com.example.project_skripsi.utils.service.notification.NotificationUtil
import com.example.project_skripsi.utils.helper.DateHelper.Companion.convertDateToCalendarDay
import com.example.project_skripsi.utils.helper.DateHelper.Companion.getCurrentDate

class StHomeViewModel : ViewModel() {

    private val _currentStudent = MutableLiveData<Student>()
    val currentStudent : LiveData<Student> = _currentStudent

    private val _profileClass = MutableLiveData<String>()
    val profileClass : LiveData<String> = _profileClass

    private val _sectionData = MutableLiveData<List<HomeMainSection>>()
    val sectionData: LiveData<List<HomeMainSection>> = _sectionData

    private val _listAttendedMeeting = MutableLiveData<List<AttendedMeeting>>()
    var attendedMeeting = _listAttendedMeeting

    private val _listHomeSectionDataClassSchedule = MutableLiveData<List<ClassMeeting>>()
    var listHomeSectionDataClassSchedule = _listHomeSectionDataClassSchedule
    private val _listHomeSectionDataExam = MutableLiveData<List<TaskForm>>()
    var listHomeSectionDataExam = _listHomeSectionDataExam
    private val _listHomeSectionDataAssignment = MutableLiveData<List<TaskForm>>()
    var listHomeSectionDataAssignment = _listHomeSectionDataAssignment
    private val _listPaymentSectionDataPayment = MutableLiveData<List<Payment>>()
    private val _listPaymentSectionDataAnnouncement = MutableLiveData<List<Announcement>>()

    init {
        loadCurrentStudent(AuthRepository.inst.getCurrentUser().uid)
        loadAnnouncements()
        initData()

    }

    private fun initData() {
        val listData = arrayListOf<HomeMainSection>()
        listData.add(HomeMainSection(SECTION_MEETING, sectionItem = emptyList()))
        listData.add(HomeMainSection(SECTION_EXAM, sectionItem = emptyList()))
        listData.add(HomeMainSection(SECTION_ASSIGNMENT, sectionItem = emptyList()))
        listData.add(HomeMainSection(SECTION_PAYMENT, sectionItem = emptyList()))
        listData.add(HomeMainSection(SECTION_ANNOUNCEMENT, sectionItem = emptyList()))

        _listHomeSectionDataClassSchedule.observeOnce {
            listData[0] = HomeMainSection(SECTION_MEETING, sectionItem = it)
            _sectionData.postValue(listData)
        }

        _listHomeSectionDataExam.observeOnce {
            listData[1] = (HomeMainSection(SECTION_EXAM, sectionItem = it))
            _sectionData.postValue(listData)
        }

        _listHomeSectionDataAssignment.observeOnce {
            listData[2] = (HomeMainSection(SECTION_ASSIGNMENT, sectionItem = it))
            _sectionData.postValue(listData)
        }

        _listPaymentSectionDataPayment.observeOnce {
            listData[3] =  HomeMainSection(SECTION_PAYMENT, sectionItem = it)
            _sectionData.postValue(listData)
        }

        _listPaymentSectionDataAnnouncement.observeOnce {
            listData[4] = HomeMainSection(SECTION_ANNOUNCEMENT, sectionItem = it)
            _sectionData.postValue(listData)
        }
        _sectionData.postValue(listData)
    }

    private fun loadCurrentStudent(uid: String) {
        FireRepository.inst.getItem<Student>(uid).first.observeOnce { student ->
            Log.d("Data Student", "$student")
            _currentStudent.postValue(student)
            student.studyClass?.let { loadStudyClass(it) }
            // TODO: Load the Payment
            student.payments?.let { list ->
                _listPaymentSectionDataPayment.postValue(
                    list.filter { convertDateToCalendarDay(it.paymentDeadline) == getCurrentDate() }
                        .sortedBy { it.paymentDeadline }
                )
            }
            student.attendedMeetings?.let {
                Log.d("987", "loadCurrentStudent: meeting ${it}")
                _listAttendedMeeting.postValue(it)
            }
        }
    }

        private fun loadStudyClass(uid: String) {
            FireRepository.inst.getItem<StudyClass>(uid).first.observeOnce { studyClass ->
                studyClass.name?.let { _profileClass.postValue(it) }
                val meetingList = mutableListOf<ClassMeeting>()
                val examList = mutableListOf<String>()
                val assignmentList = mutableListOf<String>()

                studyClass.subjects?.map { subject ->
                    with(subject) {
                        subject.classMeetings
                            ?.filter {
                                it.startTime?.let { date ->
                                    convertDateToCalendarDay(date)
                                } == getCurrentDate()
                            }
                            ?.map {
                                meetingList.add(it)
                            }
                        classExams?.let { exams -> examList.addAll(exams) }
                        classAssignments?.let { assignments -> assignmentList.addAll(assignments) }
                    }
                }
                _listHomeSectionDataClassSchedule.postValue(
                    meetingList.filter { convertDateToCalendarDay(it.startTime) == getCurrentDate() }
                        .sortedBy { it.startTime }
                )
                loadTaskForms(examList, _listHomeSectionDataExam)
                loadTaskForms(assignmentList, _listHomeSectionDataAssignment)
            }
        }

        private fun loadAnnouncements() {
            FireRepository.inst.getAllItems<Announcement>().first.observeOnce { list ->
                _listPaymentSectionDataAnnouncement.postValue(
                    list.filter { convertDateToCalendarDay(it.date) == getCurrentDate() }
                        .sortedBy { it.date }
                )
            }
        }

        private fun loadTaskForms(
            uids: List<String>,
            _taskFormList: MutableLiveData<List<TaskForm>>
        ) {
            FireRepository.inst.getItems<TaskForm>(uids).first.observeOnce { list ->
                _taskFormList.postValue(
                    list.filter { convertDateToCalendarDay(it.startTime) == getCurrentDate() }
                        .sortedBy { it.startTime }
                )
            }
        }
    }

