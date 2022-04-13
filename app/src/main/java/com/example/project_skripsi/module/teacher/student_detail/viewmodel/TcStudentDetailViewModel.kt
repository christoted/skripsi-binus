package com.example.project_skripsi.module.teacher.student_detail.viewmodel

import androidx.lifecycle.ViewModel

class TcStudentDetailViewModel: ViewModel() {
    companion object {
        const val tabCount = 3
        val tabHeader = arrayOf("Nilai", "Absensi", "Pembayaran")
    }
}