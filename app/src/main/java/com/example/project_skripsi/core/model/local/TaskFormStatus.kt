package com.example.project_skripsi.core.model.local

import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.AssignedTaskForm
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.utils.helper.DateHelper
import java.util.*

data class TaskFormStatus(

    val id : String? = null,

    val title: String? = null,

    val type: String? = null,

    val startTime: Date? = null,

    val endTime: Date? = null,

    val duration: Long? = null,

    val className: String? = null,

    val subjectName: String? = null,

    val status: String? = null,

    val statusColor: Int? = null,

    val score: Int? = null,

) {
    constructor(className: String, taskForm: TaskForm, assignedTaskForm: AssignedTaskForm) : this(
        id = assignedTaskForm.id,
        title = taskForm.title,
        type = taskForm.type,
        startTime = taskForm.startTime,
        endTime = taskForm.endTime,
        duration = getDuration(taskForm),
        className = className,
        subjectName = taskForm.subjectName,
        status = getStatus(taskForm, assignedTaskForm),
        statusColor = getStatusColor(taskForm, assignedTaskForm),
        score = getScore(assignedTaskForm),
    )

    companion object {

        fun getDuration(taskForm: TaskForm) : Long =
            if (taskForm.startTime == null || taskForm.endTime == null) 0
            else (taskForm.endTime.time - taskForm.startTime.time) / (1000 * 60)


        fun getStatus(taskForm: TaskForm, assignedTaskForm: AssignedTaskForm) : String =
            when {
                taskForm.endTime!! < DateHelper.getCurrentTime() -> "selesai"
                taskForm.startTime!! > DateHelper.getCurrentTime() -> "belum dimulai"
                assignedTaskForm.answers!!.isEmpty() -> "belum terkumpul"
                else -> "terkumpul"
            }

        fun getStatusColor(taskForm: TaskForm, assignedTaskForm: AssignedTaskForm): Int =
            when {
                assignedTaskForm.answers!!.isNotEmpty() -> R.color.task_submit
                assignedTaskForm.answers.isEmpty() &&
                        taskForm.endTime!! < DateHelper.getCurrentTime() -> R.color.task_not_submit
                else -> R.color.task_ongoing
            }

        private fun getScore(assignedTaskForm: AssignedTaskForm) : Int? =
            if (assignedTaskForm.isChecked!!) assignedTaskForm.score
            else null
    }
}