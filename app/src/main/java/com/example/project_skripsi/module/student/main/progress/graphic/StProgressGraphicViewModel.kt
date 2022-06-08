package com.example.project_skripsi.module.student.main.progress.graphic


import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.AssignedTaskForm
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class StProgressGraphicViewModel : ViewModel() {


    private lateinit var mapOfSubjectExam : Map<String, List<AssignedTaskForm>>
    private lateinit var mapOfSubjectAssignment : Map<String, List<AssignedTaskForm>>

    init {
        loadCurrentStudent(AuthRepository.inst.getCurrentUser().uid)
    }

    private fun loadCurrentStudent(uid: String) {
        FireRepository.inst.getItem<Student>(uid).first.observeOnce { student ->
            student.studyClass?.let { loadStudyClass(it) }
            student.assignedExams?.groupBy { it.subjectName!! }?.let { mapOfSubjectExam = it }
            student.assignedAssignments?.groupBy { it.subjectName!! }?.let { mapOfSubjectAssignment = it }
        }
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.inst.getItem<StudyClass>(uid).first.observeOnce {
            it.subjects?.map { it.subjectName!! }.let {

            }
        }
    }

}