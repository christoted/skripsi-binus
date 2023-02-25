package com.example.project_skripsi.module.teacher.study_class.task_detail

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
import com.example.project_skripsi.databinding.FragmentTcStudyClassTaskDetailBinding
import com.example.project_skripsi.databinding.ViewRecyclerViewBinding
import com.example.project_skripsi.utils.generic.ItemClickListener
import com.example.project_skripsi.utils.helper.UIHelper


class TcStudyClassTaskDetailFragment : Fragment(), ItemClickListener {

    private lateinit var viewModel: TcStudyClassTaskDetailViewModel
    private var _binding: FragmentTcStudyClassTaskDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[TcStudyClassTaskDetailViewModel::class.java]
        _binding = FragmentTcStudyClassTaskDetailBinding.inflate(inflater, container, false)

        viewModel.taskForm.observe(viewLifecycleOwner, { binding.tvHeader.text = it.title })
        viewModel.studyClass.observe(viewLifecycleOwner, {
            binding.tvHeaderClass.text = it.name
        })

        binding.vpContainer.adapter = ScreenSlidePagerAdapter(this)
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
        val args: TcStudyClassTaskDetailFragmentArgs by navArgs()
        viewModel.setData(args.studyClassId, args.subjectName, args.taskFormId)
    }

    private inner class ScreenSlidePagerAdapter(private val listener: ItemClickListener) :
        PagerAdapter() {

        lateinit var layoutInflater: LayoutInflater
        var unCheckedEmptyView: View? = null
        var checkedEmptyView: View? = null

        override fun getCount(): Int =
            TcStudyClassTaskDetailViewModel.tabCount

        override fun isViewFromObject(view: View, `object`: Any): Boolean =
            view == `object`

        override fun getPageTitle(position: Int): CharSequence =
            TcStudyClassTaskDetailViewModel.tabHeader[position]


        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = LayoutInflater.from(context)
            val bindingRV = ViewRecyclerViewBinding.inflate(layoutInflater, container, false)

            bindingRV.rvContainer.layoutManager = LinearLayoutManager(context)
            when (position) {
                TcStudyClassTaskDetailViewModel.TASK_UNCHECKED -> {
                    viewModel.uncheckedList.observe(viewLifecycleOwner, {
                        unCheckedEmptyView?.let { bindingRV.llParent.removeView(unCheckedEmptyView) }
                        if (it.isEmpty()) {
                            unCheckedEmptyView = UIHelper.getEmptyList(
                                "Tidak ada form yang belum dikoreksi",
                                layoutInflater,
                                bindingRV.llParent
                            )
                            bindingRV.llParent.addView(unCheckedEmptyView)
                        }
                        bindingRV.rvContainer.adapter = TaskAssessmentViewHolder(
                            TcStudyClassTaskDetailViewModel.TASK_UNCHECKED,
                            viewModel,
                            it,
                            listener
                        ).getAdapter()
                    })
                }
                TcStudyClassTaskDetailViewModel.TASK_CHECKED -> {
                    viewModel.checkedList.observe(viewLifecycleOwner, {
                        checkedEmptyView?.let { bindingRV.llParent.removeView(checkedEmptyView) }
                        if (it.isEmpty()) {
                            checkedEmptyView = UIHelper.getEmptyList(
                                "Tidak ada form yang sudah dikoreksi",
                                layoutInflater,
                                bindingRV.llParent
                            )
                            bindingRV.llParent.addView(checkedEmptyView)
                        }
                        bindingRV.rvContainer.adapter = TaskAssessmentViewHolder(
                            TcStudyClassTaskDetailViewModel.TASK_CHECKED,
                            viewModel,
                            it,
                            listener
                        ).getAdapter()
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

    override fun onItemClick(itemId: String) {
        view?.findNavController()?.navigate(
            TcStudyClassTaskDetailFragmentDirections
                .actionTcStudyClassTaskDetailFragmentToTcAssessmentTaskFormFragment(
                    itemId,
                    viewModel.taskFormId
                )
        )
    }
}