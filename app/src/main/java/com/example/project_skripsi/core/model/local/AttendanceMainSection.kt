package com.example.project_skripsi.core.model.local

data class AttendanceMainSection(
    val subjectName: String,
    val totalPresence: Int,
    val totalSick: Int,
    val totalLeave: Int,
    val totalAlpha: Int
)