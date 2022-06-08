package com.example.project_skripsi.module.teacher.study_class.task

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
import com.example.project_skripsi.databinding.FragmentTcStudyClassTaskBinding
import com.example.project_skripsi.databinding.ViewRecyclerViewBinding
import com.example.project_skripsi.utils.generic.ItemClickListener
import com.example.project_skripsi.utils.helper.UIHelper


class TcStudyClassTaskFragment : Fragment(), ItemClickListener {

    private lateinit var viewModel : TcStudyClassTaskViewModel
    private var _binding: FragmentTcStudyClassTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[TcStudyClassTaskViewModel::class.java]
        _binding = FragmentTcStudyClassTaskBinding.inflate(inflater, container, false)

        retrieveArgs()

        binding.vpContainer.adapter = ScreenSlidePagerAdapter()
        binding.tabLayout.setupWithViewPager(binding.vpContainer)

        viewModel.studyClass.observe(viewLifecycleOwner, {
            binding.tvClassName.text = it.name
        })

        binding.imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs() {
        val args : TcStudyClassTaskFragmentArgs by navArgs()
        viewModel.setClassAndSubject(args.studyClassId, args.subjectName)
        binding.tvSubjectName.text = args.subjectName
    }

    override fun onItemClick(itemId: String) {
        view?.findNavController()?.navigate(TcStudyClassTaskFragmentDirections
            .actionTcStudyClassTaskFragmentToTcStudyClassTaskDetailFragment(
                viewModel.studyClassId, viewModel.subjectName, itemId
            )
        )
    }

    private inner class ScreenSlidePagerAdapter : PagerAdapter(){

        lateinit var layoutInflater: LayoutInflater

        override fun getCount(): Int =
            TcStudyClassTaskViewModel.tabCount

        override fun isViewFromObject(view: View, `object`: Any): Boolean =
            view == `object`

        override fun getPageTitle(position: Int): CharSequence =
            TcStudyClassTaskViewModel.tabHeader[position]


        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = LayoutInflater.from(context)
            val bindingRV = ViewRecyclerViewBinding.inflate(layoutInflater, container, false)

            bindingRV.rvContainer.layoutManager = LinearLayoutManager(context)
            when(position) {
                TcStudyClassTaskViewModel.TAB_EXAM -> {
                    viewModel.examList.observe(viewLifecycleOwner, {
                        if (it.isEmpty()) {
                            bindingRV.llParent.addView(
                                UIHelper.getEmptyList("Tidak ada ujian", layoutInflater, bindingRV.llParent)
                            )
                        } else {
                            bindingRV.rvContainer.adapter = TaskViewHolder(it, this@TcStudyClassTaskFragment).getAdapter()
                        }
                    })
                }
                TcStudyClassTaskViewModel.TAB_ASSIGNMENT -> {
                    viewModel.assignmentList.observe(viewLifecycleOwner, {
                        if (it.isEmpty()) {
                            bindingRV.llParent.addView(
                                UIHelper.getEmptyList("Tidak ada tugas", layoutInflater, bindingRV.llParent)
                            )
                        } else {
                            bindingRV.rvContainer.adapter = TaskViewHolder(it, this@TcStudyClassTaskFragment).getAdapter()
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