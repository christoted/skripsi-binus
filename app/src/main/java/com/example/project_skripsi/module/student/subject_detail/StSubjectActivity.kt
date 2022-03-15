package com.example.project_skripsi.module.student.subject_detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.project_skripsi.databinding.ActivityStSubjectBinding
import com.example.project_skripsi.module.student.subject_detail.assignment.StSubjectAssignmentFragment
import com.example.project_skripsi.module.student.subject_detail.attendance.StSubjectAttendanceFragment
import com.example.project_skripsi.module.student.subject_detail.exam.StSubjectExamFragment
import com.example.project_skripsi.module.student.subject_detail.resource.StSubjectResourceFragment
import com.google.android.material.tabs.TabLayoutMediator

class StSubjectActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStSubjectBinding
    private lateinit var viewModel : StSubjectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStSubjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[StSubjectViewModel::class.java]
        supportActionBar?.hide()

        binding.viewPagerContainer.adapter = ScreenSlidePagerAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.viewPagerContainer) { tab, position ->
            tab.text = StSubjectViewModel.tabHeader[position]
        }.attach()

        retrieveArgs()
    }

    private fun retrieveArgs(){
        val args: StSubjectActivityArgs by navArgs()
        binding.textTitle.text = args.subjectName
        viewModel.getSubjectData(args.subjectName)
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = StSubjectViewModel.tabCount

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> StSubjectAttendanceFragment()
                1 -> StSubjectResourceFragment()
                2 -> StSubjectExamFragment()
                else -> StSubjectAssignmentFragment()
            }
        }
    }
}