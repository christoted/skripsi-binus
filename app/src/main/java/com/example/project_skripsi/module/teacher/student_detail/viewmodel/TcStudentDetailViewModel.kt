package com.example.project_skripsi.module.teacher.student_detail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.AssignedTaskForm
import com.example.project_skripsi.core.model.firestore.AttendedMeeting
import com.example.project_skripsi.core.model.firestore.Payment
import com.example.project_skripsi.core.model.firestore.Subject
import com.example.project_skripsi.core.model.local.AttendanceMainSection
import com.example.project_skripsi.core.model.local.Score
import com.example.project_skripsi.core.model.local.ScoreMainSection
import com.example.project_skripsi.core.model.local.TcStudentDetailPaymentSection
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.module.student.main.score.viewmodel.StScoreViewModel
import com.example.project_skripsi.module.student.main.score.viewmodel.averageOf
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.helper.DateHelper

class TcStudentDetailViewModel: ViewModel() {
    var studentUID = ""
    // Payment
    private val _listPaymentSection: MutableLiveData<List<TcStudentDetailPaymentSection>> = MutableLiveData()
    val listPaymentSection: LiveData<List<TcStudentDetailPaymentSection>> = _listPaymentSection
    // Score
    private val _sectionDatas = MutableLiveData<List<ScoreMainSection>>()
    val sectionDatas: LiveData<List<ScoreMainSection>> = _sectionDatas
    private val _subjects = MutableLiveData<List<Subject>>()
    private val mutableListOfTask: MutableList<AssignedTaskForm> = mutableListOf()
    private val listData = arrayListOf<ScoreMainSection>()
    // Attendance
    private val _sectionAttendances = MutableLiveData<List<AttendanceMainSection>>()
    val sectionAttendances: LiveData<List<AttendanceMainSection>> = _sectionAttendances
    private val listDataAttendance = arrayListOf<AttendanceMainSection>()
    private var _mapAttendanceBySubject = MutableLiveData<Map<String, List<AttendedMeeting>>>()
    private val attendances: MutableList<AttendedMeeting> = mutableListOf()

    companion object {
        const val tabCount = 3
        val tabHeader = arrayOf("Nilai", "Absensi", "Pembayaran")

        const val MID_EXAM_WEIGHT = 40
        const val FINAL_EXAM_WEIGHT = 40
        const val ASSIGNMENT_WEIGHT = 20

        const val TYPE_MID_EXAM = "ujian_tengah_semester"
        const val TYPE_FINAL_EXAM = "ujian_akhir_semester"
        const val TYPE_ASSIGNMENT = "tugas"
    }

    //  MARK: - Get Payment Flow
    fun getPayments() {
        val paymentSection: MutableList<TcStudentDetailPaymentSection> = mutableListOf()
        val payments: MutableList<Payment> = mutableListOf()
        val _payments: MutableLiveData<List<Payment>> = MutableLiveData()
        FireRepository.instance.getStudent(studentUID).first.observeOnce { student ->
            student.payments?.let {

                payments.addAll(it)
            }
            _payments.postValue(payments)
        }
        paymentSection.add(TcStudentDetailPaymentSection(title = "Jatuh Tempo", payments = emptyList()))
        paymentSection.add(TcStudentDetailPaymentSection(title = "Mendatang", payments = emptyList()))
        _payments.observeOnce {
            Log.d("987 ", "getPayments: palign bawah" + it)
            paymentSection[0].payments = it.filter {
                it.paymentDeadline!! < DateHelper.getCurrentDate()
            }
            paymentSection[1].payments = it.filter {
                it.paymentDeadline!! > DateHelper.getCurrentDate()
            }
            _listPaymentSection.postValue(paymentSection)
        }
    }

    // MARK: - Get Score Flow
    fun loadCurrentStudent(uid: String) {
        FireRepository.instance.getStudent(uid).let { response ->
            response.first.observeOnce { student ->
                student.studyClass?.let {
                    loadStudyClass(it)
                }
                //  val assignedTask: MutableMap<String, MutableList<AssignedTaskForm>> = mutableMapOf()

                student.assignedAssignments?.filter { it.taskChecked == true }?.let {
                    mutableListOfTask.addAll(it)
                }

                student.assignedExams?.filter { it.taskChecked == true }?.let {
                    mutableListOfTask.addAll(it)
                }
                student.attendedMeetings.let {
                    _mapAttendanceBySubject.postValue(it?.groupBy {
                        it.subjectName!!
                    })
                    if (it != null) {
                        attendances.addAll(it)
                    }
                }
            }
        }
    }

    private fun loadStudyClass(uid: String) {
        getScores()
        FireRepository.instance.getStudyClass(uid).let {
                response ->
            response.first.observeOnce {
                it.subjects.let { subjects ->
                    _subjects.postValue(subjects)
                }
            }
        }
    }

    private fun getScores() {
        _subjects.observeOnce { subjects ->
            subjects.forEach { subject ->
                subject.subjectName?.let { subjectName ->
                    val midExam: Int? = mutableListOfTask.filter {
                        getTaskFilter(it, StScoreViewModel.TYPE_MID_EXAM, subjectName)
                    }.let {
                        if (it.isEmpty()) null else it[0].score ?: 0
                    }

                    val finalExam: Int? = mutableListOfTask.filter {
                        getTaskFilter(it, StScoreViewModel.TYPE_FINAL_EXAM, subjectName)
                    }.let {
                        if (it.isEmpty()) null else it[0].score ?: 0
                    }

                    val totalAssignment : Int? = mutableListOfTask.filter {
                        getTaskFilter(it, StScoreViewModel.TYPE_ASSIGNMENT, subjectName)
                    }.let {
                        if (it.isEmpty()) null else it.averageOf { task -> task.score ?: 0 }
                    }

                    val totalScore = StScoreViewModel.MID_EXAM_WEIGHT * (midExam ?: 0) +
                            StScoreViewModel.FINAL_EXAM_WEIGHT * (finalExam ?: 0) +
                            StScoreViewModel.ASSIGNMENT_WEIGHT * (totalAssignment ?: 0)

                    val scoreWeight = StScoreViewModel.MID_EXAM_WEIGHT * (midExam?.let { 1 } ?: 0) +
                            StScoreViewModel.FINAL_EXAM_WEIGHT * (finalExam?.let { 1 } ?: 0) +
                            StScoreViewModel.ASSIGNMENT_WEIGHT * (totalAssignment?.let { 1 } ?: 0)

                    listData.add(ScoreMainSection(
                        subjectName = subjectName,
                        mid_exam = midExam,
                        final_exam = finalExam,
                        total_assignment = totalAssignment,
                        total_score = if (scoreWeight == 0) null else totalScore / scoreWeight,
                        sectionItem = mutableListOfTask.filter { it.subjectName == subjectName }))

                    addAttendanceData(subjectName, attendances.filter { it.subjectName == subjectName && it.status == "hadir"}.count())
                }
            }
            _sectionDatas.postValue(listData)
            _sectionAttendances.postValue(listDataAttendance)
        }
    }

    private fun getTaskFilter(atf: AssignedTaskForm, taskType : String, subjectName : String) =
        atf.type == taskType && atf.taskChecked == true && atf.subjectName == subjectName

    // Get Attendances
    private fun addAttendanceData(subjectName: String, totalPresence: Int) {
        listDataAttendance.add(AttendanceMainSection(subjectName, totalPresence,0, 0,0 ))
    }

    private fun getAttendances() {

    }
}

