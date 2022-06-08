package com.example.project_skripsi.module.parent.student_detail.exam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import com.example.project_skripsi.databinding.FragmentPrExamBinding
import com.example.project_skripsi.databinding.ViewEmptyListBinding
import com.example.project_skripsi.databinding.ViewRecyclerViewBinding
import com.example.project_skripsi.module.parent.student_detail._sharing.PrTaskViewHolder
import com.example.project_skripsi.module.student.task.exam.StTaskExamViewModel
import com.example.project_skripsi.utils.helper.UIHelper

class PrExamFragment : Fragment() {

    private lateinit var viewModel: PrExamViewModel
    private var _binding: FragmentPrExamBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[PrExamViewModel::class.java]
        _binding = FragmentPrExamBinding.inflate(inflater, container, false)

        binding.vpContainer.adapter = ScreenSlidePagerAdapter()
        binding.tabLayout.setupWithViewPager(binding.vpContainer)

        binding.imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }

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
            StTaskExamViewModel.tabCount

        override fun isViewFromObject(view: View, `object`: Any): Boolean =
            view == `object`

        override fun getPageTitle(position: Int): CharSequence =
            StTaskExamViewModel.tabHeader[position]


        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = LayoutInflater.from(context)
            val bindingRV = ViewRecyclerViewBinding.inflate(layoutInflater, container, false)

            bindingRV.rvContainer.layoutManager = LinearLayoutManager(context)
            when(position) {
                PrExamViewModel.EXAM_ONGOING -> {
                    viewModel.ongoingList.observe(viewLifecycleOwner, {
                        if (it.isEmpty()) {
                            bindingRV.llParent.addView(UIHelper.getEmptyList(
                                "Tidak ada ujian yang sedang berlangsung",
                                layoutInflater, container))
                        } else {
                            bindingRV.rvContainer.adapter = PrTaskViewHolder(it).getAdapter()
                        }
                    })
                }
                PrExamViewModel.EXAM_PAST -> {
                    viewModel.pastList.observe(viewLifecycleOwner, {
                        if (it.isEmpty()) {
                            bindingRV.llParent.addView(UIHelper.getEmptyList(
                                "Tidak ada ujian yang sudah selesai",
                                layoutInflater, container))
                        } else {
                            bindingRV.rvContainer.adapter = PrTaskViewHolder(it).getAdapter()
                        }
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
