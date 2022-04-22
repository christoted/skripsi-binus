package com.example.project_skripsi.core.model.local

import com.example.project_skripsi.core.model.firestore.AttendedMeeting

data class AttendanceMainSection(

    val subjectName: String,

    val totalPresence: Int,

    val totalSick: Int,

    val totalLeave: Int,

    val totalAlpha: Int,

    val sectionItem: List<AttendedMeeting> = emptyList()
)