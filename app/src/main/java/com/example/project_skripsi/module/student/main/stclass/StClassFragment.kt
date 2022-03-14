package com.example.project_skripsi.module.student.main.stclass

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.project_skripsi.databinding.FragmentStClassBinding
import com.example.project_skripsi.databinding.FragmentStClassSubjectBinding
import com.example.project_skripsi.databinding.ItemStClassSubjectBinding
import com.example.project_skripsi.module.student.main.stclass.subject.StClassSubjectFragment
import com.example.project_skripsi.module.student.main.stclass.subject.SubjectAdapter
import com.example.project_skripsi.module.student.subject_detail.assignment.StSubjectAssignmentFragment
import com.example.project_skripsi.module.student.subject_detail.attendance.StSubjectAttendanceFragment
import com.example.project_skripsi.module.student.subject_detail.exam.StSubjectExamFragment
import com.example.project_skripsi.module.student.subject_detail.resource.StSubjectResourceFragment
import com.google.android.material.tabs.TabLayoutMediator
import android.view.View.MeasureSpec




class StClassFragment : Fragment() {

    private lateinit var viewModel: StClassViewModel
    private var _binding: FragmentStClassBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("12345", "cre");

        viewModel = ViewModelProvider(this).get(StClassViewModel::class.java)
        _binding = FragmentStClassBinding.inflate(inflater, container, false)

        viewModel.subjectList.observe(viewLifecycleOwner, {
            binding.viewpagerSubject.adapter = ScreenSlidePagerAdapter()
            binding.tablSubject.setupWithViewPager(binding.viewpagerSubject)
        })

        return binding.root
    }

    override fun onDestroyView() {
        Log.d("12345", "dest");
        super.onDestroyView()
        _binding = null
    }

    private inner class ScreenSlidePagerAdapter() : PagerAdapter(){

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