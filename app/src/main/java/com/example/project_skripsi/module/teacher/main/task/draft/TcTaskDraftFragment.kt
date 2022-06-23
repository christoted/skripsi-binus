package com.example.project_skripsi.module.teacher.main.task.draft

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
import com.example.project_skripsi.core.model.local.SubjectGroup
import com.example.project_skripsi.databinding.FragmentTcTaskDraftBinding
import com.example.project_skripsi.databinding.ViewRecyclerViewBinding
import com.example.project_skripsi.module.teacher.main.task.TcTaskViewModel
import com.example.project_skripsi.module.teacher.study_class.task.TaskViewHolder
import com.example.project_skripsi.utils.generic.ItemClickListener
import com.example.project_skripsi.utils.helper.UIHelper

class TcTaskDraftFragment : Fragment(), ItemClickListener {

    private lateinit var viewModel: TcTaskDraftViewModel
    private var _binding: FragmentTcTaskDraftBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[TcTaskDraftViewModel::class.java]
        _binding = FragmentTcTaskDraftBinding.inflate(inflater, container, false)

        binding.vpContainer.adapter = ScreenSlidePagerAdapter()
        binding.tabLayout.setupWithViewPager(binding.vpContainer)

        binding.imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }

        retrieveArgs()

        return binding.root
    }

    private fun retrieveArgs() {
        val args: TcTaskDraftFragmentArgs by navArgs()
        viewModel.setSubjectGroup(SubjectGroup(args.subjectName, args.gradeLevel))
        binding.tvTitle.text = ("Draf Formulir Kelas ${args.gradeLevel} - ${args.subjectName}")
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(itemId: String) {
        viewModel.getTaskFormType(itemId)?.let { type ->
            view?.findNavController()?.navigate(
                TcTaskDraftFragmentDirections.actionTcTaskDraftFragmentToTcAlterTaskFragment(
                    viewModel.currentSubjectGroup.subjectName,
                    viewModel.currentSubjectGroup.gradeLevel,
                    type,
                    itemId
                )
            )
        }
    }

    private inner class ScreenSlidePagerAdapter : PagerAdapter() {

        lateinit var layoutInflater: LayoutInflater
        private var examEmptyView: View? = null
        private var assignmentEmptyView: View? = null

        override fun getCount(): Int =
            TcTaskViewModel.tabCount

        override fun isViewFromObject(view: View, `object`: Any): Boolean =
            view == `object`

        override fun getPageTitle(position: Int): CharSequence =
            TcTaskViewModel.tabHeader[position]


        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = LayoutInflater.from(context)
            val bindingRV = ViewRecyclerViewBinding.inflate(layoutInflater, container, false)

            bindingRV.rvContainer.layoutManager = LinearLayoutManager(context)
            when (position) {
                TcTaskDraftViewModel.TAB_EXAM -> {
                    viewModel.examList.observe(viewLifecycleOwner, { list ->
                        examEmptyView?.let { bindingRV.llParent.removeView(it) }
                        if (list.isEmpty()) {
                            val emptyView = UIHelper.getEmptyList(
                                "Tidak ada ujian",
                                layoutInflater,
                                bindingRV.llParent
                            )
                            bindingRV.llParent.addView(emptyView)
                            examEmptyView = emptyView
                        }
                        bindingRV.rvContainer.adapter =
                            TaskViewHolder(list, this@TcTaskDraftFragment).getAdapter()
                    })
                }
                TcTaskDraftViewModel.TAB_ASSIGNMENT -> {
                    viewModel.assignmentList.observe(viewLifecycleOwner, { list ->
                        assignmentEmptyView?.let { bindingRV.llParent.removeView(it) }
                        if (list.isEmpty()) {
                            val emptyView = UIHelper.getEmptyList(
                                "Tidak ada tugas",
                                layoutInflater,
                                bindingRV.llParent
                            )
                            bindingRV.llParent.addView(emptyView)
                            assignmentEmptyView = emptyView
                        }
                        bindingRV.rvContainer.adapter =
                            TaskViewHolder(list, this@TcTaskDraftFragment).getAdapter()
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