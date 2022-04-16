package com.example.project_skripsi.core.model.local

import com.example.project_skripsi.core.model.firestore.Payment

data class TcStudentDetailPaymentSection (
    val title: String,
    var payments: List<Payment> )