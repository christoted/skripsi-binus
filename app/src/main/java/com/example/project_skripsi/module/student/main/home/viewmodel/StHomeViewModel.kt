package com.example.project_skripsi.module.student.main.home.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.model.local.HomeMainSection
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.Constant.Companion.SECTION_ANNOUNCEMENT
import com.example.project_skripsi.utils.Constant.Companion.SECTION_ASSIGNMENT
import com.example.project_skripsi.utils.Constant.Companion.SECTION_EXAM
import com.example.project_skripsi.utils.Constant.Companion.SECTION_MEETING
import com.example.project_skripsi.utils.Constant.Companion.SECTION_PAYMENT
import com.example.project_skripsi.utils.generic.GenericLinkHandler
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.generic.HandledEvent
import com.example.project_skripsi.utils.helper.DateHelper.Companion.convertDateToCalendarDay
import com.example.project_skripsi.utils.helper.DateHelper.Companion.getCurrentDate
import com.example.project_skripsi.utils.helper.DateHelper.Companion.getCurrentTime
import com.example.project_skripsi.utils.helper.DateHelper.Companion.getDateWithDayOffset

class StHomeViewModel : ViewModel() {

    private val _currentStudent = MutableLiveData<Student>()
    val currentStudent: LiveData<Student> = _currentStudent

    private val _profileClass = MutableLiveData<String>()
    val profileClass: LiveData<String> = _profileClass

    private val _sectionData = MutableLiveData<List<HomeMainSection>>()
    val sectionData: LiveData<List<HomeMainSection>> = _sectionData


    private val _listHomeSectionDataClassScheduleOneWeek = MutableLiveData<List<ClassMeeting>>()
    val listHomeSectionDataClassScheduleOneWeek: LiveData<List<ClassMeeting>> =
        _listHomeSectionDataClassScheduleOneWeek

    private val _listHomeSectionDataExamOneWeek = MutableLiveData<List<TaskForm>>()
    val listHomeSectionDataExamOneWeek: LiveData<List<TaskForm>> = _listHomeSectionDataExamOneWeek

    private val _listHomeSectionDataAssignmentOneWeek = MutableLiveData<List<TaskForm>>()
    var listHomeSectionDataAssignmentOneWeek: LiveData<List<TaskForm>> =
        _listHomeSectionDataAssignmentOneWeek

    private val _listHomeSectionDataClassSchedule = MutableLiveData<List<ClassMeeting>>()
    private val _listHomeSectionDataExam = MutableLiveData<List<TaskForm>>()
    private val _listHomeSectionDataAssignment = MutableLiveData<List<TaskForm>>()
    private val _listPaymentSectionDataPayment = MutableLiveData<List<Payment>>()
    private val _listPaymentSectionDataAnnouncement = MutableLiveData<List<Announcement>>()

    private val _incompleteResource = MutableLiveData<HandledEvent<Resource>>()
    val incompleteResource: LiveData<HandledEvent<Resource>> = _incompleteResource

    private val _isDataFetchFinished = MutableLiveData<Boolean>()
    val isFetchDataCompleted: LiveData<Boolean> = _isDataFetchFinished

    private var counterData = 0

    init {
        initData()
        refreshData()
    }

    fun refreshData() {
        counterData = 0
        _isDataFetchFinished.postValue(false)
        loadCurrentStudent(AuthRepository.inst.getCurrentUser().uid)
        loadAnnouncements()
    }

    private fun initData() {
        val listData = mutableListOf(
            HomeMainSection(SECTION_MEETING, sectionItem = emptyList()),
            HomeMainSection(SECTION_EXAM, sectionItem = emptyList()),
            HomeMainSection(SECTION_ASSIGNMENT, sectionItem = emptyList()),
            HomeMainSection(SECTION_PAYMENT, sectionItem = emptyList()),
            HomeMainSection(SECTION_ANNOUNCEMENT, sectionItem = emptyList())
        )

        _listHomeSectionDataClassSchedule.observeForever {
            listData[0] = HomeMainSection(SECTION_MEETING, sectionItem = it)
            _sectionData.postValue(listData)
            if (++counterData == 5) _isDataFetchFinished.postValue(true)
        }

        _listHomeSectionDataExam.observeForever {
            listData[1] = (HomeMainSection(SECTION_EXAM, sectionItem = it))
            _sectionData.postValue(listData)
            if (++counterData == 5) _isDataFetchFinished.postValue(true)
        }

        _listHomeSectionDataAssignment.observeForever {
            listData[2] = (HomeMainSection(SECTION_ASSIGNMENT, sectionItem = it))
            _sectionData.postValue(listData)
            if (++counterData == 5) _isDataFetchFinished.postValue(true)
        }

        _listPaymentSectionDataPayment.observeForever {
            listData[3] = HomeMainSection(SECTION_PAYMENT, sectionItem = it)
            _sectionData.postValue(listData)
            if (++counterData == 5) _isDataFetchFinished.postValue(true)
        }

        _listPaymentSectionDataAnnouncement.observeForever {
            listData[4] = HomeMainSection(SECTION_ANNOUNCEMENT, sectionItem = it)
            _sectionData.postValue(listData)
            if (++counterData == 5) _isDataFetchFinished.postValue(true)
        }
        _sectionData.postValue(listData)
    }

    private fun loadCurrentStudent(uid: String) {
        FireRepository.inst.getItem<Student>(uid).first.observeOnce { student ->
            _currentStudent.postValue(student)
            student.studyClass?.let { loadStudyClass(it) }
            // TODO: Load the Payment
            student.payments?.let { list ->
                _listPaymentSectionDataPayment.postValue(
                    list.filter { convertDateToCalendarDay(it.paymentDeadline) == getCurrentDate() }
                        .sortedBy { it.paymentDeadline }
                )
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
                    subject.classMeetings?.let { meetingList.addAll(it) }
                    classExams?.let { exams -> examList.addAll(exams) }
                    classAssignments?.let { assignments -> assignmentList.addAll(assignments) }
                }
            }
            _listHomeSectionDataClassSchedule.postValue(
                meetingList.filter { convertDateToCalendarDay(it.startTime) == getCurrentDate() }
                    .sortedBy { it.startTime }
            )
            _listHomeSectionDataClassScheduleOneWeek.postValue(
                meetingList.filter { it.startTime!! < getCurrentTime().getDateWithDayOffset(7) }
            )
            loadTaskForms(examList, _listHomeSectionDataExam, _listHomeSectionDataExamOneWeek)
            loadTaskForms(
                assignmentList,
                _listHomeSectionDataAssignment,
                _listHomeSectionDataAssignmentOneWeek
            )
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
        _taskFormList: MutableLiveData<List<TaskForm>>,
        _taskFormListOneWeek: MutableLiveData<List<TaskForm>>
    ) {
        FireRepository.inst.getItems<TaskForm>(uids).first.observeOnce { list ->
            _taskFormList.postValue(
                list.filter { convertDateToCalendarDay(it.startTime) == getCurrentDate() }
                    .sortedBy { it.startTime }
            )
            _taskFormListOneWeek.postValue(
                list.filter { it.startTime!! < getCurrentTime().getDateWithDayOffset(7) }
            )
        }
    }

    fun openResource(context: Context, uid: String) {
        FireRepository.inst.getItem<Resource>(uid).first.observeOnce { resource ->

            var incompleteId: String? = null
            resource.prerequisites?.map {
                currentStudent.value?.completedResources?.let { list ->
                    if (!list.contains(it)) {
                        incompleteId = it
                        return@map
                    }
                }
            }

            incompleteId?.let { id ->
                FireRepository.inst.getItem<Resource>(id).first.observeOnce {
                    _incompleteResource.postValue(HandledEvent(it))
                }
            } ?: kotlin.run {
                resource.link?.let { GenericLinkHandler.goToLink(context, it) }
                resource.id?.let {
                    val curStudent = currentStudent.value
                    if (curStudent?.completedResources?.contains(it) == false) {
                        curStudent.completedResources?.add(it)
                        FireRepository.inst.alterItems(listOf(curStudent))
                    }
                }
            }
        }
    }
}

