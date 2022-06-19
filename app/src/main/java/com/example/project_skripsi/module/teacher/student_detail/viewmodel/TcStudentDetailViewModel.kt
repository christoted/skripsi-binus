package com.example.project_skripsi.module.teacher.student_detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.model.local.AttendanceMainSection
import com.example.project_skripsi.core.model.local.ScoreMainSection
import com.example.project_skripsi.core.model.local.TcStudentDetailPaymentSection
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.Constant
import com.example.project_skripsi.utils.Constant.Companion.ASSIGNMENT_WEIGHT
import com.example.project_skripsi.utils.Constant.Companion.FINAL_EXAM_WEIGHT
import com.example.project_skripsi.utils.Constant.Companion.MID_EXAM_WEIGHT
import com.example.project_skripsi.utils.generic.GenericExtension.Companion.averageOf
import com.example.project_skripsi.utils.generic.GenericExtension.Companion.compareTo
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.helper.DateHelper.Companion.convertDateToCalendarDay
import com.example.project_skripsi.utils.helper.DateHelper.Companion.getCurrentDate
import kotlin.math.ceil

class TcStudentDetailViewModel: ViewModel() {

    companion object {
        const val tabCount = 3
        val tabHeader = arrayOf("Nilai", "Absensi", "Pembayaran")
        val paymentType = listOf("Telat", "Mendatang")
    }

    private var studentId = ""

    private val _student = MutableLiveData<Student>()
    val student: LiveData<Student> = _student

    private val _parent = MutableLiveData<Parent>()
    val parent: LiveData<Parent> = _parent


    // Score
    private val _sectionScore = MutableLiveData<List<ScoreMainSection>>()
    val sectionScore: LiveData<List<ScoreMainSection>> = _sectionScore

    private val _subjects = MutableLiveData<List<Subject>>()
    private val mutableListOfTask: MutableList<AssignedTaskForm> = mutableListOf()
    private val listDataScore = arrayListOf<ScoreMainSection>()

    // Attendance
    private val _sectionAttendance = MutableLiveData<List<AttendanceMainSection>>()
    val sectionAttendance: LiveData<List<AttendanceMainSection>> = _sectionAttendance

    private var _mapAttendanceBySubject = MutableLiveData<Map<String, List<AttendedMeeting>>>()
    private val mutableListOfAttendance: MutableList<AttendedMeeting> = mutableListOf()
    private val listDataAttendance = arrayListOf<AttendanceMainSection>()

    // Payment
    private val _sectionPayment: MutableLiveData<List<TcStudentDetailPaymentSection>> = MutableLiveData()
    val sectionPayment: LiveData<List<TcStudentDetailPaymentSection>> = _sectionPayment

    init {
        _subjects.observeOnce { subjects ->
            subjects.forEach { subject ->
                subject.subjectName?.let { subjectName ->
                    listDataScore.add(getScore(subjectName, mutableListOfTask.filter { it.subjectName == subjectName }))
                    listDataAttendance.add(getAttendance(subjectName, mutableListOfAttendance.filter { it.subjectName == subjectName }))
                }
            }
            _sectionScore.postValue(listDataScore)
            _sectionAttendance.postValue(listDataAttendance)
        }
    }

    fun setStudent(studentId: String) {
        this.studentId = studentId
        loadCurrentStudent(studentId)
    }

    private fun loadCurrentStudent(uid: String) {
        FireRepository.inst.getItem<Student>(uid).first.observeOnce { student ->
            _student.postValue(student)
            student.parent?.let { loadParent(it) }

            student.assignedExams?.filter { it.isChecked == true }?.let { mutableListOfTask.addAll(it) }
            student.assignedAssignments?.filter { it.isChecked == true }?.let { mutableListOfTask.addAll(it) }

            student.attendedMeetings?.let {
                _mapAttendanceBySubject.postValue( it.groupBy { subject -> subject.subjectName!! })
                mutableListOfAttendance.addAll(it)
            }

            student.payments?.let {
                val paymentSection: MutableList<TcStudentDetailPaymentSection> = mutableListOf()
                paymentSection.add(TcStudentDetailPaymentSection(title = paymentType[0], payments = emptyList()))
                paymentSection.add(TcStudentDetailPaymentSection(title = paymentType[1], payments = emptyList()))
                paymentSection[0].payments = it.filter { payment ->
                    convertDateToCalendarDay(payment.paymentDeadline) < getCurrentDate() && payment.paymentDate == null
                }.sortedBy { payment -> payment.paymentDeadline }
                paymentSection[1].payments = it.filter { payment ->
                    convertDateToCalendarDay(payment.paymentDeadline) >= getCurrentDate() && payment.paymentDate == null
                }.sortedBy { payment -> payment.paymentDeadline }
                _sectionPayment.postValue(paymentSection)
            }
            student.studyClass?.let { loadStudyClass(it) }
        }
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.inst.getItem<StudyClass>(uid).first.observeOnce {
            it.subjects.let { subjects -> _subjects.postValue(subjects) }
        }
    }

    private fun getScore(subjectName: String, itemList : List<AssignedTaskForm>) : ScoreMainSection {
        val midExam: Int? = itemList.filter {
            getTaskFilter(it, Constant.TASK_TYPE_MID_EXAM)
        }.let {
            if (it.isEmpty()) null else it[0].score ?: 0
        }

        val finalExam: Int? = itemList.filter {
            getTaskFilter(it, Constant.TASK_TYPE_FINAL_EXAM)
        }.let {
            if (it.isEmpty()) null else it[0].score ?: 0
        }

        val totalAssignment : Int? = itemList.filter {
            getTaskFilter(it, Constant.TASK_TYPE_ASSIGNMENT)
        }.let {
            if (it.isEmpty()) null else it.averageOf { task -> task.score ?: 0 }
        }

        val totalScore = MID_EXAM_WEIGHT * (midExam ?: 0) +
                FINAL_EXAM_WEIGHT * (finalExam ?: 0) +
                ASSIGNMENT_WEIGHT * (totalAssignment ?: 0)

//        val scoreWeight = MID_EXAM_WEIGHT * (midExam?.let { 1 } ?: 0) +
//                FINAL_EXAM_WEIGHT * (finalExam?.let { 1 } ?: 0) +
//                ASSIGNMENT_WEIGHT * (totalAssignment?.let { 1 } ?: 0)

        val scoreWeight = 100

        return ScoreMainSection(
            subjectName = subjectName,
            mid_exam = midExam,
            final_exam = finalExam,
            total_assignment = totalAssignment,
            total_score = if (scoreWeight == 0) null else ceil(totalScore.toDouble() / scoreWeight).toInt(),
            sectionItem = itemList
        )

    }

    private fun getAttendance(subjectName: String, itemList : List<AttendedMeeting>) : AttendanceMainSection {
        return AttendanceMainSection(
            subjectName = subjectName,
            totalPresence = itemList.count { it.status == Constant.ATTENDANCE_ATTEND },
            totalSick = itemList.count { it.status == Constant.ATTENDANCE_SICK },
            totalLeave = itemList.count { it.status == Constant.ATTENDANCE_LEAVE },
            totalAlpha = itemList.count { it.status == Constant.ATTENDANCE_ALPHA },
            sectionItem = itemList
        )
    }

    private fun loadParent(uid: String) {
        FireRepository.inst.getItem<Parent>(uid).first.observeOnce{ _parent.postValue(it) }
    }

    private fun getTaskFilter(atf: AssignedTaskForm, taskType : String) =
        atf.type == taskType && atf.isChecked == true

}

