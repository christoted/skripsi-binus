package com.example.project_skripsi.core.model.local

import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.ClassMeeting
import com.example.project_skripsi.utils.helper.DateHelper
import java.util.*

data class Attendance(
    val startTime: Date? = null,

    val endTime: Date? = null,

    val status: String? = null,

    val statusColor: Int? = null,
) {
    constructor(classMeeting: ClassMeeting, hasAttend: Boolean) : this(
        startTime = classMeeting.startTime,
        endTime = classMeeting.endTime,
        status = getStatus(classMeeting, hasAttend),
        statusColor = getStatusColor(classMeeting, hasAttend)
    )

    companion object {
        fun getStatus(classMeeting: ClassMeeting, hasAttend: Boolean) : String =
            when {
                classMeeting.startTime!! > DateHelper.getCurrentDate() -> "-"
                hasAttend -> "hadir"
                classMeeting.endTime!! < DateHelper.getCurrentDate() -> "absen"
                else -> "-"
            }

        fun getStatusColor(classMeeting: ClassMeeting, hasAttend: Boolean) : Int =
            when {
                classMeeting.startTime!! > DateHelper.getCurrentDate() -> R.color.attendance_incoming
                hasAttend -> R.color.attendance_attend
                classMeeting.endTime!! < DateHelper.getCurrentDate() -> R.color.attendance_not_attend
                else -> R.color.attendance_incoming
            }
    }
}
