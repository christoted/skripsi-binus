package com.example.project_skripsi.module.student.subject_detail

import android.content.Context
import androidx.lifecycle.*
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.model.local.Attendance
import com.example.project_skripsi.core.model.local.TaskFormStatus
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.Constant
import com.example.project_skripsi.utils.Constant.Companion.ATTENDANCE_ATTEND
import com.example.project_skripsi.utils.generic.GenericLinkHandler
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.generic.HandledEvent

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

    private val _incompleteResource = MutableLiveData<HandledEvent<Resource>>()
    val incompleteResource : LiveData<HandledEvent<Resource>> = _incompleteResource

    private lateinit var curStudent: Student
    private var className = ""
    private var subjectName = ""
    private val mAssignedTaskForms = HashMap<String, AssignedTaskForm>()
    private val mAttendedMeetings = HashSet<String>()

    fun setSubject(subjectName : String) {
        this.subjectName = subjectName
        loadStudent(AuthRepository.inst.getCurrentUser().uid)
    }


    private fun loadStudent(uid: String) {
        FireRepository.inst.getItem<Student>(uid).first.observeOnce { student ->
            with(student) {
                curStudent = student
                attendedMeetings?.filter { it.status == ATTENDANCE_ATTEND }?.map { meeting ->
                    meeting.id?.let { mAttendedMeetings.add(it) }
                }
                assignedExams?.map { exam -> exam.id?.let { mAssignedTaskForms.put(it, exam) } }
                assignedAssignments?.map { asg ->
                    asg.id?.let {
                        mAssignedTaskForms.put(
                            it,
                            asg
                        )
                    }
                }
                studyClass?.let { loadStudyClass(it) }
            }
        }
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.inst.getItem<StudyClass>(uid).first.observeOnce { studyClass ->
            studyClass.name?.let { className = it }
            studyClass.subjects?.filter { it.subjectName == this.subjectName }?.map { subject ->
                with(subject) {
                    classAssignments?.let { loadTaskForms(it, _assignmentList) }
                    classExams?.let { loadTaskForms(it, _examList) }
                    classMeetings?.let { list ->
                        loadAttendances(list)
                        loadResources(list.mapNotNull {
                            if (it.meetingResource.isNullOrEmpty()) null else it.meetingResource
                        })
                    }
                    teacher?.let { loadTeacher(it) }
                }
            }
        }
    }

    private fun loadTeacher(uid: String) {
        FireRepository.inst.getItem<Teacher>(uid).first.observeOnce { _teacher.postValue(it) }
    }

    private fun loadAttendances(meetings: List<ClassMeeting>) {
        _attendanceList.postValue(meetings.map { meeting ->
            Attendance(meeting, mAttendedMeetings.contains(meeting.id))
        }.sortedBy { it.startTime })
    }

    private fun loadResources(uids: List<String>) {
        FireRepository.inst.getItems<Resource>(uids).first.observeOnce { list ->
            _resourceList.postValue(list.sortedBy { it.meetingNumber })
        }
    }

    private fun loadTaskForms(uids: List<String>, _taskFormList: MutableLiveData<List<TaskFormStatus>>) {
        FireRepository.inst.getItems<TaskForm>(uids).first.observeOnce { list ->
            val taskFormList = mutableListOf<TaskFormStatus>()
            list.map { taskForm ->
                mAssignedTaskForms[taskForm.id]?.let {
                    taskFormList.add(TaskFormStatus(className, taskForm, it))
                }
            }
            _taskFormList.postValue(taskFormList.sortedByDescending { it.endTime })
        }
    }

    fun openResource(context: Context, resource: Resource) {
        var incompleteId: String? = null
        resource.prerequisites?.map {
            curStudent.completedResources?.let { list ->
                if (!list.contains(it)) {
                    incompleteId = it
                    return@map
                }
            }
        }

        incompleteId?.let { id ->
            FireRepository.inst.getItem<Resource>(id).first.observeOnce {
                _incompleteResource.postValue(HandledEvent(it))
            }
        } ?: kotlin.run {
            resource.link?.let { GenericLinkHandler.goToLink(context, it) }
            resource.id?.let {
                if (curStudent.completedResources?.contains(it) == false) {
                    curStudent.completedResources?.add(it)
                    FireRepository.inst.alterItems(listOf(curStudent))
                }
            }
        }
    }
}