package com.example.project_skripsi.module.teacher.form.alter_task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.Resource
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.model.local.SubjectGroup
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class TcAlterTaskViewModel : ViewModel() {

    companion object {
        const val QUERY_CLASS = 0
        const val QUERY_RESOURCE = 1
        const val QUERY_TASK_FORM = 2
    }

    var selectedClass = listOf<StudyClass>()
    var selectedResource = listOf<Resource>()
    var selectedTaskForm = listOf<TaskForm>()

    private val _classList = MutableLiveData<List<StudyClass>>()
    val classList : LiveData<List<StudyClass>> = _classList

    private val _resourceList = MutableLiveData<List<Resource>>()
    val resourceList : LiveData<List<Resource>> = _resourceList

    private val _taskFormList = MutableLiveData<List<TaskForm>>()
    val taskFormList : LiveData<List<TaskForm>> = _taskFormList

    private lateinit var subjectGroup : SubjectGroup

    fun initData() {
        subjectGroup = SubjectGroup("Biologi", 12)
    }

    private fun loadTeacher(uid : String) {
        FireRepository.instance.getTeacher(uid).let { response ->
            response.first.observeOnce { teacher ->
                with(teacher) {
                    teachingSubjects.
                }
            }
        }
    }

    fun loadClass() {

    }

}