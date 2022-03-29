package com.example.project_skripsi.core.model.local

import com.example.project_skripsi.core.model.firestore.AssignedTaskForm
import com.example.project_skripsi.core.model.firestore.Question
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties
data class AssignedQuestion(

    val title: String? = null,

    val type: String? = null,

    val choices: List<String>? = null,

    val answerKey: Int? = null,

    var answer: Any? = null,
) {
    constructor(question: Question, answer: Any?) : this (
        title = question.title,
        type = question.type,
        choices = question.choices,
        answerKey = question.answerKey,
        answer = answer
    )
}