package com.example.project_skripsi.module.parent.student_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.model.local.ParentSubject
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.Constant
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class PrStudentDetailViewModel : ViewModel() {

    private val _student = MutableLiveData<Student>()
    val student : LiveData<Student> = _student

    private val _school = MutableLiveData<School>()
    val school : LiveData<School> = _school

    private val _studyClass = MutableLiveData<StudyClass>()
    val studyClass : LiveData<StudyClass> = _studyClass

    private val _homeroomTeacher = MutableLiveData<Teacher>()
    val homeroomTeacher : LiveData<Teacher> = _homeroomTeacher

    private val _subjectList = MutableLiveData<List<ParentSubject>>()
    val subjectList : LiveData<List<ParentSubject>> = _subjectList

    private val mapSubjectToParentSubject = mutableMapOf<String, ParentSubject>()

    fun setStudent(studentId: String) {
        loadStudent(studentId)
    }

    private fun loadStudent(uid: String) {
        FireRepository.inst.getItem<Student>(uid).first.observeOnce { student ->
            _student.postValue(student)
            student.school?.let { loadSchool(it) }
            student.studyClass?.let { loadStudyClass(it) }
        }
    }

    private fun loadSchool(uid: String) {
        FireRepository.inst.getItem<School>(uid).first.observeOnce { _school.postValue(it) }
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.inst.getItem<StudyClass>(uid).first.observeOnce { studyClass ->
            _studyClass.postValue(studyClass)
            studyClass.homeroomTeacher?.let { loadHomeroomTeacher(it) }

            val teacherSubjectId = mutableListOf<TeacherSubjectId>()
            studyClass.subjects?.map { subject ->
                subject.teacher?.let { teacherSubjectId.add(TeacherSubjectId(it, subject.subjectName!!)) }

                subject.subjectName?.let { name ->
                    var countAttendance = 0
                    var countMeetingTotal = 0
                    subject.classMeetings?.map { classMeeting ->
                        countMeetingTotal++
                        student.value?.attendedMeetings?.firstOrNull {
                            it.id == classMeeting.id && it.status == Constant.ATTENDANCE_ATTEND
                        }.let { countAttendance++ }
                    }

                    var countSubmittedExam = 0
                    var countExamTotal = 0
                    subject.classExams?.map { classExam ->
                        countExamTotal++
                        student.value?.assignedExams?.firstOrNull {
                            it.id == classExam && !it.answers.isNullOrEmpty()
                        }?.let { countSubmittedExam++ }
                    }

                    var countSubmittedAssignment = 0
                    var countAssignmentTotal = 0
                    subject.classAssignments?.map { classAssignment ->
                        countAssignmentTotal++
                        student.value?.assignedAssignments?.firstOrNull {
                            it.id == classAssignment && it.isSubmitted!!
                        }?.let { countSubmittedAssignment++ }
                    }

                    mapSubjectToParentSubject[name] = ParentSubject(
                        subjectName = name,
                        attendance = countAttendance,
                        meetingTotal = countMeetingTotal,
                        exam = countSubmittedExam,
                        examTotal = countExamTotal,
                        assignment = countSubmittedAssignment,
                        assignmentTotal = countAssignmentTotal
                    )
                }
            }
            loadTeachers(teacherSubjectId)
        }
    }

    private fun loadHomeroomTeacher(uid: String) {
        FireRepository.inst.getItem<Teacher>(uid).first.observeOnce { _homeroomTeacher.postValue(it) }
    }

    private fun loadTeachers(uid: List<TeacherSubjectId>) {
        FireRepository.inst.getItems<Teacher>(uid.map { it.teacherId }).first.observeOnce {
            it.mapIndexed { index, teacher ->
                mapSubjectToParentSubject[uid[index].subjectName]?.teacherName = teacher.name
                mapSubjectToParentSubject[uid[index].subjectName]?.teacherPhoneNumber = teacher.phoneNumber
            }
            _subjectList.postValue(mapSubjectToParentSubject.map { item -> item.value })
        }
    }

    data class TeacherSubjectId(val teacherId: String, val subjectName: String)
}

