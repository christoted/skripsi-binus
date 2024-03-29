package com.example.project_skripsi.module.parent.student_detail.progress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.model.local.AttendanceMainSection
import com.example.project_skripsi.core.model.local.Score
import com.example.project_skripsi.core.model.local.ScoreMainSection
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.Constant.Companion.ASSIGNMENT_WEIGHT
import com.example.project_skripsi.utils.Constant.Companion.ATTENDANCE_ALPHA
import com.example.project_skripsi.utils.Constant.Companion.ATTENDANCE_ATTEND
import com.example.project_skripsi.utils.Constant.Companion.ATTENDANCE_LEAVE
import com.example.project_skripsi.utils.Constant.Companion.ATTENDANCE_SICK
import com.example.project_skripsi.utils.Constant.Companion.FINAL_EXAM_WEIGHT
import com.example.project_skripsi.utils.Constant.Companion.MID_EXAM_WEIGHT
import com.example.project_skripsi.utils.Constant.Companion.TASK_TYPE_ASSIGNMENT
import com.example.project_skripsi.utils.Constant.Companion.TASK_TYPE_FINAL_EXAM
import com.example.project_skripsi.utils.Constant.Companion.TASK_TYPE_MID_EXAM
import com.example.project_skripsi.utils.generic.GenericExtension.Companion.averageOf
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.helper.DateHelper
import kotlin.math.ceil

class PrProgressViewModel : ViewModel() {

    companion object {
        const val tabCount = 3
        val tabHeader = listOf("Nilai", "Absensi", "Pencapaian")

        const val VIEW_TYPE_SCORE = 0
        const val VIEW_TYPE_ATTENDANCE = 1
        const val VIEW_TYPE_ACHIEVEMENT = 2
    }


    // Fragment Data
    private val _scoreFragmentData = MutableLiveData<Score>()
    val scoreFragmentData: LiveData<Score> = _scoreFragmentData

    private val _sectionScore = MutableLiveData<List<ScoreMainSection>>()
    val sectionScore: LiveData<List<ScoreMainSection>> = _sectionScore

    private val _sectionAttendance = MutableLiveData<List<AttendanceMainSection>>()
    val sectionAttendance: LiveData<List<AttendanceMainSection>> = _sectionAttendance

    private val _achievements = MutableLiveData<List<Achievement>>()
    val achievements: LiveData<List<Achievement>> = _achievements

    private val _subjects = MutableLiveData<List<Subject>>()

    // Score
    private val listDataScore = mutableListOf<ScoreMainSection>()
    private val mutableListOfTask: MutableList<AssignedTaskForm> = mutableListOf()


    // Attendance
    private val listDataAttendance = mutableListOf<AttendanceMainSection>()
    private val mutableListOfAttendance: MutableList<AttendedMeeting> = mutableListOf()


    fun setStudent(studentId: String) {
        loadCurrentStudent(studentId)
    }


    init {
        _subjects.observeOnce { subjects ->
            subjects.forEach { subject ->
                subject.subjectName?.let { subjectName ->
                    listDataScore.add(
                        getScore(
                            subjectName,
                            mutableListOfTask.filter { it.subjectName == subjectName })
                    )
                    listDataAttendance.add(
                        getAttendance(
                            subjectName,
                            mutableListOfAttendance.filter { it.subjectName == subjectName })
                    )
                }
            }
            _scoreFragmentData.postValue(
                Score(
                    listDataScore.averageOf { it.total_score ?: 0 },
                    listDataAttendance.sumOf { it.totalPresence } / listDataAttendance.size,
                    achievements.value?.count() ?: 0
                )
            )
            _sectionScore.postValue(listDataScore)
            _sectionAttendance.postValue(listDataAttendance)
        }

    }

    private fun loadCurrentStudent(uid: String) {
        FireRepository.inst.getItem<Student>(uid).first.observeOnce { student ->
            student.assignedExams?.filter { it.isChecked == true }
                ?.let { mutableListOfTask.addAll(it) }
            student.assignedAssignments?.filter { it.isChecked == true }
                ?.let { mutableListOfTask.addAll(it) }
            student.achievements?.let { _achievements.postValue(it) }

            student.attendedMeetings?.let { list ->
                mutableListOfAttendance.addAll(
                    list.filter { it.endTime!! < DateHelper.getCurrentTime() }
                )
            }
            student.studyClass?.let { loadStudyClass(it) }
        }
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.inst.getItem<StudyClass>(uid).first.observeOnce {
            it.subjects.let { subjects -> _subjects.postValue(subjects) }
        }
    }

    private fun getScore(subjectName: String, itemList: List<AssignedTaskForm>): ScoreMainSection {
        val midExam: Int? = itemList.filter {
            getTaskFilter(it, TASK_TYPE_MID_EXAM)
        }.let {
            if (it.isEmpty()) null else it[0].score ?: 0
        }

        val finalExam: Int? = itemList.filter {
            getTaskFilter(it, TASK_TYPE_FINAL_EXAM)
        }.let {
            if (it.isEmpty()) null else it[0].score ?: 0
        }

        val totalAssignment: Int? = itemList.filter {
            getTaskFilter(it, TASK_TYPE_ASSIGNMENT)
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

    private fun getAttendance(
        subjectName: String,
        itemList: List<AttendedMeeting>
    ): AttendanceMainSection {
        return AttendanceMainSection(
            subjectName = subjectName,
            totalPresence = itemList.count { it.status == ATTENDANCE_ATTEND && it.endTime!! < DateHelper.getCurrentTime() },
            totalSick = itemList.count { it.status == ATTENDANCE_SICK && it.endTime!! < DateHelper.getCurrentTime() },
            totalLeave = itemList.count { it.status == ATTENDANCE_LEAVE && it.endTime!! < DateHelper.getCurrentTime() },
            totalAlpha = itemList.count { it.status == ATTENDANCE_ALPHA && it.endTime!! < DateHelper.getCurrentTime() },
            sectionItem = itemList
        )
    }

    private fun getTaskFilter(atf: AssignedTaskForm, taskType: String) =
        atf.type == taskType && atf.isChecked == true
}