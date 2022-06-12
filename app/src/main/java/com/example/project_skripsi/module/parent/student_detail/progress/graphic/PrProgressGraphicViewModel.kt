package com.example.project_skripsi.module.parent.student_detail.progress.graphic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.AssignedTaskForm
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class PrProgressGraphicViewModel : ViewModel() {

    private val _subjects = MutableLiveData<List<String>>()
    val subjects : LiveData<List<String>> = _subjects

    private val _exams = MutableLiveData<Map<String, List<AssignedTaskForm>>>()
    val exams : LiveData<Map<String, List<AssignedTaskForm>>> = _exams

    private val _assignment = MutableLiveData<Map<String, List<AssignedTaskForm>>>()
    val assignment : LiveData<Map<String, List<AssignedTaskForm>>> = _assignment

    fun setStudent(studentId: String) {
        loadCurrentStudent(studentId)
    }

    private fun loadCurrentStudent(uid: String) {
        FireRepository.inst.getItem<Student>(uid).first.observeOnce { student ->
            student.studyClass?.let { loadStudyClass(it) }
            student.assignedExams
                ?.filter { it.isChecked == true }
                ?.groupBy { it.subjectName!! }
                ?.let { _exams.postValue(it) }
            student.assignedAssignments
                ?.filter { it.isChecked == true }
                ?.groupBy { it.subjectName!! }
                ?.let { _assignment.postValue(it) }
        }
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.inst.getItem<StudyClass>(uid).first.observeOnce { studyClass ->
            studyClass.subjects?.map { it.subjectName!! }?.let { _subjects.postValue(it) }
        }
    }

}