package com.example.project_skripsi.module.teacher._sharing.agenda

interface TcAgendaItemListener {
    fun onTaskFormItemClicked(taskFormId: String, studyClassId: String, subjectName: String)
    fun onClassItemClicked(Position: Int)
    fun onMaterialItemClicked(Position: Int)
}