package com.example.project_skripsi.module.teacher.form.alter.finalization

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.model.local.SubjectGroup
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.module.teacher.form.alter.TcAlterTaskViewModel
import com.example.project_skripsi.utils.Constant.Companion.TASK_FORM_MC
import com.example.project_skripsi.utils.Constant.Companion.TASK_TYPE_ASSIGNMENT
import com.example.project_skripsi.utils.Constant.Companion.TASK_TYPE_FINAL_EXAM
import com.example.project_skripsi.utils.Constant.Companion.TASK_TYPE_MID_EXAM
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class TcAlterTaskFinalizationViewModel : ViewModel() {

    companion object {
        val mapFormType = mapOf(
            TASK_TYPE_MID_EXAM to "Ujian Tengah Semester",
            TASK_TYPE_FINAL_EXAM to "Ujian Akhir Semester",
            TASK_TYPE_ASSIGNMENT to "Tugas"
        )
    }

    var selectedClass = listOf<String>()

    private val _oldTaskForm = MutableLiveData<TaskForm>()
    val oldTaskForm : LiveData<TaskForm> = _oldTaskForm

    private val _classList = MutableLiveData<List<StudyClass>>()
    val classList : LiveData<List<StudyClass>> = _classList

    private val _finalizationCompleted = MutableLiveData<Boolean>()
    val finalizationCompleted : LiveData<Boolean> = _finalizationCompleted

    private lateinit var subjectGroup : SubjectGroup
    private val classIds = mutableListOf<String>()

    private var formType: Int = -1


    fun initData(subjectName: String, gradeLevel: Int, formType : Int, taskFormId : String) {
        subjectGroup = SubjectGroup(subjectName, gradeLevel)
        this.formType = formType
        loadTeacher(AuthRepository.inst.getCurrentUser().uid)
        loadTaskForm(taskFormId)
    }

    private fun loadTeacher(uid : String) {
        FireRepository.inst.getItem<Teacher>(uid).first.observeOnce { teacher ->
            teacher.teachingGroups?.firstOrNull { it.subjectName == subjectGroup.subjectName && it.gradeLevel == subjectGroup.gradeLevel }
                ?.let { group ->
                    group.teachingClasses?.let { classIds.addAll(it) }
                }
        }
    }

    private fun loadTaskForm(uid: String) {
        FireRepository.inst.getItem<TaskForm>(uid).first.observeOnce{ _oldTaskForm.postValue(it) }
    }

    fun loadClass() {
        FireRepository.inst.getItems<StudyClass>(classIds).first.observeOnce { _classList.postValue(it) }
    }

    fun submitForm() {
        val items = mutableListOf<Any>()

        val taskForm = TaskForm(
            id = oldTaskForm.value?.id,
            title = oldTaskForm.value?.title,
            gradeLevel = oldTaskForm.value?.gradeLevel,
            type = oldTaskForm.value?.type,
            startTime = oldTaskForm.value?.startTime,
            endTime = oldTaskForm.value?.endTime,
            location = oldTaskForm.value?.location,
            subjectName = oldTaskForm.value?.subjectName,
            questions = oldTaskForm.value?.questions,
            assignedClasses = selectedClass,
            prerequisiteResources = oldTaskForm.value?.prerequisiteResources,
            prerequisiteTaskForms = oldTaskForm.value?.prerequisiteTaskForms,
            isFinalized = true
        )
        items.add(taskForm)


        FireRepository.inst.getItems<StudyClass>(selectedClass).first.observeOnce { list ->
            val students = mutableSetOf<String>()
            list.map { studyClass ->
                students.addAll(studyClass.students ?: emptyList())

                studyClass.subjects?.firstOrNull {
                    it.subjectName == subjectGroup.subjectName
                }?.let {
                    if (formType == TcAlterTaskViewModel.TYPE_EXAM) {
                        it.classExams
                    } else {
                        it.classAssignments
                    }?.add(taskForm.id ?: "")
                }
                items.add(studyClass)
            }


            FireRepository.inst.getItems<Student>(students.toList()).first.observeOnce { list2 ->
                list2.map { student ->
                    (if (formType == TcAlterTaskViewModel.TYPE_EXAM) student.assignedExams
                    else student.assignedAssignments
                            )?.add(
                            AssignedTaskForm(
                                id = taskForm.id,
                                title = taskForm.title,
                                type = taskForm.type,
                                startTime = taskForm.startTime,
                                endTime = taskForm.endTime,
                                isSubmitted = false,
                                subjectName = taskForm.subjectName,
                                isChecked = false,
                                score = 0,
                                answers = taskForm.questions?.map {
                                    if (it.type == TASK_FORM_MC) Answer("0", 0)
                                    else Answer("", 0)
                                }?: emptyList()
                            )
                        )
                    items.add(student)
                }

                FireRepository.inst.alterItems(items).first.observeOnce{
                    _finalizationCompleted.postValue(it)
                }
            }
        }



    }

}