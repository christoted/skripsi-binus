package com.example.project_skripsi.core.model.firestore

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import java.util.*

@IgnoreExtraProperties
data class TaskForm(

    val title: String? = null,

    val type: String? = null,

    val startTime: Date? = null,

    val endTime: Date? = null,

    @get: PropertyName("subject_name")
    @set: PropertyName("subject_name")
    var subjectName: String? = null,

    val questions: List<Question>? = null,

    @get: PropertyName("assigned_classes")
    @set: PropertyName("assigned_classes")
    var assignedClasses: List<String>? = null,
): HomeSectionData()

@IgnoreExtraProperties
data class Question(

    val title: String? = null,

    val type: String? = null,

    val choices: List<String>? = null,

    @get: PropertyName("answer_key")
    @set: PropertyName("answer_key")
    var answerKey: Int? = null
)

