package com.example.project_skripsi.core.model.firestore

import com.example.project_skripsi.core.model.local.HomeSectionData
import com.example.project_skripsi.core.model.local.ScoreSectionData
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import java.util.*

@IgnoreExtraProperties
data class Student(

    @DocumentId
    val id: String? = null,

    val nis: String? = null,

    val name: String? = null,

    val profile: String? = null,

    @get: PropertyName("attendance_number")
    @set: PropertyName("attendance_number")
    var attendanceNumber: Int? = null,

    val address: String? = null,

    @get: PropertyName("phone_number")
    @set: PropertyName("phone_number")
    var phoneNumber: String? = null,

    val age: Int? = null,

    val gender: String? = null,

    val parent: String? = null,

    @get: PropertyName("study_class")
    @set: PropertyName("study_class")
    var studyClass: String? = null,

    val school: String? = null,

    val payments: List<Payment>? = null,

    @get: PropertyName("attended_meetings")
    @set: PropertyName("attended_meetings")
    var attendedMeetings: List<AttendedMeeting>? = null,

    @get: PropertyName("completed_resources")
    @set: PropertyName("completed_resources")
    var completedResources: MutableList<String>? = null,

    @get: PropertyName("assigned_exams")
    @set: PropertyName("assigned_exams")
    var assignedExams: MutableList<AssignedTaskForm>? = null,

    @get: PropertyName("assigned_assignments")
    @set: PropertyName("assigned_assignments")
    var assignedAssignments: MutableList<AssignedTaskForm>? = null,

    var achievements: MutableList<Achievement>? = null,

    )

@IgnoreExtraProperties
data class Payment(

    val title: String? = null,

    val nominal: Int? = null,

    @get: PropertyName("account_number")
    @set: PropertyName("account_number")
    var accountNumber: String? = null,

    @get: PropertyName("payment_deadline")
    @set: PropertyName("payment_deadline")
    var paymentDeadline: Date? = null,

    @get: PropertyName("payment_date")
    @set: PropertyName("payment_date")
    var paymentDate: Date? = null,

    ) : HomeSectionData()

@IgnoreExtraProperties
data class AssignedTaskForm(

    val id: String? = null,

    val title: String? = null,

    val type: String? = null,

    @get: PropertyName("start_time")
    @set: PropertyName("start_time")
    var startTime: Date? = null,

    @get: PropertyName("end_time")
    @set: PropertyName("end_time")
    var endTime: Date? = null,

    @get: PropertyName("is_submitted")
    @set: PropertyName("is_submitted")
    var isSubmitted: Boolean? = null,

    @get: PropertyName("is_checked")
    @set: PropertyName("is_checked")
    var isChecked: Boolean? = null,

    @get: PropertyName("subject_name")
    @set: PropertyName("subject_name")
    var subjectName: String? = null,

    val score: Int? = null,

    val answers: List<Answer>? = null,

    ) : ScoreSectionData()

@IgnoreExtraProperties
data class AttendedMeeting(

    val id: String? = null,

    var status: String? = null,

    @get: PropertyName("start_time")
    @set: PropertyName("start_time")
    var startTime: Date? = null,

    @get: PropertyName("end_time")
    @set: PropertyName("end_time")
    var endTime: Date? = null,

    @get: PropertyName("subject_name")
    @set: PropertyName("subject_name")
    var subjectName: String? = null
)

@IgnoreExtraProperties
data class Achievement(

    val title: String? = null,

    val description: String? = null,

    )

@IgnoreExtraProperties
data class Answer(

    @get: PropertyName("answer_text")
    @set: PropertyName("answer_text")
    var answerText: String? = null,

    val score: Int? = null,

    var images: List<String>? = null

)