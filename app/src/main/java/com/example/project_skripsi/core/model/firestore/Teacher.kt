package com.example.project_skripsi.core.model.firestore

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties
data class Teacher(

    @DocumentId
    val id : String? = null,

    val name: String? = null,

    @get: PropertyName("phone_number")
    @set: PropertyName("phone_number")
    var phoneNumber: String? = null,

    val gender: String? = null,

    @get: PropertyName("homeroom_class")
    @set: PropertyName("homeroom_class")
    var homeroomClass: String? = null,

    val school: String? = null,

    @get: PropertyName("teaching_groups")
    @set: PropertyName("teaching_groups")
    var teachingGroups: MutableList<TeachingGroup>? = null,

    @get: PropertyName("payments")
    @set: PropertyName("payments")
    var payments: MutableList<Payment>? = null,
)

@IgnoreExtraProperties
data class TeachingGroup(

    @get: PropertyName("subject_name")
    @set: PropertyName("subject_name")
    var subjectName: String? = null,

    @get: PropertyName("grade_level")
    @set: PropertyName("grade_level")
    var gradeLevel: Int? = null,

    @get: PropertyName("teaching_classes")
    @set: PropertyName("teaching_classes")
    var teaching_classes: List<String>? = null,

    @get: PropertyName("created_resources")
    @set: PropertyName("created_resources")
    var createdResources: MutableList<String>? = null,

    @get: PropertyName("created_exams")
    @set: PropertyName("created_exams")
    var createdExams: MutableList<String>? = null,

    @get: PropertyName("created_assignments")
    @set: PropertyName("created_assignments")
    var createdAssignments: MutableList<String>? = null,
)