package com.example.project_skripsi.module.student.main.score.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.Achievement
import com.example.project_skripsi.core.model.firestore.AssignedTaskForm
import com.example.project_skripsi.core.model.firestore.AttendedMeeting
import com.example.project_skripsi.core.model.firestore.Subject
import com.example.project_skripsi.core.model.local.AttendanceMainSection
import com.example.project_skripsi.core.model.local.Score
import com.example.project_skripsi.core.model.local.ScoreMainSection
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce


class StScoreViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is score Fragment"
    }
    val text: LiveData<String> = _text

    // Fragment Data
    private val _scoreFragmentData = MutableLiveData<Score>()
    val scoreFragmentData: LiveData<Score> = _scoreFragmentData

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
    // Achievement
    private val _achievements = MutableLiveData<List<Achievement>>()
    val achievements: LiveData<List<Achievement>> = _achievements

    companion object {
        const val tabCount = 3
        val tabHeader = arrayOf("Nilai", "Absensi", "Pencapaian")

        const val MID_EXAM_WEIGHT = 40
        const val FINAL_EXAM_WEIGHT = 40
        const val ASSIGNMENT_WEIGHT = 20

        const val TYPE_MID_EXAM = "ujian_tengah_semester"
        const val TYPE_FINAL_EXAM = "ujian_akhir_semester"
        const val TYPE_ASSIGNMENT = "tugas"
    }

    init {
        var totalScores: MutableList<Int?> = mutableListOf()
        _subjects.observeOnce { subjects ->
            subjects.forEach { subject ->
                subject.subjectName?.let { subjectName ->
                    val midExam: Int? = mutableListOfTask.filter {
                        getTaskFilter(it, TYPE_MID_EXAM, subjectName)
                    }.let {
                        if (it.isEmpty()) null else it[0].score ?: 0
                    }

                    val finalExam: Int? = mutableListOfTask.filter {
                        getTaskFilter(it, TYPE_FINAL_EXAM, subjectName)
                    }.let {
                        if (it.isEmpty()) null else it[0].score ?: 0
                    }

                    val totalAssignment : Int? = mutableListOfTask.filter {
                        getTaskFilter(it, TYPE_ASSIGNMENT, subjectName)
                    }.let {
                        if (it.isEmpty()) null else it.averageOf { task -> task.score ?: 0 }
                    }

                    val totalScore = MID_EXAM_WEIGHT * (midExam ?: 0) +
                            FINAL_EXAM_WEIGHT * (finalExam ?: 0) +
                            ASSIGNMENT_WEIGHT * (totalAssignment ?: 0)

                    val scoreWeight = MID_EXAM_WEIGHT * (midExam?.let { 1 } ?: 0) +
                            FINAL_EXAM_WEIGHT * (finalExam?.let { 1 } ?: 0) +
                            ASSIGNMENT_WEIGHT * (totalAssignment?.let { 1 } ?: 0)
                    
                    listData.add(ScoreMainSection(
                        subjectName = subjectName,
                        mid_exam = midExam,
                        final_exam = finalExam,
                        total_assignment = totalAssignment,
                        total_score = if (scoreWeight == 0) null else totalScore / scoreWeight,
                        sectionItem = mutableListOfTask.filter { it.subjectName == subjectName }))

                    addAttendanceData(subjectName, attendances.filter { it.subjectName == subjectName && it.status == "hadir"}.count())

                    val totalScoreBySubject = if (scoreWeight == 0) null else totalScore / scoreWeight
                    totalScores.add(totalScoreBySubject)

                    _scoreFragmentData.postValue(Score(totalScores.filterNotNull().sum() / totalScores.filterNotNull().count(), attendances.filter { it.subjectName == subjectName && it.status != "hadir"}.count(), 0))
                }
            }
            _sectionDatas.postValue(listData)
            _sectionAttendances.postValue(listDataAttendance)
        }
        loadCurrentStudent(AuthRepository.instance.getCurrentUser().uid)
    }

    private fun addAttendanceData(subjectName: String, totalPresence: Int) {
        listDataAttendance.add(AttendanceMainSection(subjectName, totalPresence,0, 0,0 ))
    }

    private fun loadCurrentStudent(uid: String) {
        FireRepository.instance.getStudent(uid).let {
                response ->
            response.first.observeOnce {
                    student ->
                student.studyClass?.let {
                    loadStudyClass(it)
                }
              //  val assignedTask: MutableMap<String, MutableList<AssignedTaskForm>> = mutableMapOf()

                student.assignedAssignments?.filter { it.isChecked == true }?.let {
                    mutableListOfTask.addAll(it)
                }

                student.achievements?.let {
                    _achievements.postValue(it)
                }

                student.assignedExams?.filter { it.isChecked == true }?.let {
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
        FireRepository.instance.getStudyClass(uid).let {
            response ->
            response.first.observeOnce {
                it.subjects.let { subjects ->
                    _subjects.postValue(subjects)
                }
            }
        }
    }

    private fun getTaskFilter(atf: AssignedTaskForm, taskType : String, subjectName : String) =
        atf.type == taskType && atf.isChecked == true && atf.subjectName == subjectName

}

fun <T> Iterable<T>.averageOf(selector: (T) -> Int): Int {
    var sum = 0
    var count = 0
    for (element in this) {
        sum += selector(element)
        count++
    }
    return sum / count
}
