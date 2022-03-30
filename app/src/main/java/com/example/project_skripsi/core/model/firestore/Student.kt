package com.example.project_skripsi.core.model.firestore

import com.example.project_skripsi.core.model.local.HomeSectionData
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import java.util.*

@IgnoreExtraProperties
data class Student(

    @DocumentId
    val id : String? = null,

    val nis: String? = null,

    val name: String? = null,

    @get: PropertyName("phone_number")
    @set: PropertyName("phone_number")
    var phoneNumber: String? = null,

    val age: Int? = null,

    val gender: String? = null,

    @get: PropertyName("study_class")
    @set: PropertyName("study_class")
    var studyClass: String? = null,

    val payments: List<Payment>? = null,

    @get: PropertyName("attended_meetings")
    @set: PropertyName("attended_meetings")
    var attendedMeetings: List<String>? = null,

    @get: PropertyName("completed_resources")
    @set: PropertyName("completed_resources")
    var completedResources: List<String>? = null,

    @get: PropertyName("assigned_exams")
    @set: PropertyName("assigned_exams")
    var assignedExams: List<AssignedTaskForm>? = null,

    @get: PropertyName("assigned_assignments")
    @set: PropertyName("assigned_assignments")
    var assignedAssignments: List<AssignedTaskForm>? = null,

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

): HomeSectionData()

@IgnoreExtraProperties
data class AssignedTaskForm(

    val id: String? = null,

    @get: PropertyName("task_checked")
    @set: PropertyName("task_checked")
    var taskChecked: Boolean? = null,

    val score: Int? = null,

    val answer: List<Any>? = null,

)

