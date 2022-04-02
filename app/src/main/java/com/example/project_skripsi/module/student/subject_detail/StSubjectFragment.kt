package com.example.project_skripsi.module.student.subject_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.FragmentStSubjectBinding
import com.example.project_skripsi.module.student.subject_detail.assignment.StSubjectAssignmentFragment
import com.example.project_skripsi.module.student.subject_detail.attendance.StSubjectAttendanceFragment
import com.example.project_skripsi.module.student.subject_detail.exam.StSubjectExamFragment
import com.example.project_skripsi.module.student.subject_detail.resource.StSubjectResourceFragment
import com.example.project_skripsi.module.student.task.StTaskViewModel
import com.example.project_skripsi.utils.generic.ItemClickListener
import com.google.android.material.tabs.TabLayoutMediator

class StSubjectFragment : Fragment(), ItemClickListener {

    private var _binding: FragmentStSubjectBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : StSubjectViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStSubjectBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[StSubjectViewModel::class.java]

        binding.viewPagerContainer.adapter = ScreenSlidePagerAdapter(activity!!, this)
        TabLayoutMediator(binding.tabLayout, binding.viewPagerContainer) { tab, position ->
            tab.text = StSubjectViewModel.tabHeader[position]
        }.attach()

        viewModel.teacher.observe(this, {
            with(binding) {
                tvTeacherName.text = it.name
                it.phoneNumber?.let { imvTeacherPhone.setImageResource(R.drawable.whatsapp) }
            }
        })

        retrieveArgs()

        return binding.root
    }

    private fun retrieveArgs() {
        val args: StSubjectFragmentArgs by navArgs()
        binding.subjectName.text = args.subjectName
        viewModel.setSubject(args.subjectName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity, private val taskFormListener: ItemClickListener) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = StSubjectViewModel.tabCount

        override fun createFragment(position: Int): Fragment =
            when (position) {
                0 -> StSubjectAttendanceFragment(viewModel)
                1 -> StSubjectResourceFragment(viewModel)
                2 -> StSubjectExamFragment(viewModel, taskFormListener)
                else -> StSubjectAssignmentFragment(viewModel, taskFormListener)
            }
    }

    override fun onItemClick(itemId: String) {
        val action = StSubjectFragmentDirections.actionStSubjectFragmentToStTaskActivity(itemId)
        action.navigationType = StTaskViewModel.NAVIGATION_FORM
        view?.findNavController()?.navigate(action)
    }
}