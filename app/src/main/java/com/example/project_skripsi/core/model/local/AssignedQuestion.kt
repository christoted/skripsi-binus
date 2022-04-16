package com.example.project_skripsi.core.model.local

import com.example.project_skripsi.core.model.firestore.Answer
import com.example.project_skripsi.core.model.firestore.Question
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class AssignedQuestion(

    val title: String? = null,

    val scoreWeight: Int? = null,

    val type: String? = null,

    val choices: List<String>? = null,

    val answerKey: String? = null,

    var answer: Answer? = null,
) {
    constructor(question: Question, answer: Answer?) : this (
        title = question.title,
        scoreWeight = question.scoreWeight,
        type = question.type,
        choices = question.choices,
        answerKey = question.answerKey,
        answer = answer
    )
}