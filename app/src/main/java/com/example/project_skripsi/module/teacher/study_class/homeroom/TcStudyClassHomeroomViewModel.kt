package com.example.project_skripsi.module.teacher.study_class.homeroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.helper.DateHelper

class TcStudyClassHomeroomViewModel : ViewModel() {

    private val _studyClass = MutableLiveData<StudyClass>()
    val studyClass: LiveData<StudyClass> = _studyClass

    private val _classChief = MutableLiveData<Student>()
    val classChief: LiveData<Student> = _classChief

    private val _studentList = MutableLiveData<List<Student>>()
    val studentList: LiveData<List<Student>> = _studentList

    fun setClass(classId: String) {
        loadStudyClass(classId)
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
        FireRepository.inst.getItems<Student>(uids).first.observeOnce { _studentList.postValue(it) }
    }

    fun getAttendanceAbsent(student: Student): Int =
        student.attendedMeetings?.filter { it.status != "hadir" }?.size ?: 0

    fun getPaymentStatus(student: Student): Pair<String, Int> {
        if ((student.payments?.filter { it.paymentDate == null && it.paymentDeadline!! < DateHelper.getCurrentDate() }?.size ?: 0) > 0)
            return Pair("jatuh tempo", R.color.payment_late)

        if ((student.payments?.filter { it.paymentDate == null }?.size ?: 0) > 0)
            return Pair("belum jatuh tempo", R.color.payment_incoming)

        return Pair("lengkap", R.color.payment_complete)
    }
}