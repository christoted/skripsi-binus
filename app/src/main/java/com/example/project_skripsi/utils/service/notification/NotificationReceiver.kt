package com.example.project_skripsi.utils.service.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.model.firestore.Teacher
import com.example.project_skripsi.core.model.local.ClassIdSubject
import com.example.project_skripsi.core.model.local.ClassTaskFormId
import com.example.project_skripsi.core.model.local.TeacherAgendaMeeting
import com.example.project_skripsi.core.model.local.TeacherAgendaTaskForm
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.helper.DateHelper.Companion.convertDateToCalendarDay
import com.example.project_skripsi.utils.helper.DateHelper.Companion.getCurrentDate

class NotificationReceiver : BroadcastReceiver() {

    companion object {
        val inst = NotificationReceiver()

        const val EXTRA_IS_DAILY = "is_daily"
        const val EXTRA_IS_STUDENT = "is_student"
        const val EXTRA_ID = "id"
        const val EXTRA_TITLE = "title"
        const val EXTRA_BODY = "body"
    }

    fun newIntent(
        context: Context,
        isDaily: Boolean,
        isStudent: Boolean,
        id: Int,
        title: String,
        body: String
    ): Intent {
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra(EXTRA_IS_DAILY, isDaily)
        intent.putExtra(EXTRA_IS_STUDENT, isStudent)
        intent.putExtra(EXTRA_ID, id)
        intent.putExtra(EXTRA_TITLE, title)
        intent.putExtra(EXTRA_BODY, body)
        return intent
    }

    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras
        val isDaily = bundle?.getBoolean(EXTRA_IS_DAILY) ?: false
        val isStudent = bundle?.getBoolean(EXTRA_IS_STUDENT) ?: false
        val notificationId = bundle?.getInt(EXTRA_ID) ?: 0
        Log.d("12345-", "receive")

        val notificationUtil = NotificationUtil(context)
        if (isDaily) {
            if (isStudent) {
                getStudentDaily(notificationUtil, notificationId)
            } else {
                getTeacherDaily(notificationUtil, notificationId)
            }
        } else {
            val title = bundle?.getString(EXTRA_TITLE) ?: ""
            val body = bundle?.getString(EXTRA_BODY) ?: ""

            val notification = notificationUtil.getNotificationBuilder(title, body).build()
            notificationUtil.getManager().notify(notificationId, notification)
        }
    }

    private fun getStudentDaily(notificationUtil: NotificationUtil, notificationId: Int) {
        FireRepository.inst.getItem<Student>(AuthRepository.inst.getCurrentUser().uid).first.observeOnce { student ->

            val meetings =
                student.attendedMeetings?.filter { convertDateToCalendarDay(it.startTime) == getCurrentDate() }
                    ?: emptyList()
            val exams =
                student.assignedExams?.filter { convertDateToCalendarDay(it.startTime) == getCurrentDate() }
                    ?: emptyList()
            val assignments =
                student.assignedAssignments?.filter { convertDateToCalendarDay(it.startTime) == getCurrentDate() }
                    ?: emptyList()

            var suffix = ""
            if (meetings.isNotEmpty())
                suffix += if (suffix.isNotEmpty()) ", ${meetings.size} pertemuan" else " ${meetings.size} pertemuan"
            if (exams.isNotEmpty())
                suffix += if (suffix.isNotEmpty()) ", ${exams.size} ujian" else " ${exams.size} ujian"
            if (assignments.isNotEmpty())
                suffix += if (suffix.isNotEmpty()) ", ${assignments.size} tugas" else " ${assignments.size} tugas"

            val title = if (suffix.isNotEmpty()) "Siap untuk belajar hari ini" else "Hai!"
            val message =
                if (suffix.isNotEmpty()) "Kamu punya$suffix" else "Tidak ada agenda hari ini"

            val notification = notificationUtil.getNotificationBuilder(title, message).build()
            notificationUtil.getManager().notify(notificationId, notification)
        }
    }

    private fun getTeacherDaily(notificationUtil: NotificationUtil, notificationId: Int) {
        FireRepository.inst.getItem<Teacher>(AuthRepository.inst.getCurrentUser().uid).first.observeOnce { teacher ->

            val classes = mutableListOf<ClassIdSubject>()
            teacher.teachingGroups?.map { teachingGroup ->
                teachingGroup.teachingClasses?.map { classId ->
                    classes.add(ClassIdSubject(classId, teachingGroup.subjectName!!))
                }
            }

            val allMeetings = mutableListOf<TeacherAgendaMeeting>()
            val allExams = mutableListOf<ClassTaskFormId>()
            val allAssignments = mutableListOf<ClassTaskFormId>()

            FireRepository.inst.getItems<StudyClass>(classes.map {
                it.studyClassId
            }).first.observeOnce { list ->
                classes.map { classIdSubject ->
                    list.firstOrNull { it.id == classIdSubject.studyClassId }?.let { studyClass ->
                        studyClass.subjects?.firstOrNull { item -> item.subjectName == classIdSubject.subjectName }
                            .let { subject ->
                                subject?.classMeetings?.map { meeting ->
                                    allMeetings.add(
                                        TeacherAgendaMeeting(
                                            studyClass.name ?: "",
                                            meeting
                                        )
                                    )
                                }
                                subject?.classAssignments?.map { asgId ->
                                    allAssignments.add(
                                        ClassTaskFormId(
                                            studyClass.id!!,
                                            studyClass.name ?: "",
                                            asgId
                                        )
                                    )
                                }
                                subject?.classExams?.map { examId ->
                                    allExams.add(
                                        ClassTaskFormId(
                                            studyClass.id!!,
                                            studyClass.name ?: "",
                                            examId
                                        )
                                    )
                                }
                            }
                    }
                }

                val meetings =
                    allMeetings.filter { convertDateToCalendarDay(it.classMeeting.startTime) == getCurrentDate() }
                var exams: List<TeacherAgendaTaskForm> = emptyList()
                var assignments: List<TeacherAgendaTaskForm> = emptyList()
                var counter = 0
                FireRepository.inst.getItems<TaskForm>(allExams.map { it.taskFormId }).first.observeOnce { list ->
                    val taskFormList =
                        allExams.mapNotNull { classTaskFormId ->
                            list.firstOrNull { it.id == classTaskFormId.taskFormId }
                                ?.let { taskForm ->
                                    TeacherAgendaTaskForm(
                                        classTaskFormId.studyClassId,
                                        classTaskFormId.studyClassName,
                                        taskForm
                                    )
                                }
                        }
                    exams =
                        taskFormList.filter { convertDateToCalendarDay(it.taskForm.startTime) == getCurrentDate() }
                    if (++counter == 2)
                        getTeacherDailyNotification(
                            notificationUtil,
                            notificationId,
                            meetings,
                            exams,
                            assignments
                        )
                }
                FireRepository.inst.getItems<TaskForm>(allAssignments.map { it.taskFormId }).first.observeOnce { list ->
                    val taskFormList =
                        allAssignments.mapNotNull { classTaskFormId ->
                            list.firstOrNull { it.id == classTaskFormId.taskFormId }
                                ?.let { taskForm ->
                                    TeacherAgendaTaskForm(
                                        classTaskFormId.studyClassId,
                                        classTaskFormId.studyClassName,
                                        taskForm
                                    )
                                }
                        }
                    assignments =
                        taskFormList.filter { convertDateToCalendarDay(it.taskForm.startTime) == getCurrentDate() }
                    if (++counter == 2)
                        getTeacherDailyNotification(
                            notificationUtil,
                            notificationId,
                            meetings,
                            exams,
                            assignments
                        )
                }
            }
        }
    }

    private fun getTeacherDailyNotification(
        notificationUtil: NotificationUtil,
        notificationId: Int,
        meetings: List<Any>,
        exams: List<Any>,
        assignments: List<Any>,
    ) {
        Log.d("12345-", "woi ${meetings.size} ${exams.size} ${assignments.size}")

        var suffix = ""
        if (meetings.isNotEmpty())
            suffix += if (suffix.isNotEmpty()) ", ${meetings.size} pertemuan" else " ${meetings.size} pertemuan"
        if (exams.isNotEmpty())
            suffix += if (suffix.isNotEmpty()) ", ${exams.size} ujian" else " ${exams.size} ujian"
        if (assignments.isNotEmpty())
            suffix += if (suffix.isNotEmpty()) ", ${assignments.size} tugas" else " ${assignments.size} tugas"

        val title = if (suffix.isNotEmpty()) "Semangat mengajar hari ini" else "Hai!"
        val message = if (suffix.isNotEmpty()) "Kamu punya$suffix" else "Tidak ada agenda hari ini"

        val notification = notificationUtil.getNotificationBuilder(title, message).build()
        notificationUtil.getManager().notify(notificationId, notification)
    }
}