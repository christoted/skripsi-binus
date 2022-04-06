package com.example.project_skripsi.module.teacher.main.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.model.local.SubjectGroup
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class TcTaskViewModel : ViewModel() {


    private val _subjectGroupList = MutableLiveData<List<SubjectGroup>>()
    val subjectGroupList : LiveData<List<SubjectGroup>> = _subjectGroupList

    private val _assignmentList = MutableLiveData<List<TaskForm>>()
    val assignmentList : LiveData<List<TaskForm>> = _assignmentList

    private val _examList = MutableLiveData<List<TaskForm>>()
    val examList : LiveData<List<TaskForm>> = _examList

    private val examIds = mutableMapOf<SubjectGroup, MutableList<String>>()
    private val assignmentIds = mutableMapOf<SubjectGroup, MutableList<String>>()

    init {
        loadTeacher(AuthRepository.instance.getCurrentUser().uid)
    }

    private fun loadTeacher(uid : String) {
        FireRepository.instance.getTeacher(uid).let { response ->
            response.first.observeOnce { teacher ->
                with(teacher) {
                    val subjectClasses = mutableListOf<SubjectGroup>()

                    teachingGroups?.map { group ->
                        val sg = SubjectGroup(group.subjectName, group.gradeLevel)
                        subjectClasses.add(sg)
                        group.createdExams?.map { examIds.getOrPut(sg) { mutableListOf()}.add(it) }
                        group.createdAssignments?.map { assignmentIds.getOrPut(sg) { mutableListOf()}.add(it) }
                    }
//                    teachingSubjects?.map { subject ->
//                        with(subject) {
//                            teaching_class?.map { studyClassId ->
//                                subjectName?.let { subjectClasses.add(Pair(it, studyClassId)) }
//                            }
//                        }
//                    }
//                    teacher.createdExams?.let { examIds.addAll(it) }
//                    teacher.createdAssignments?.let { assignmentIds.addAll(it) }
//                    loadSubjectGroup(subjectClasses)
                }
            }
        }
    }

    private fun loadSubjectGroup(uids: MutableList<Pair<String, String>>) {
//        val subjectGroupList = mutableListOf<SubjectGroup>()
//        uids.map { uid ->
//            FireRepository.instance.getStudyClass(uid.second).let { response ->
//                response.first.observeOnce { studyClass ->
//                    studyClass.gradeLevel?.let { subjectGroupList.add(SubjectGroup(uid.first, it)) }
//                    if (subjectGroupList.size == uids.size) {
//                        _subjectGroupList.postValue(subjectGroupList.toSet())
//                    }
//                }
//            }
//        }
    }

    fun loadExam(subjectGroup : SubjectGroup) {
//        loadTaskForm(subjectGroup, examIds, _examList)
    }

    fun loadAssignment(subjectGroup : SubjectGroup) {
//        loadTaskForm(subjectGroup, assignmentIds, _assignmentList)
    }

    private fun loadTaskForm(subjectGroup : SubjectGroup, uids: List<String>, mutableLiveData: MutableLiveData<List<TaskForm>>) {
        val taskFormList = mutableListOf<TaskForm>()
        var counter = 0
        uids.map { uid ->
            FireRepository.instance.getTaskForm(uid).let { response ->
                response.first.observeOnce {
                    counter++
                    if (it.gradeLevel == subjectGroup.gradeLevel && it.subjectName == subjectGroup.subjectName) taskFormList.add(it)
                    if (counter == uids.size) mutableLiveData.postValue(taskFormList)
                }
            }
        }
    }

}