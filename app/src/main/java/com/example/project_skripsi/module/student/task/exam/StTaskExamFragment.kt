package com.example.project_skripsi.module.student.task.exam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import com.example.project_skripsi.databinding.FragmentStTaskExamBinding
import com.example.project_skripsi.databinding.StandardRecyclerViewBinding
import com.example.project_skripsi.module.student.task._sharing.TaskViewHolder

class StTaskExamFragment : Fragment() {

    private lateinit var viewModel: StTaskExamViewModel
    private var _binding: FragmentStTaskExamBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this)[StTaskExamViewModel::class.java]
        _binding = FragmentStTaskExamBinding.inflate(inflater, container, false)

        binding.vpContainer.adapter = ScreenSlidePagerAdapter()
        binding.tabLayout.setupWithViewPager(binding.vpContainer)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class ScreenSlidePagerAdapter : PagerAdapter(){

        lateinit var layoutInflater: LayoutInflater

        override fun getCount(): Int =
            StTaskExamViewModel.tabCount

        override fun isViewFromObject(view: View, `object`: Any): Boolean =
            view == `object`

        override fun getPageTitle(position: Int): CharSequence =
            StTaskExamViewModel.tabHeader[position]


        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = LayoutInflater.from(context)
            val bindingRV = StandardRecyclerViewBinding.inflate(layoutInflater, container, false)

            bindingRV.rvContainer.layoutManager = LinearLayoutManager(context)
            when(position) {
                StTaskExamViewModel.EXAM_ONGOING -> {
                    viewModel.ongoingList.observe(viewLifecycleOwner, {
                        bindingRV.rvContainer.adapter = TaskViewHolder(TaskViewHolder.TYPE_EXAM, it).getAdapter()
                    })
                }
                StTaskExamViewModel.EXAM_PAST -> {
                    viewModel.pastList.observe(viewLifecycleOwner, {
                        bindingRV.rvContainer.adapter = TaskViewHolder(TaskViewHolder.TYPE_EXAM, it).getAdapter()
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

