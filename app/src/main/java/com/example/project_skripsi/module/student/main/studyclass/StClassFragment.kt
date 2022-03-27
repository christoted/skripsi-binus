package com.example.project_skripsi.module.student.main.studyclass

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.PagerAdapter
import com.example.project_skripsi.databinding.FragmentStClassBinding
import com.example.project_skripsi.databinding.FragmentStClassSubjectBinding
import com.example.project_skripsi.module.student.task.StTaskViewModel
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs


class StClassFragment : Fragment() {

    private lateinit var viewModel: StClassViewModel
    private var _binding: FragmentStClassBinding? = null
    private val binding get() = _binding!!

    private var isExpanded = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this)[StClassViewModel::class.java]
        _binding = FragmentStClassBinding.inflate(inflater, container, false)

        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                isExpanded = false
                Handler(Looper.getMainLooper()).postDelayed({
                    if (!isExpanded) binding.collapseLayout.title = "XII - IPA - 2"
                }, 750)
            } else {
                binding.collapseLayout.title = ""
                isExpanded = true
            }
        })

        binding.btnAssignment.setOnClickListener{ view ->
            val toTaskActivity = StClassFragmentDirections.actionNavigationClassToStTaskActivity()
            toTaskActivity.navigationType = StTaskViewModel.NAVIGATION_ASSIGNMENT
            view.findNavController().navigate(toTaskActivity)
        }

        binding.btnExam.setOnClickListener{ view ->
            val toTaskActivity = StClassFragmentDirections.actionNavigationClassToStTaskActivity()
            toTaskActivity.navigationType = StTaskViewModel.NAVIGATION_EXAM
            view.findNavController().navigate(toTaskActivity)
        }

        viewModel.subjectList.observe(viewLifecycleOwner, {
            binding.viewpagerSubject.adapter = ScreenSlidePagerAdapter()
            binding.tablSubject.setupWithViewPager(binding.viewpagerSubject)
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class ScreenSlidePagerAdapter : PagerAdapter(){

        lateinit var layoutInflater: LayoutInflater

        override fun getCount(): Int =
            viewModel.getSubjectPageCount()

        override fun isViewFromObject(view: View, `object`: Any): Boolean =
            view == `object`

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = LayoutInflater.from(context)
            val binding2 = FragmentStClassSubjectBinding.inflate(layoutInflater, container, false)

            binding2.rvSubjectContainer.layoutManager = GridLayoutManager(context, 4)
            binding2.rvSubjectContainer.adapter = SubjectAdapter(viewModel.getSubjects(position))

            container.addView(binding2.root, 0)
            return binding2.root
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}