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
import com.example.project_skripsi.core.model.local.TaskFormStatus
import com.example.project_skripsi.databinding.FragmentTcStudyClassTaskDetailBinding
import com.example.project_skripsi.databinding.StandardRecyclerViewBinding
import com.example.project_skripsi.module.student.task._sharing.TaskViewHolder
import com.example.project_skripsi.module.teacher.study_class.task.TcStudyClassTaskFragmentDirections
import com.example.project_skripsi.utils.generic.ItemClickListener
import com.example.project_skripsi.utils.helper.DateHelper


class TcStudyClassTaskDetailFragment : Fragment(), ItemClickListener {

    private lateinit var viewModel : TcStudyClassTaskDetailViewModel
    private var _binding: FragmentTcStudyClassTaskDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[TcStudyClassTaskDetailViewModel::class.java]
        _binding = FragmentTcStudyClassTaskDetailBinding.inflate(inflater, container, false)

        retrieveArgs()

        binding.btnPreviewForm.setOnClickListener {
            view?.findNavController()?.navigate(
                TcStudyClassTaskDetailFragmentDirections.actionTcStudyClassTaskDetailFragmentToTcPreviewTaskFormFragment(
                    viewModel.studyClassId, viewModel.taskFormId
                )
            )
        }

        viewModel.studyClass.observe(viewLifecycleOwner, {
            binding.tvClassName.text = it.name
        })

        viewModel.taskForm.observe(viewLifecycleOwner, {
            with(binding) {
                tvTaskTitle.text = it.title

                it.startTime?.let {
                    tvStartDate.text = DateHelper.getFormattedDateTime(DateHelper.DMY, it)
                    tvStartTime.text = DateHelper.getFormattedDateTime(DateHelper.hm, it)
                }

                it.endTime?.let {
                    tvEndDate.text = DateHelper.getFormattedDateTime(DateHelper.DMY, it)
                    tvEndTime.text = DateHelper.getFormattedDateTime(DateHelper.hm, it)
                }

                tvDuration.text = ("${TaskFormStatus.getDuration(it)} menit")
            }
        })

        binding.vpContainer.adapter = ScreenSlidePagerAdapter(this)
        binding.tabLayout.setupWithViewPager(binding.vpContainer)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs() {
        val args : TcStudyClassTaskDetailFragmentArgs by navArgs()
        viewModel.setData(args.studyClassId, args.subjectName, args.taskFormId)
        binding.tvSubjectName.text = args.subjectName
    }

    private inner class ScreenSlidePagerAdapter(private val listener: ItemClickListener) : PagerAdapter(){

        lateinit var layoutInflater: LayoutInflater

        override fun getCount(): Int =
            TcStudyClassTaskDetailViewModel.tabCount

        override fun isViewFromObject(view: View, `object`: Any): Boolean =
            view == `object`

        override fun getPageTitle(position: Int): CharSequence =
            TcStudyClassTaskDetailViewModel.tabHeader[position]


        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = LayoutInflater.from(context)
            val bindingRV = StandardRecyclerViewBinding.inflate(layoutInflater, container, false)

            bindingRV.rvContainer.layoutManager = LinearLayoutManager(context)
            when(position) {
                TcStudyClassTaskDetailViewModel.TASK_UNCHECKED -> {
                    viewModel.uncheckedList.observe(viewLifecycleOwner, {
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
                .actionTcStudyClassTaskDetailFragmentToTcAssessmentTaskFormFragment()
        )
    }
}