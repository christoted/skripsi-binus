package com.example.project_skripsi.module.student.task.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentStTaskFormBinding
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.helper.DateHelper

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
        binding.rvQuestion.isNestedScrollingEnabled = true
        viewModel.taskForm.observe(viewLifecycleOwner, { taskForm ->
            with(binding) {
                tvTitle.text = taskForm.title
                tvClassName.text = ""
                tvSubjectName.text = taskForm.subjectName
                tvStatus.text = ""
                taskForm.startTime?.let {
                    tvStartDate.text = DateHelper.getFormattedDateTime(DateHelper.DMY, it)
                    tvStartTime.text = DateHelper.getFormattedDateTime(DateHelper.hm, it)
                }
                taskForm.endTime?.let {
                    tvEndDate.text = DateHelper.getFormattedDateTime(DateHelper.DMY, it)
                    tvEndTime.text = DateHelper.getFormattedDateTime(DateHelper.hm, it)
                }
            }
        })

        viewModel.studyClass.observe(viewLifecycleOwner, { binding.tvClassName.text = it.name })
        viewModel.formStatus.observe(viewLifecycleOwner, {
            with(binding) {
                tvStatus.text = it.first
                tvStatus.setTextColor(ResourcesCompat.getColor(App.resourses!!, it.second, null))
            }
        })

        viewModel.questionList.observe(viewLifecycleOwner, { binding.rvQuestion.adapter = StFormAdapter(it) })

        return binding.root
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