package com.example.project_skripsi.core.model.local

open class ScoreSectionData

data class ScoreMainSection(val subjectName: String, val mid_exam: Double, val exam: Double, val total_assignment: Double, val total_score: Double,val sectionItem: List<ScoreSectionData>)