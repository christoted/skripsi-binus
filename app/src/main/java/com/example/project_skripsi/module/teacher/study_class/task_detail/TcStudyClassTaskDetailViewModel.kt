package com.example.project_skripsi.module.teacher.study_class.task_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class TcStudyClassTaskDetailViewModel : ViewModel() {

    companion object {
        const val TASK_UNCHECKED = 0
        const val TASK_CHECKED = 1
        const val tabCount = 2
        val tabHeader = arrayOf("Belum dikoreksi", "Selesai dikoreksi")
    }

    private val _studyClass = MutableLiveData<StudyClass>()
    val studyClass: LiveData<StudyClass> = _studyClass

    private val _uncheckedList = MutableLiveData<List<Student>>()
    val uncheckedList: LiveData<List<Student>> = _uncheckedList

    private val _checkedList = MutableLiveData<List<Student>>()
    val checkedList: LiveData<List<Student>> = _checkedList

    private val _taskForm = MutableLiveData<TaskForm>()
    val taskForm: LiveData<TaskForm> = _taskForm

    var subjectName = ""
    var taskFormId = ""
    var studyClassId = ""

    fun setData(studyClassId: String, subjectName: String, taskFormId: String) {
        loadStudyClass(studyClassId)
        loadTaskForm(taskFormId)
        this.studyClassId = studyClassId
        this.subjectName = subjectName
        this.taskFormId = taskFormId
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.inst.getItem<StudyClass>(uid).first.observeOnce { studyClass ->
            _studyClass.postValue(studyClass)
            studyClass.students?.let { loadStudents(it) }
        }
    }

    private fun loadTaskForm(uid: String) {
        FireRepository.inst.getItem<TaskForm>(uid).first.observeOnce { _taskForm.postValue(it) }
    }

    private fun loadStudents(uids: List<String>) {
        val uncheckedList : MutableList<Student> = mutableListOf()
        val checkedList : MutableList<Student> = mutableListOf()

        FireRepository.inst.getItems<Student>(uids).first.observeOnce { list ->
            list.map { student ->
                student.assignedAssignments?.firstOrNull { it.id == taskFormId }?.let {
                    if (it.isChecked!!) checkedList.add(student)
                    else uncheckedList.add(student)
                }
                student.assignedExams?.firstOrNull { it.id == taskFormId }?.let {
                    if (it.isChecked!!) checkedList.add(student)
                    else uncheckedList.add(student)
                }
            }
            _uncheckedList.postValue(uncheckedList)
            _checkedList.postValue(checkedList)
        }
    }

    fun getTaskScore(item: Student): Int {
        item.assignedAssignments?.firstOrNull { it.id == taskFormId }?.score.let {
            if (it != null) return it
        }
        item.assignedExams?.firstOrNull { it.id == taskFormId }?.score.let {
            if (it != null) return it
        }
        return 0
    }
}