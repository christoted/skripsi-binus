package com.example.project_skripsi.core.model.firestore

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties
data class Resource(

    @DocumentId
    val id : String? = null,

    val title: String? = null,

    @get: PropertyName("grade_level")
    @set: PropertyName("grade_level")
    var gradeLevel: Int? = null,

    val type: String? = null,

    val link: String? = null,

    @get: PropertyName("subject_name")
    @set: PropertyName("subject_name")
    var subjectName: String? = null,

    val prerequisites: List<String>? = null,

    @get: PropertyName("assigned_classes")
    @set: PropertyName("assigned_classes")
    var assignedClasses: List<String>? = null
)