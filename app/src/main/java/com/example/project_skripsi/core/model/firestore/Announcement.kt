package com.example.project_skripsi.core.model.firestore

import com.example.project_skripsi.core.model.local.HomeSectionData
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Announcement(

    @DocumentId
    val id: String? = null,

    val title: String? = null,

    val description: String? = null,

    val date: Date? = null,

    ) : HomeSectionData()