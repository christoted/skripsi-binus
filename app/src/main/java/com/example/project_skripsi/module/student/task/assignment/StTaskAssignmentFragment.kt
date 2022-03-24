package com.example.project_skripsi.module.student.task.assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import com.example.project_skripsi.databinding.FragmentDummyRvBinding
import com.example.project_skripsi.databinding.FragmentStTaskAssignmentBinding
import com.example.project_skripsi.module.student.task._sharing.TaskViewHolder

class StTaskAssignmentFragment : Fragment() {

    private lateinit var viewModel: StTaskAssignmentViewModel
    private var _binding: FragmentStTaskAssignmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this)[StTaskAssignmentViewModel::class.java]
        _binding = FragmentStTaskAssignmentBinding.inflate(inflater, container, false)

        viewModel.list.observe(viewLifecycleOwner, { refreshAdapter() })
        refreshAdapter()

        return binding.root
    }

    private fun refreshAdapter(){
        binding.vpContainer.adapter = ScreenSlidePagerAdapter()
        binding.tabLayout.setupWithViewPager(binding.vpContainer)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            val binding2 = FragmentDummyRvBinding.inflate(layoutInflater, container, false)

            binding2.rvContainer.layoutManager = LinearLayoutManager(context)
            binding2.rvContainer.adapter = TaskViewHolder(TaskViewHolder.TYPE_ASSIGNMENT,viewModel.getAssignmentList(position)).getAdapter()

            container.addView(binding2.root, 0)
            return binding2.root
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}