package com.example.project_skripsi.module.common.zoom

import com.example.project_skripsi.core.model.firestore.Student

class MeetingHandler {

    companion object {
        val inst = MeetingHandler()
    }

    var isStudent = false
    var meetingId: String? = null
    var studentEntity : Student? = null

    fun startMeetingAsStudent(student: Student?, id: String?) {
        isStudent = true
        studentEntity = student
        meetingId = id
    }

    fun startMeetingAsTeacher() {
        isStudent = false
        meetingId = "XXX"
        studentEntity = null
    }

    fun endMeeting() {
        isStudent = false
        meetingId = null
        studentEntity = null
    }

}