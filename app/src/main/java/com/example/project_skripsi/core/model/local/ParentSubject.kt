package com.example.project_skripsi.core.model.local


data class ParentSubject(

    val subjectName: String? = null,

    var teacherName: String? = null,

    var teacherPhoneNumber: String? = null,

    var attendance: Int? = null,

    var meetingTotal: Int? = null,

    var exam: Int? = null,

    var examTotal: Int? = null,

    var assignment: Int? = null,

    var assignmentTotal: Int? = null,
)