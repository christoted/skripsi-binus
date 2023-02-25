package com.example.project_skripsi.core.model.local

import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.AssignedTaskForm
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.utils.helper.DateHelper
import java.util.*

data class TaskFormStatus(

    val id: String? = null,

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

    val isChecked: Boolean? = null
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
        isChecked = assignedTaskForm.isChecked
    )

    companion object {

        fun getDuration(taskForm: TaskForm): Long =
            DateHelper.getMinute(taskForm.startTime, taskForm.endTime)


        fun getStatus(taskForm: TaskForm, assignedTaskForm: AssignedTaskForm): String =
            when {
                taskForm.endTime!! < DateHelper.getCurrentTime() -> "selesai"
                taskForm.startTime!! > DateHelper.getCurrentTime() -> "belum dimulai"
                !assignedTaskForm.isSubmitted!! -> "belum terkumpul"
                else -> "terkumpul"
            }

        fun getStatusColor(taskForm: TaskForm, assignedTaskForm: AssignedTaskForm): Int =
            when {
                assignedTaskForm.isSubmitted!! -> R.color.task_submit
                !assignedTaskForm.isSubmitted!! &&
                        taskForm.endTime!! < DateHelper.getCurrentTime() -> R.color.task_not_submit
                else -> R.color.task_ongoing
            }

        private fun getScore(assignedTaskForm: AssignedTaskForm): Int? =
            if (assignedTaskForm.isChecked!!) assignedTaskForm.score
            else null
    }
}