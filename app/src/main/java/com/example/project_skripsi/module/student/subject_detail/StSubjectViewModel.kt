package com.example.project_skripsi.module.student.subject_detail

import androidx.lifecycle.*
import com.example.project_skripsi.core.model.firestore.AssignedTaskForm
import com.example.project_skripsi.core.model.firestore.ClassMeeting
import com.example.project_skripsi.core.model.firestore.Resource
import com.example.project_skripsi.core.model.firestore.Teacher
import com.example.project_skripsi.core.model.local.Attendance
import com.example.project_skripsi.core.model.local.TaskFormStatus
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class StSubjectViewModel : ViewModel() {

    companion object {
        const val tabCount = 4
        val tabHeader = arrayOf("Absen", "Materi", "Ujian", "Tugas")
    }

    private val _teacher = MutableLiveData<Teacher>()
    val teacher: LiveData<Teacher> = _teacher

    private val _attendanceList = MutableLiveData<List<Attendance>>()
    val attendanceList : LiveData<List<Attendance>> = _attendanceList

    private val _resourceList = MutableLiveData<List<Resource>>()
    val resourceList : LiveData<List<Resource>> = _resourceList

    private val _examList = MutableLiveData<List<TaskFormStatus>>()
    val examList : LiveData<List<TaskFormStatus>> = _examList

    private val _assignmentList = MutableLiveData<List<TaskFormStatus>>()
    val assignmentList : LiveData<List<TaskFormStatus>> = _assignmentList

    private var className = ""
    private var subjectName = ""
    private val mAssignedTaskForms = HashMap<String, AssignedTaskForm>()
    private val mAttendedMeetings = HashSet<String>()

    fun setSubject(subjectName : String) {
        this.subjectName = subjectName
        loadStudent(AuthRepository.instance.getCurrentUser().uid)
    }


    private fun loadStudent(uid: String) {
        FireRepository.instance.getStudent(uid).let { response ->
            response.first.observeOnce { student ->
                with(student) {
                    attendedMeetings?.map { meeting -> mAttendedMeetings.add(meeting)}
                    assignedExams?.map { exam -> exam.id?.let { mAssignedTaskForms.put(it, exam) }}
                    assignedAssignments?.map { asg -> asg.id?.let { mAssignedTaskForms.put(it, asg) }}
                    studyClass?.let { loadStudyClass(it) }
                }
            }
        }
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.instance.getStudyClass(uid).let { response ->
            response.first.observeOnce { studyClass ->
                studyClass.name?.let { className = it }
                studyClass.subjects?.filter { it.subjectName == this.subjectName }?.map { subject ->
                    with(subject) {
                        classAssignments?.let { loadTaskForms(it, _assignmentList) }
                        classExams?.let { loadTaskForms(it, _examList) }
                        classMeetings?.let { loadAttendances(it) }
                        classResources?.let { loadResources(it) }
                        teacher?.let { loadTeacher(it) }
                    }
                }
            }
        }
    }

    private fun loadTeacher(uid: String) {
        FireRepository.instance.getTeacher(uid).let { response ->
            response.first.observeOnce { _teacher.postValue(it) }
        }
    }

    private fun loadAttendances(meetings: List<ClassMeeting>) {
        val attendanceList = ArrayList<Attendance>()
        meetings.map { meeting ->
            attendanceList.add(Attendance(meeting, mAttendedMeetings.contains(meeting.id)))
            if (attendanceList.size == meetings.size)
                _attendanceList.postValue(attendanceList.toList())
        }
    }

    private fun loadResources(uids: List<String>) {
        val resourceList = ArrayList<Resource>()
        uids.map { uid ->
            FireRepository.instance.getResource(uid).let { response ->
                response.first.observeOnce {
                    resourceList.add(it)
                    if (resourceList.size == uids.size) {
                        _resourceList.postValue(resourceList.toList())
                        response.first.removeObserver {  }
                    }
                }
            }
        }
    }

    private fun loadTaskForms(uids: List<String>, _taskFormList: MutableLiveData<List<TaskFormStatus>>) {
        val taskFormList = ArrayList<TaskFormStatus>()
        uids.map { uid ->
            FireRepository.instance.getTaskForm(uid).let { response ->
                response.first.observeOnce { taskForm ->
                    mAssignedTaskForms[uid]?.let {
                        taskFormList.add(TaskFormStatus(className, taskForm, it))
                    }
                    if (taskFormList.size == uids.size)
                        _taskFormList.postValue(taskFormList.toList())
                }
            }
        }
    }
}