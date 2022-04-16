package com.example.project_skripsi.module.teacher.form.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentTcPreviewTaskFormBinding
import com.example.project_skripsi.databinding.FragmentTemplateBinding
import com.example.project_skripsi.module.student.task.form.StFormAdapter
import com.example.project_skripsi.module.student.task.form.StTaskFormFragmentArgs
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.helper.DateHelper

class TcPreviewTaskFormFragment : Fragment() {

    private lateinit var viewModel: TcPreviewTaskFormViewModel
    private var _binding: FragmentTcPreviewTaskFormBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[TcPreviewTaskFormViewModel::class.java]
        _binding = FragmentTcPreviewTaskFormBinding.inflate(inflater, container, false)

        retrieveArgs()

        binding.rvQuestion.layoutManager = LinearLayoutManager(context)
        binding.rvQuestion.isNestedScrollingEnabled = true
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
                taskForm.questions?.let {
                    rvQuestion.adapter = TcPreviewFormAdapter(it)
                }
            }
        })

        viewModel.studyClass.observe(viewLifecycleOwner, { binding.tvClassName.text = it.name })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs(){
        val args: TcPreviewTaskFormFragmentArgs by navArgs()
        viewModel.setTaskForm(args.studyClassId, args.taskFormId)
    }
}