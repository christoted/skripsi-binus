package com.example.project_skripsi.module.teacher._sharing.agenda

import com.example.project_skripsi.core.model.local.TeacherAgendaMeeting

interface TcAgendaItemListener {
    fun onTaskFormItemClicked(taskFormId: String, studyClassId: String, subjectName: String)
    fun onClassItemClicked(Position: Int, agendaMeeting: TeacherAgendaMeeting)
    fun onMaterialItemClicked(Position: Int)
}