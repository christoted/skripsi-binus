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

    @get: PropertyName("teaching_subjects")
    @set: PropertyName("teaching_subjects")
    var teachingSubjects: List<TeachingSubject>? = null,

    @get: PropertyName("created_resources")
    @set: PropertyName("created_resources")
    var createdResources: List<String>? = null,

    @get: PropertyName("created_forms")
    @set: PropertyName("created_forms")
    var createdForms: List<String>? = null,
)

@IgnoreExtraProperties
data class TeachingSubject(

    @get: PropertyName("subject_name")
    @set: PropertyName("subject_name")
    var subjectName: String? = null,

    @get: PropertyName("teaching_class")
    @set: PropertyName("teaching_class")
    var teaching_class: List<String>? = null,
)