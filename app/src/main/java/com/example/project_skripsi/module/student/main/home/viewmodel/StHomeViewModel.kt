package com.example.project_skripsi.module.student.main.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.model.local.HomeMainSection
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.Constant.Companion.SECTION_MEETING
import com.example.project_skripsi.utils.Constant.Companion.SECTION_PAYMENT
import com.example.project_skripsi.utils.Constant.Companion.SECTION_ANNOUNCEMENT
import com.example.project_skripsi.utils.Constant.Companion.SECTION_ASSIGNMENT
import com.example.project_skripsi.utils.Constant.Companion.SECTION_EXAM
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.helper.DateHelper

class StHomeViewModel : ViewModel() {

    private val _profileName = MutableLiveData<String>()
    val profileName : LiveData<String> = _profileName

    private val _profileClass = MutableLiveData<String>()
    val profileClass : LiveData<String> = _profileClass

    private val _sectionData = MutableLiveData<List<HomeMainSection>>()
    val sectionData: LiveData<List<HomeMainSection>> = _sectionData

    private val _listHomeSectionDataClassSchedule = MutableLiveData<List<ClassMeeting>>()
    private val _listHomeSectionDataExam = MutableLiveData<List<TaskForm>>()
    private val _listHomeSectionDataAssignment = MutableLiveData<List<TaskForm>>()
    private val _listPaymentSectionDataPayment = MutableLiveData<List<Payment>>()
    private val _listPaymentSectionDataAnnouncement = MutableLiveData<List<Announcement>>()
    
    init {
        _profileName.value = "Luis Anthonie Alkins (21)"
        _profileClass.value = "XII - IPA - 1"
        loadCurrentStudent(AuthRepository.instance.getCurrentUser().uid)
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
            listData[1] = (HomeMainSection(SECTION_EXAM, sectionItem = it.filter { it.startTime == DateHelper.getCurrentDate() }))
            _sectionData.postValue(listData)
        }

        _listHomeSectionDataAssignment.observeOnce {
            listData[2] = (HomeMainSection(SECTION_ASSIGNMENT, sectionItem = it.filter { it.startTime == DateHelper.getCurrentDate() }))
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
        FireRepository.inst.getStudent(uid).let {
            response ->
            response.first.observeOnce {
                student ->
                Log.d("Data Student", "${student}")
                student.name?.let { _profileName.postValue(it) }
                // TODO: Take the Class id
                student.studyClass?.let { loadStudyClass(it) }
                // TODO: Load the Payment
                student.payments?.let { _listPaymentSectionDataPayment.postValue(it) }
            }
        }
    }

    private fun loadStudyClass(uid: String) {

        FireRepository.inst.getStudyClass(uid).let {
            response ->
            response.first.observeOnce { studyClass ->

                studyClass.name?.let { _profileClass.postValue(it) }
                val meetingList = mutableListOf<ClassMeeting>()
                val examList = mutableListOf<String>()
                val assignmentList = mutableListOf<String>()
                studyClass.subjects?.map { subject ->
                    with(subject) {
                        subject.classMeetings?.let { meetingList.addAll(it) }
                        // TODO: Take the class exams
                        classExams?.let { exams -> examList.addAll(exams) }
                        // TODO: Take the class assignments
                        classAssignments?.let { assignments -> assignmentList.addAll(assignments) }
                    }
                }
                _listHomeSectionDataClassSchedule.postValue(meetingList)
                loadTaskForms(examList, _listHomeSectionDataExam)
                loadTaskForms(assignmentList, _listHomeSectionDataAssignment)
            }
        }
    }

    private fun loadAnnouncements() {
        FireRepository.inst.getAnnouncements().let {
            response ->
            response.first.observeOnce {
                // TODO: Load Announcement
                _listPaymentSectionDataAnnouncement.postValue(it)
            }
        }
    }

    private fun loadTaskForms(uids: List<String>, _taskFormList: MutableLiveData<List<TaskForm>>) {
        val taskFormList = ArrayList<TaskForm>()
        uids.map { uid ->
            FireRepository.inst.getTaskForm(uid).let { response ->
                response.first.observeOnce { taskForm ->
                    taskFormList.add(taskForm)
                    if (taskFormList.size == uids.size)
                        _taskFormList.postValue(taskFormList.toList())
                }
            }
        }
    }


}