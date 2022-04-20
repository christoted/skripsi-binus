package com.example.project_skripsi.module.parent.student_detail.assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import com.example.project_skripsi.databinding.FragmentPrAssignmentBinding
import com.example.project_skripsi.databinding.FragmentPrHomeBinding
import com.example.project_skripsi.databinding.ViewRecyclerViewBinding
import com.example.project_skripsi.module.parent.student_detail._sharing.PrTaskViewHolder
import com.example.project_skripsi.module.parent.student_detail.exam.PrExamFragmentArgs
import com.example.project_skripsi.module.student.task._sharing.TaskViewHolder
import com.example.project_skripsi.module.student.task.assignment.StTaskAssignmentViewModel

class PrAssignmentFragment : Fragment() {

    private lateinit var viewModel: PrAssignmentViewModel
    private var _binding: FragmentPrAssignmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[PrAssignmentViewModel::class.java]
        _binding = FragmentPrAssignmentBinding.inflate(inflater, container, false)

        binding.vpContainer.adapter = ScreenSlidePagerAdapter()
        binding.tabLayout.setupWithViewPager(binding.vpContainer)

        retrieveArgs()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs() {
        val args : PrExamFragmentArgs by navArgs()
        viewModel.setStudent(args.studentId)
    }

    private inner class ScreenSlidePagerAdapter : PagerAdapter(){

        lateinit var layoutInflater: LayoutInflater

        override fun getCount(): Int =
            StTaskAssignmentViewModel.tabCount

        override fun isViewFromObject(view: View, `object`: Any): Boolean =
            view == `object`

        override fun getPageTitle(position: Int): CharSequence =
            StTaskAssignmentViewModel.tabHeader[position]


        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = LayoutInflater.from(context)
            val bindingRV = ViewRecyclerViewBinding.inflate(layoutInflater, container, false)

            bindingRV.rvContainer.layoutManager = LinearLayoutManager(context)
            when(position) {
                StTaskAssignmentViewModel.ASSIGNMENT_ONGOING -> {
                    viewModel.ongoingList.observe(viewLifecycleOwner, {
                        bindingRV.rvContainer.adapter = PrTaskViewHolder(it).getAdapter()
                    })
                }
                StTaskAssignmentViewModel.ASSIGNMENT_PAST -> {
                    viewModel.pastList.observe(viewLifecycleOwner, {
                        bindingRV.rvContainer.adapter = PrTaskViewHolder(it).getAdapter()
                    })
                }
            }
            container.addView(bindingRV.root, 0)
            return bindingRV.root
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}