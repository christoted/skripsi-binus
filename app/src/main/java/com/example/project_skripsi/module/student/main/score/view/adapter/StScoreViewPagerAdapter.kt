package com.example.project_skripsi.module.student.main.score.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.project_skripsi.module.student.main.score.view.fragment.absensi.StScoreAbsensiFragment
import com.example.project_skripsi.module.student.main.score.view.fragment.nilai.StScoreContentFragment
import com.example.project_skripsi.module.student.main.score.view.fragment.pencapaian.StScorePencapainFragment
import com.example.project_skripsi.module.student.main.score.viewmodel.StScoreViewModel

class StScoreViewPagerAdapter(fa: FragmentActivity, private val vm: StScoreViewModel): FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return StScoreViewModel.tabCount
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> {
                return StScoreContentFragment(viewModel = vm)
            }
            1 -> {
                return StScoreAbsensiFragment(viewModel = vm)
            }
            2 -> {
                return StScorePencapainFragment(viewModel = vm)
            }
        }
        return StScoreContentFragment(viewModel = vm)
    }
}