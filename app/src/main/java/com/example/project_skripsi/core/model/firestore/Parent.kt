package com.example.project_skripsi.core.model.firestore

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties
data class Parent(

    @DocumentId
    val id: String? = null,

    val name: String? = null,

    @get: PropertyName("phone_number")
    @set: PropertyName("phone_number")
    var phoneNumber: String? = null,

    val children: List<String>? = null,

    )