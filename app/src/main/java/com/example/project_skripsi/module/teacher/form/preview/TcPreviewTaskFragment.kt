package com.example.project_skripsi.module.teacher.form.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.project_skripsi.databinding.FragmentTcPreviewTaskBinding
import com.example.project_skripsi.module.teacher.form.alter.TcAlterTaskViewModel
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
                tvPreqResource.text = (it.prerequisiteResources?.size ?: 0).toString()
                tvPreqAssignment.text = (it.prerequisiteTaskForms?.size ?: 0).toString()
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
        viewModel.initData(args.subjectName, args.gradeLevel, args.formType, args.taskFormId)
        if (args.formType == TcAlterTaskViewModel.TYPE_EXAM) {
            binding.trPreqResource.visibility = View.GONE
            binding.trPreqAssignment.visibility = View.GONE
        }
    }

}