package com.example.project_skripsi.core.model.local

import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.AttendedMeeting
import com.example.project_skripsi.utils.helper.DateHelper
import java.util.*

data class Attendance(
    val startTime: Date? = null,

    val endTime: Date? = null,

    val status: String? = null,

    val statusColor: Int? = null,
) {
    constructor(attendedMeeting: AttendedMeeting) : this(
        startTime = attendedMeeting.startTime,
        endTime = attendedMeeting.endTime,
        status = getStatus(attendedMeeting),
        statusColor = getStatusColor(attendedMeeting)
    )

    companion object {
        fun getStatus(attendedMeeting: AttendedMeeting) : String =
            when {
                attendedMeeting.startTime!! > DateHelper.getCurrentTime() -> "-"
                else -> attendedMeeting.status  ?: "-"
            }

        fun getStatusColor(attendedMeeting: AttendedMeeting) : Int =
            when {
                attendedMeeting.startTime!! > DateHelper.getCurrentTime() -> R.color.attendance_incoming
                attendedMeeting.status == "hadir" -> R.color.attendance_attend
                else -> R.color.attendance_not_attend
            }
    }
}
