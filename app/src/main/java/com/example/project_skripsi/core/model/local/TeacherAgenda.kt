package com.example.project_skripsi.core.model.local

import com.example.project_skripsi.core.model.firestore.ClassMeeting
import com.example.project_skripsi.core.model.firestore.TaskForm

data class TeacherAgendaMeeting (

    val studyClassName: String,

    val classMeeting: ClassMeeting,
) : HomeSectionData()

data class TeacherAgendaTaskForm (

    val studyClassId: String,

    val studyClassName: String,

    val taskForm: TaskForm,
) : HomeSectionData()

data class ClassIdSubject (

    val studyClassId: String,

    val subjectName: String,
)

data class ClassTaskFormId (

    val studyClassId: String,

    val studyClassName: String,

    val taskFormId: String,
)