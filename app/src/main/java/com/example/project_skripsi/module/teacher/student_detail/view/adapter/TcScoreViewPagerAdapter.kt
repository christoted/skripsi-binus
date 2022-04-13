package com.example.project_skripsi.module.teacher.student_detail.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.project_skripsi.module.teacher.student_detail.view.attendance.TcStudentDetailAttendance
import com.example.project_skripsi.module.teacher.student_detail.view.payment.TcStudentDetailPayment
import com.example.project_skripsi.module.teacher.student_detail.view.score.TcStudentDetailScore
import com.example.project_skripsi.module.teacher.student_detail.viewmodel.TcStudentDetailViewModel

class TcScoreViewPagerAdapter(fa: FragmentActivity,
    private val viewModel: TcStudentDetailViewModel): FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return TcStudentDetailViewModel.tabCount
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> {
                TcStudentDetailScore()
            }
            1 -> {
                TcStudentDetailAttendance
            }
            2 -> {
                TcStudentDetailPayment()
            }
        }
        return TcStudentDetailPayment()
    }

}