package com.example.project_skripsi.utils

class Constant {

    companion object {
        const val SECTION_MEETING = "Jadwal Kelas"
        const val SECTION_EXAM = "Ujian"
        const val SECTION_ASSIGNMENT = "Tugas"
        const val SECTION_PAYMENT = "Pembayaran"
        const val SECTION_ANNOUNCEMENT = "Pengumuman"


        const val TASK_TYPE_MID_EXAM = "ujian_tengah_semester"
        const val TASK_TYPE_FINAL_EXAM = "ujian_akhir_semester"
        const val TASK_TYPE_ASSIGNMENT = "tugas"
        fun isExam(type: String?) = (type == TASK_TYPE_MID_EXAM || type == TASK_TYPE_FINAL_EXAM)

        const val TASK_FORM_ESSAY = "essai"
        const val TASK_FORM_MC = "pilihan berganda"

        const val ATTENDANCE_ATTEND = "hadir"
        const val ATTENDANCE_SICK = "sakit"
        const val ATTENDANCE_LEAVE = "izin"
        const val ATTENDANCE_ALPHA = "alfa"

        // Notification
//        const val NOTIF_CLASS_MEETING = "Meeting"
//        const val NOTIF_EXAM= "Exam"
//        const val NOTIF_ASSIGNMENT = "Assignment"

        const val MID_EXAM_WEIGHT = 40
        const val FINAL_EXAM_WEIGHT = 40
        const val ASSIGNMENT_WEIGHT = 20

    }


}