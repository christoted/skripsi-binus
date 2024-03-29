package com.example.project_skripsi.module.student.subject_detail

import android.content.Intent
import android.net.Uri
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
import com.example.project_skripsi.utils.generic.ItemClickListener
import com.google.android.material.tabs.TabLayoutMediator

class StSubjectFragment : Fragment(), ItemClickListener {

    private var _binding: FragmentStSubjectBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: StSubjectViewModel

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

        viewModel.teacher.observe(this) { teacher ->
            with(binding) {
                tvTeacherName.text = teacher.name
                teacher.phoneNumber?.let {
                    imvTeacherPhone.setImageResource(R.drawable.whatsapp)
                    imvTeacherPhone.setOnClickListener {
                        goToWhatsApp(teacher.phoneNumber ?: "")
                    }
                }
            }
        }

        binding.imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }

        retrieveArgs()

        return binding.root
    }

    private fun goToWhatsApp(phoneNumber: String) {
        val url = "https://api.whatsapp.com/send/?phone=${phoneNumber}"
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun retrieveArgs() {
        val args: StSubjectFragmentArgs by navArgs()
        binding.tvSubjectName.text = args.subjectName
        viewModel.setSubject(args.subjectName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class ScreenSlidePagerAdapter(
        fa: FragmentActivity,
        private val taskFormListener: ItemClickListener
    ) : FragmentStateAdapter(fa) {
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
        view?.findNavController()?.navigate(
            StSubjectFragmentDirections.actionStSubjectFragmentToStTaskFormFragment(itemId)
        )
    }
}