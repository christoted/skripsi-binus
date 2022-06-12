package com.example.project_skripsi.module.teacher.study_class.teaching

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.Constant.Companion.TASK_TYPE_ASSIGNMENT
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class TcStudyClassTeachingViewModel : ViewModel() {

    private val _studyClass = MutableLiveData<StudyClass>()
    val studyClass: LiveData<StudyClass> = _studyClass

    private val _classChief = MutableLiveData<Student>()
    val classChief: LiveData<Student> = _classChief

    private val _studentList = MutableLiveData<List<Student>>()
    val studentList: LiveData<List<Student>> = _studentList

    var subjectName = ""
    var studyClassId = ""

    fun setClassAndSubject(studyClassId: String, subjectName: String) {
        loadStudyClass(studyClassId)
        this.studyClassId = studyClassId
        this.subjectName = subjectName
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.inst.getItem<StudyClass>(uid).first.observeOnce { studyClass ->
            _studyClass.postValue(studyClass)
            studyClass.classChief?.let { loadClassChief(it) }
            studyClass.students?.let { loadStudents(it) }
        }
    }

    private fun loadClassChief(uid: String) {
        FireRepository.inst.getItem<Student>(uid).first.observeOnce { _classChief.postValue(it) }
    }

    private fun loadStudents(uids: List<String>) {
        FireRepository.inst.getItems<Student>(uids).first.observeOnce { list ->
            _studentList.postValue(list.sortedBy { it.attendanceNumber })
        }
    }

    fun getAttendanceAbsent(student: Student): Int =
        student.attendedMeetings?.filter { it.status != "hadir" && it.subjectName == subjectName }?.size ?: 0

    fun getLastAssignmentStatus(student: Student): Pair<String, Int> {
        val asg = student.assignedAssignments?.lastOrNull { it.subjectName == subjectName && it.type == TASK_TYPE_ASSIGNMENT }
            ?: return Pair("tidak ada tugas", R.color.last_assignment_null)

        if ((asg.answers?.size ?: 0) == 0 ) return Pair("tidak kumpul", R.color.last_assignment_not_submit)
        return Pair("terkumpul", R.color.last_assignment_submit)
    }
}