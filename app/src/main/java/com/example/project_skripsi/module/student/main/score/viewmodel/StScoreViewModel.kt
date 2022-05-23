package com.example.project_skripsi.module.student.main.score.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.model.local.AttendanceMainSection
import com.example.project_skripsi.core.model.local.Score
import com.example.project_skripsi.core.model.local.ScoreMainSection
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.module.parent.student_detail.progress.PrProgressViewModel
import com.example.project_skripsi.utils.Constant
import com.example.project_skripsi.utils.generic.GenericExtension.Companion.averageOf
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce


class StScoreViewModel : ViewModel() {

    // Fragment Data
    private val _scoreFragmentData = MutableLiveData<Score>()
    val scoreFragmentData: LiveData<Score> = _scoreFragmentData

    // Score
    private val _sectionScore = MutableLiveData<List<ScoreMainSection>>()
    val sectionScore: LiveData<List<ScoreMainSection>> = _sectionScore
    private val _subjects = MutableLiveData<List<Subject>>()
    private val mutableListOfTask: MutableList<AssignedTaskForm> = mutableListOf()
    private val listDataScore = arrayListOf<ScoreMainSection>()

    // Attendance
    private val _sectionAttendance = MutableLiveData<List<AttendanceMainSection>>()
    val sectionAttendance: LiveData<List<AttendanceMainSection>> = _sectionAttendance
    private val listDataAttendance = arrayListOf<AttendanceMainSection>()
    private var _mapAttendanceBySubject = MutableLiveData<Map<String, List<AttendedMeeting>>>()
    private val mutableListOfAttendance: MutableList<AttendedMeeting> = mutableListOf()
    // Achievement
    private val _achievements = MutableLiveData<List<Achievement>>()
    val achievements: LiveData<List<Achievement>> = _achievements

    companion object {
        const val tabCount = 3
        val tabHeader = arrayOf("Nilai", "Absensi", "Pencapaian")

        const val MID_EXAM_WEIGHT = 40
        const val FINAL_EXAM_WEIGHT = 40
        const val ASSIGNMENT_WEIGHT = 20
    }

    init {
        _subjects.observeOnce { subjects ->
            subjects.forEach { subject ->
                subject.subjectName?.let { subjectName ->
                    listDataScore.add(getScore(subjectName, mutableListOfTask.filter { it.subjectName == subjectName }))
                    listDataAttendance.add(getAttendance(subjectName, mutableListOfAttendance.filter { it.subjectName == subjectName }))
                }
            }
            _scoreFragmentData.postValue(
                Score(
                    listDataScore.averageOf { it.total_score ?: 0 },
                    listDataAttendance.sumOf { it.totalSick + it.totalLeave + it.totalAlpha },
                    achievements.value?.count() ?: 0
                )
            )
            _sectionScore.postValue(listDataScore)
            _sectionAttendance.postValue(listDataAttendance)
        }
        loadCurrentStudent(AuthRepository.instance.getCurrentUser().uid)
    }


    private fun loadCurrentStudent(uid: String) {
        FireRepository.inst.getItem<Student>(uid).first.observeOnce { student ->

            student.studyClass?.let { loadStudyClass(it) }

            student.assignedExams?.filter { it.isChecked == true }?.let { mutableListOfTask.addAll(it) }
            student.assignedAssignments?.filter { it.isChecked == true }?.let { mutableListOfTask.addAll(it) }
            student.achievements?.let { _achievements.postValue(it) }

            student.attendedMeetings.let {
                _mapAttendanceBySubject.postValue(it?.groupBy { subject -> subject.subjectName!! })
                if (it != null) { mutableListOfAttendance.addAll(it) }
            }
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

        val totalScore = PrProgressViewModel.MID_EXAM_WEIGHT * (midExam ?: 0) +
                PrProgressViewModel.FINAL_EXAM_WEIGHT * (finalExam ?: 0) +
                PrProgressViewModel.ASSIGNMENT_WEIGHT * (totalAssignment ?: 0)

        val scoreWeight = PrProgressViewModel.MID_EXAM_WEIGHT * (midExam?.let { 1 } ?: 0) +
                PrProgressViewModel.FINAL_EXAM_WEIGHT * (finalExam?.let { 1 } ?: 0) +
                PrProgressViewModel.ASSIGNMENT_WEIGHT * (totalAssignment?.let { 1 } ?: 0)

        return ScoreMainSection(
            subjectName = subjectName,
            mid_exam = midExam,
            final_exam = finalExam,
            total_assignment = totalAssignment,
            total_score = if (scoreWeight == 0) null else totalScore / scoreWeight,
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

    private fun getTaskFilter(atf: AssignedTaskForm, taskType : String) =
        atf.type == taskType && atf.isChecked == true
}

