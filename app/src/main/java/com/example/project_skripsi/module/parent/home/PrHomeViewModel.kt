package com.example.project_skripsi.module.parent.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.Parent
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.model.firestore.Subject
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import kotlin.math.min

class PrHomeViewModel : ViewModel() {

    companion object {
        private val contentPerPage = 3
    }

    private val _studentList = MutableLiveData<List<Student>>()
    val studentList: LiveData<List<Student>> = _studentList

    init {
        loadParent(AuthRepository.instance.getCurrentUser().uid)
    }

    private fun loadParent(uid: String) {
        FireRepository.instance.getItem<Parent>(uid).first.observeOnce { parent ->
            parent.children?.let { loadStudent(it) }
        }
    }

    private fun loadStudent(uids: List<String>) {
        FireRepository.instance.getItems<Student>(uids).first.observeOnce {
            _studentList.postValue(it)
        }
    }

    fun getStudents(page: Int): List<Student> {
        val startIdx = page * contentPerPage
        val endIdx = min(startIdx + contentPerPage, studentList.value?.size?:0)
        return studentList.value?.subList(startIdx, endIdx)?: emptyList()
    }

    fun getStudentPageCount() : Int{
        val subjects = studentList.value?.size ?: 0
        return (subjects + contentPerPage - 1) / contentPerPage
    }

}