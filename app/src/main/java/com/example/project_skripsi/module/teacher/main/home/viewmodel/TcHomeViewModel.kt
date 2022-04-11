package com.example.project_skripsi.module.teacher.main.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.model.local.HomeMainSection
import com.example.project_skripsi.core.model.local.SubjectGroup
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.Constant
import com.example.project_skripsi.utils.Constant.Companion.SECTION_ANNOUNCEMENT
import com.example.project_skripsi.utils.Constant.Companion.SECTION_ASSIGNMENT
import com.example.project_skripsi.utils.Constant.Companion.SECTION_EXAM
import com.example.project_skripsi.utils.Constant.Companion.SECTION_MEETING
import com.example.project_skripsi.utils.Constant.Companion.SECTION_PAYMENT
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.helper.DateHelper

class TcHomeViewModel: ViewModel() {

    private val _teacherData: MutableLiveData<Teacher> = MutableLiveData()
    val teacherData: LiveData<Teacher> = _teacherData

    private val _studyClass: MutableLiveData<StudyClass> = MutableLiveData()
    val studyClass: LiveData<StudyClass> = _studyClass

    private val _listMeeting: MutableLiveData<List<ClassMeeting>> = MutableLiveData()
    val listMeeting: LiveData<List<ClassMeeting>> = _listMeeting

    private val _announcements: MutableLiveData<List<Announcement>> = MutableLiveData()
    val announcements: LiveData<List<Announcement>> = _announcements

    private val _examList: MutableLiveData<List<TaskForm>> = MutableLiveData()
    val examList: LiveData<List<TaskForm>> = _examList

    private val _assignmentList: MutableLiveData<List<TaskForm>> = MutableLiveData()
    val assignmentList: LiveData<List<TaskForm>> = _assignmentList

    private val examIds: MutableList<String> = mutableListOf()
    private val assignmentIds: MutableList<String> = mutableListOf()

    private val _sectionData = MutableLiveData<List<HomeMainSection>>()
    val sectionData: LiveData<List<HomeMainSection>> = _sectionData

    init {
        loadTeacher(AuthRepository.instance.getCurrentUser().uid)
        initData()
    }

    private fun initData() {
        val listData: MutableList<HomeMainSection> = mutableListOf()
        listData.add(HomeMainSection(SECTION_MEETING, emptyList()))
        listData.add(HomeMainSection(SECTION_EXAM, emptyList()))
        listData.add(HomeMainSection(SECTION_ASSIGNMENT, emptyList()))
        listData.add(HomeMainSection(SECTION_PAYMENT, emptyList()))
        listData.add(HomeMainSection(SECTION_ANNOUNCEMENT, emptyList()))

        _listMeeting.observeOnce {
            listData[0] = HomeMainSection(SECTION_MEETING, it)
            _sectionData.postValue(listData)
        }
        _examList.observeOnce {
            listData[1] = HomeMainSection(SECTION_EXAM, it)
            _sectionData.postValue(listData)
        }
        _assignmentList.observeOnce {
            listData[2] = HomeMainSection(SECTION_ASSIGNMENT, it)
            _sectionData.postValue(listData)
        }
        _announcements.observeOnce {
            listData[4] = HomeMainSection(SECTION_ANNOUNCEMENT, it)
            _sectionData.postValue(listData)
        }
        _sectionData.postValue(listData)
    }

    private fun loadTeacher(uid: String) {
        FireRepository.instance.getTeacher(uid).first.observeOnce {
            val subjectGroups = mutableListOf<SubjectGroup>()
            it.teachingGroups?.map { teachingGroup ->
                val sg = SubjectGroup(teachingGroup.subjectName!!, teachingGroup.gradeLevel!!)
                subjectGroups.add(sg)
                teachingGroup.createdExams?.let { exams -> examIds.addAll(exams) }
                teachingGroup.createdAssignments?.let { assignments -> assignmentIds.addAll(assignments)}
            }
            _teacherData.postValue(it)
            loadHomeRoomClass(it.homeroomClass ?: "")
            loadExam()
            loadAssignment()
            loadAnnouncement()
        }
    }
    // Load Schedule
    private fun loadHomeRoomClass(uid: String) {
        val meetings: MutableList<ClassMeeting> = mutableListOf()
        FireRepository.instance.getStudyClass(uid).first.observeOnce {
            _studyClass.postValue(it)
            it.subjects?.map { subject ->
                subject.classMeetings?.let { meets -> meetings.addAll(meets) }
            }
            // TODO:: Filter Here
            _listMeeting.postValue(meetings)
        }
    }

    // Load Exam & Load Assignment
     private fun loadExam() {
        loadTaskForm(examIds.toList(), _examList)
    }

    private fun loadAssignment() {
        loadTaskForm(assignmentIds.toList(), _assignmentList)
    }

    private fun loadTaskForm(uids: List<String>, mutableLiveData: MutableLiveData<List<TaskForm>>) {
        val taskFormList = mutableListOf<TaskForm>()
        uids.map { uid ->
            FireRepository.instance.getTaskForm(uid).first.observeOnce {
                taskFormList.add(it)
                // TODO:: Filter Here
                if (taskFormList.size == uids.size) mutableLiveData.postValue(taskFormList)
            }
        }
    }

    // Load Payment Deadline

    // Load Announcement
    private fun loadAnnouncement(){
        val announcements: MutableList<Announcement> = mutableListOf()
        FireRepository.instance.getAnnouncements().first.observeOnce {
            announcements.addAll(it)
        }
        _announcements.postValue(announcements)
    }
}