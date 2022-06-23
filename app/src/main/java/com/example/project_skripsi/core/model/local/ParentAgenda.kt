package com.example.project_skripsi.core.model.local

import com.example.project_skripsi.core.model.firestore.AssignedTaskForm
import com.example.project_skripsi.core.model.firestore.AttendedMeeting
import com.example.project_skripsi.core.model.firestore.Payment

data class ParentAgendaMeeting(

    val studentName: String?,

    val attendedMeeting: AttendedMeeting?,
) : HomeSectionData()

data class ParentAgendaTaskForm(

    val studentName: String?,

    val assignedTaskForm: AssignedTaskForm?,

    ) : HomeSectionData()

data class ParentAgendaPayment(

    val studentName: String?,

    val payment: Payment?,

    ) : HomeSectionData()
