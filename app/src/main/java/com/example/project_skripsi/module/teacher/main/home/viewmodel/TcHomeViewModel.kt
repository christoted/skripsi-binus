package com.example.project_skripsi.module.teacher.main.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.model.local.*
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

    private val _listMeeting: MutableLiveData<List<TeacherAgendaMeeting>> = MutableLiveData()
    val listMeeting: LiveData<List<TeacherAgendaMeeting>> = _listMeeting

    private val _announcements: MutableLiveData<List<Announcement>> = MutableLiveData()
    val announcements: LiveData<List<Announcement>> = _announcements

    private val _examList: MutableLiveData<List<TeacherAgendaTaskForm>> = MutableLiveData()
    val examList: LiveData<List<TeacherAgendaTaskForm>> = _examList

    private val _assignmentList: MutableLiveData<List<TeacherAgendaTaskForm>> = MutableLiveData()
    val assignmentList: LiveData<List<TeacherAgendaTaskForm>> = _assignmentList

    private val _paymentList = MutableLiveData<List<Payment>>()
    val paymentList: LiveData<List<Payment>> = _paymentList

    private val examIds: MutableList<String> = mutableListOf()
    private val assignmentIds: MutableList<String> = mutableListOf()

    private val _sectionData = MutableLiveData<List<HomeMainSection>>()
    val sectionData: LiveData<List<HomeMainSection>> = _sectionData

    init {
        initData()
        loadTeacher(AuthRepository.instance.getCurrentUser().uid)
        loadAnnouncement()
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
        _paymentList.observeOnce {
            listData[3] = HomeMainSection(SECTION_PAYMENT, it)
            _sectionData.postValue(listData)
        }
        _announcements.observeOnce {
            listData[4] = HomeMainSection(SECTION_ANNOUNCEMENT, it)
            _sectionData.postValue(listData)
        }
        _sectionData.postValue(listData)
    }

    private fun loadTeacher(uid: String) {
        FireRepository.instance.getItem<Teacher>(uid).first.observeOnce {
            it.payments?.let { payments ->
                _paymentList.postValue(payments)
            }
            val classes = mutableListOf<ClassIdSubject>()
        //    val subjectGroups = mutableListOf<SubjectGroup>()
            it.teachingGroups?.map { teachingGroup ->
                teachingGroup.teachingClasses?.map { classId ->
                    classes.add(ClassIdSubject(classId, teachingGroup.subjectName!!))
                }
//                val sg = SubjectGroup(teachingGroup.subjectName!!, teachingGroup.gradeLevel!!)
//                subjectGroups.add(sg)
//                teachingGroup.createdExams?.let { exams -> examIds.addAll(exams) }
//                teachingGroup.createdAssignments?.let { assignments -> assignmentIds.addAll(assignments)}
            }
            _teacherData.postValue(it)
            loadStudyClasses(classes)
//            loadHomeRoomClass(it.homeroomClass ?: "")
//            loadExam()
//            loadAssignment()
        }
    }
    // Load Study Classes
    private fun loadStudyClasses(uids: List<ClassIdSubject>) {
        FireRepository.instance.getItems<StudyClass>(uids.map {
            it.studyClassId
        }).first.observeOnce {
            val meetings = mutableListOf<TeacherAgendaMeeting>()
            val exams = mutableListOf<ClassTaskFormId>()
            val assignments = mutableListOf<ClassTaskFormId>()

            it.mapIndexed { index, studyClass ->
                studyClass.subjects?.firstOrNull { item ->
                    item.subjectName == uids[index].subjectName
                }.let {
                    subject ->
                    // TODO: Add Here
                    subject?.classMeetings?.map { meeting ->
                        meetings.add(TeacherAgendaMeeting(studyClass.name ?: "", meeting))
                    }

                    subject?.classAssignments?.map { asgId ->
                        assignments.add(ClassTaskFormId(studyClass.id!!, studyClass.name ?: "", asgId))
                    }

                    subject?.classExams?.map { examId ->
                        exams.add(ClassTaskFormId(studyClass.id!!, studyClass.name ?: "", examId ))
                    }
                }
            }
            _listMeeting.postValue(meetings)
            loadTaskForm(exams, _examList)
            loadTaskForm(assignments, _assignmentList)
        }
    }

    private fun loadTaskForm(uids: List<ClassTaskFormId>, mutableLiveData: MutableLiveData<List<TeacherAgendaTaskForm>>) {
        FireRepository.instance.getItems<TaskForm>(uids.map { it.taskFormId }).first.observeOnce {
            val taskFormList = ArrayList<TeacherAgendaTaskForm>()
            // TODO: Add Here
            it.mapIndexed { index, taskForm ->
                taskFormList.add(TeacherAgendaTaskForm(uids[index].studyClassId, uids[index].studyClassName, taskForm))
            }
            mutableLiveData.postValue(taskFormList)
        }
    }

    // Load Announcement
    private fun loadAnnouncement(){
        val announcements: MutableList<Announcement> = mutableListOf()
        FireRepository.instance.getAnnouncements().first.observeOnce {
            announcements.addAll(it)
        }
        _announcements.postValue(announcements)
    }

    // Load Schedule
//    private fun loadHomeRoomClass(uid: String) {
//        val meetings: MutableList<ClassMeeting> = mutableListOf()
//        FireRepository.instance.getItem<StudyClass>(uid).first.observeOnce {
//            _studyClass.postValue(it)
//            it.subjects?.map { subject ->
//                subject.classMeetings?.let { meets -> meetings.addAll(meets) }
//            }
//            // TODO:: Filter Here
//            _listMeeting.postValue(meetings)
//        }
//    }

    // Load Exam & Load Assignment
//     private fun loadExam() {
//        loadTaskForm(examIds.toList(), _examList)
//    }
//
//    private fun loadAssignment() {
//        loadTaskForm(assignmentIds.toList(), _assignmentList)
//    }


}