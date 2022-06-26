package com.example.project_skripsi.module.teacher.form.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.FragmentTcPreviewTaskBinding
import com.example.project_skripsi.module.teacher.form.preview.TcPreviewTaskViewModel.Companion.GROUP_ASSIGNMENT
import com.example.project_skripsi.module.teacher.form.preview.TcPreviewTaskViewModel.Companion.GROUP_CLASS
import com.example.project_skripsi.module.teacher.form.preview.TcPreviewTaskViewModel.Companion.GROUP_RESOURCE
import com.example.project_skripsi.module.teacher.form.preview.TcPreviewTaskViewModel.Companion.mapFormType
import com.example.project_skripsi.utils.helper.DateHelper


class TcPreviewTaskFragment : Fragment() {

    private lateinit var viewModel: TcPreviewTaskViewModel
    private var _binding: FragmentTcPreviewTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[TcPreviewTaskViewModel::class.java]
        _binding = FragmentTcPreviewTaskBinding.inflate(inflater, container, false)

        viewModel.oldTaskForm.observe(viewLifecycleOwner, {
            with(binding) {
                tvTitle.text = it.title
                tvStartTime.text = ("${DateHelper.getFormattedDateTimeWithWeekDay(it.startTime)}, ${
                    DateHelper.getFormattedDateTime(
                        DateHelper.hm,
                        it.startTime
                    )
                }")
                tvEndTime.text = ("${DateHelper.getFormattedDateTimeWithWeekDay(it.endTime)}, ${
                    DateHelper.getFormattedDateTime(
                        DateHelper.hm,
                        it.endTime
                    )
                }")
                val duration = DateHelper.getDuration(it.startTime, it.endTime)
                tvDuration.text = ("${duration.first} ${duration.second}")
                tvFormType.text = mapFormType[it.type]
                tvQuestionCount.text = (it.questions?.size ?: 0).toString()

                it.id?.let { id ->
                    btnPreviewForm.setOnClickListener {
                        view?.findNavController()?.navigate(
                            TcPreviewTaskFragmentDirections.actionTcPreviewTaskFragmentToTcPreviewTaskFormFragment(
                                id
                            )
                        )
                    }
                }
            }
        })

        with(binding) {
            rvItemClass.layoutManager = LinearLayoutManager(context)
            rvItemClass.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

            rvItemPreqResource.layoutManager = LinearLayoutManager(context)
            rvItemPreqResource.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

            rvItemPreqAssignment.layoutManager = LinearLayoutManager(context)
            rvItemPreqAssignment.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

            viewModel.studyClasses.observe(viewLifecycleOwner) {
                llHeaderClass.setOnClickListener {
                    viewModel.isExpanded[GROUP_CLASS] = !viewModel.isExpanded[GROUP_CLASS]

                    if (viewModel.isExpanded[GROUP_CLASS]) {
                        TransitionManager.beginDelayedTransition(rvItemClass, AutoTransition())
                        dvClass.visibility = View.GONE
                    } else {
                        dvClass.visibility = View.VISIBLE
                    }

                    rvItemClass.isVisible = viewModel.isExpanded[GROUP_CLASS]
                    imvShowHideClass.setImageResource(
                        if (viewModel.isExpanded[GROUP_CLASS]) R.drawable.ic_baseline_arrow_drop_up_24
                        else R.drawable.ic_baseline_arrow_drop_down_24
                    )
                }
                rvItemClass.adapter = TaskPreviewViewHolder(it).getAdapter()
            }

            viewModel.prerequisiteResource.observe(viewLifecycleOwner) {
                if (it.isEmpty()) return@observe

                llHeaderResource.visibility = View.VISIBLE
                dvPreqResource.visibility = View.VISIBLE

                llHeaderResource.setOnClickListener {
                    viewModel.isExpanded[GROUP_RESOURCE] = !viewModel.isExpanded[GROUP_RESOURCE]

                    if (viewModel.isExpanded[GROUP_RESOURCE]) {
                        TransitionManager.beginDelayedTransition(rvItemPreqResource, AutoTransition())
                        dvPreqResource.visibility = View.GONE
                    } else {
                        dvPreqResource.visibility = View.VISIBLE
                    }

                    rvItemPreqResource.isVisible = viewModel.isExpanded[GROUP_RESOURCE]
                    imvShowHideResource.setImageResource(
                        if (viewModel.isExpanded[GROUP_RESOURCE]) R.drawable.ic_baseline_arrow_drop_up_24
                        else R.drawable.ic_baseline_arrow_drop_down_24
                    )
                }
                rvItemPreqResource.adapter = TaskPreviewViewHolder(it).getAdapter()
            }

            viewModel.prerequisiteTaskForm.observe(viewLifecycleOwner) {
                if (it.isEmpty()) return@observe
                llHeaderAssignment.visibility = View.VISIBLE
                dvPreqAssignment.visibility = View.VISIBLE

                llHeaderAssignment.setOnClickListener {
                    viewModel.isExpanded[GROUP_ASSIGNMENT] = !viewModel.isExpanded[GROUP_ASSIGNMENT]

                    if (viewModel.isExpanded[GROUP_ASSIGNMENT]) {
                        TransitionManager.beginDelayedTransition(rvItemPreqAssignment, AutoTransition())
                        dvPreqAssignment.visibility = View.GONE
                    } else {
                        dvPreqAssignment.visibility = View.VISIBLE
                    }

                    rvItemPreqAssignment.isVisible = viewModel.isExpanded[GROUP_ASSIGNMENT]
                    imvShowHideAssignment.setImageResource(
                        if (viewModel.isExpanded[GROUP_ASSIGNMENT]) R.drawable.ic_baseline_arrow_drop_up_24
                        else R.drawable.ic_baseline_arrow_drop_down_24
                    )
                }
                rvItemPreqAssignment.adapter = TaskPreviewViewHolder(it).getAdapter()
            }
        }

        binding.imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }

        retrieveArgs()
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs() {
        val args: TcPreviewTaskFragmentArgs by navArgs()
        binding.tvSubjectGroup.text = ("${args.subjectName} - ${args.gradeLevel}")
        viewModel.initData(args.formType, args.taskFormId)
    }

}