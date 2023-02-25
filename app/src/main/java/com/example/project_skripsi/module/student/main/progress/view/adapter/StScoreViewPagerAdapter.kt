package com.example.project_skripsi.module.student.main.progress.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.project_skripsi.module.student.main.progress.view.fragment.achievement.StProgressAchievementFragment
import com.example.project_skripsi.module.student.main.progress.view.fragment.attendance.StProgressAttendanceFragment
import com.example.project_skripsi.module.student.main.progress.view.fragment.score.StProgressScoreFragment
import com.example.project_skripsi.module.student.main.progress.viewmodel.StScoreViewModel

class StScoreViewPagerAdapter(fa: FragmentActivity, private val vm: StScoreViewModel) :
    FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return StScoreViewModel.tabCount
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                return StProgressScoreFragment(viewModel = vm)
            }
            1 -> {
                return StProgressAttendanceFragment(viewModel = vm)
            }
            2 -> {
                return StProgressAchievementFragment(viewModel = vm)
            }
        }
        return StProgressScoreFragment(viewModel = vm)
    }
}