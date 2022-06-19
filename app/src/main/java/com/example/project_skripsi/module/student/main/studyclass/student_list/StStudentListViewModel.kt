package com.example.project_skripsi.module.student.main.studyclass.student_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class StStudentListViewModel : ViewModel() {


    private val _studentList = MutableLiveData<List<Student>>()
    val studentList : LiveData<List<Student>> = _studentList

    fun setStudyClass(studyClassId: String) {
        FireRepository.inst.getAllItems<Student>().first.observeOnce { studentList ->
            _studentList.postValue(
                studentList.filter { it.studyClass == studyClassId }.sortedBy { it.attendanceNumber }
            )
        }
    }
}