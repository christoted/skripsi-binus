package com.example.project_skripsi.core.model.firestore

import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Announcement(

    val title: String? = null,

    val description: String? = null,

    val date: Date? = null,

)