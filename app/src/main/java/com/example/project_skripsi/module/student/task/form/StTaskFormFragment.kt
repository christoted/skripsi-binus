package com.example.project_skripsi.module.student.task.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.FragmentStTaskFormBinding
import com.example.project_skripsi.utils.Constant.Companion.TASK_FORM_MC
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.helper.DateHelper
import com.example.project_skripsi.utils.helper.DisplayHelper

class StTaskFormFragment : Fragment() {

    private lateinit var viewModel: StTaskFormViewModel
    private var _binding: FragmentStTaskFormBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[StTaskFormViewModel::class.java]
        _binding = FragmentStTaskFormBinding.inflate(inflater, container, false)

        retrieveArgs()

        binding.rvQuestion.layoutManager = LinearLayoutManager(context)
        viewModel.taskForm.observe(viewLifecycleOwner, { taskForm ->
            with(binding) {
                tvTitle.text = taskForm.title
                tvSubjectName.text = taskForm.subjectName
                taskForm.startTime?.let {
                    tvStartDate.text = DateHelper.getFormattedDateTime(DateHelper.DMY, it)
                    tvStartTime.text = DateHelper.getFormattedDateTime(DateHelper.hm, it)
                }
                taskForm.endTime?.let {
                    tvEndDate.text = DateHelper.getFormattedDateTime(DateHelper.DMY, it)
                    tvEndTime.text = DateHelper.getFormattedDateTime(DateHelper.hm, it)
                }
                val durationDis = DateHelper.getDuration(taskForm.startTime, taskForm.endTime)
                tvDuration.text = DisplayHelper.getDurationDisplay(durationDis.first, durationDis.second)
            }
        })

        viewModel.studyClass.observe(viewLifecycleOwner, { binding.tvClassName.text = it.name })
        viewModel.formStatus.observe(viewLifecycleOwner, {
            with(binding) {
                tvStatus.text = it.first
                tvStatus.setTextColor(ResourcesCompat.getColor(App.resourses!!, it.second, null))
            }
        })

        viewModel.questionList.observe(viewLifecycleOwner, {
            val adapter = StFormAdapter(it)
            binding.rvQuestion.adapter = adapter
            binding.btnSubmit.setOnClickListener { submitAnswer(adapter) }

            viewModel.timerLeft.observe(viewLifecycleOwner, { timer ->
                if (timer.forceSubmit) submitAnswer(adapter)
                else binding.btnTime.text = ("${timer.hour} : ${timer.minute} : ${timer.second}")
            })
        })

        viewModel.isSubmitted.observe(viewLifecycleOwner, {
            if (it) view?.findNavController()?.popBackStack()?.let { pop ->
                if (!pop) activity?.finish()
            }
        })

        return binding.root
    }

    private fun submitAnswer(adapter: StFormAdapter) {
        viewModel.submitAnswer(adapter.questionList.mapIndexed { index, question ->
            val childView = binding.rvQuestion.getChildAt(index)
            when (question.type) {
                TASK_FORM_MC -> {
                    val id = childView.findViewById<RadioGroup>(R.id.choice_group).checkedRadioButtonId
                    if (id != -1) resources.getResourceEntryName(id).split("_")[1]
                    else "0"
                }
                else -> {
                    childView.findViewById<EditText>(R.id.edt_answer).text.toString()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs(){
        val args: StTaskFormFragmentArgs by navArgs()
        viewModel.setTaskForm(args.taskFormId)
    }
}