package com.example.project_skripsi.module.student.main.score.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.project_skripsi.module.student.main.score.view.fragment.StScoreContentFragment
import com.example.project_skripsi.module.student.main.score.viewmodel.StScoreViewModel

class StScoreViewPagerAdapter(fa: FragmentActivity, private val vm: StScoreViewModel): FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return StScoreViewModel.tabCount
    }

    override fun createFragment(position: Int): Fragment {
        return StScoreContentFragment(viewModel = vm)
    }
}