package com.example.project_skripsi.core.model.firestore

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties
data class Resource(

    val title: String? = null,

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