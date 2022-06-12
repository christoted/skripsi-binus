package com.example.project_skripsi.core.model.firestore

import com.example.project_skripsi.core.model.local.HomeSectionData
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import java.util.*

@IgnoreExtraProperties
data class TaskForm(

    @DocumentId
    val id : String? = null,

    val title: String? = null,

    @get: PropertyName("grade_level")
    @set: PropertyName("grade_level")
    var gradeLevel: Int? = null,

    val type: String? = null,

    val startTime: Date? = null,

    val endTime: Date? = null,

    val location: String? = null,

    @get: PropertyName("subject_name")
    @set: PropertyName("subject_name")
    var subjectName: String? = null,

    val questions: List<Question>? = null,

    @get: PropertyName("assigned_classes")
    @set: PropertyName("assigned_classes")
    var assignedClasses: List<String>? = null,

    @get: PropertyName("prerequisite_resources")
    @set: PropertyName("prerequisite_resources")
    var prerequisiteResources: List<String>? = null,

    @get: PropertyName("prerequisite_task_forms")
    @set: PropertyName("prerequisite_task_forms")
    var prerequisiteTaskForms: List<String>? = null,

    @get: PropertyName("is_finalized")
    @set: PropertyName("is_finalized")
    var isFinalized: Boolean? = null,

    ): HomeSectionData()

@IgnoreExtraProperties
data class Question(

    val title: String? = null,

    val type: String? = null,

    @get: PropertyName("score_weight")
    @set: PropertyName("score_weight")
    var scoreWeight: Int? = null,

    val choices: List<String>? = null,

    @get: PropertyName("answer_key")
    @set: PropertyName("answer_key")
    var answerKey: String? = null,
)

