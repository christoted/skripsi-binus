package com.example.project_skripsi.core.model.local

open class ScoreSectionData

data class ScoreMainSection(

    val subjectName: String,

    val mid_exam: Int?,

    val final_exam: Int?,

    val total_assignment: Int?,

    val total_score: Int?,

    val sectionItem: List<ScoreSectionData>
)