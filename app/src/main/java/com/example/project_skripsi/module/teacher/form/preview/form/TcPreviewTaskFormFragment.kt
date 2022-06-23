package com.example.project_skripsi.module.teacher.form.preview.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentTcPreviewTaskFormBinding
import com.example.project_skripsi.utils.helper.DateHelper
import com.example.project_skripsi.utils.helper.DateHelper.Companion.getDuration
import com.example.project_skripsi.utils.helper.DateHelper.Companion.getFormattedDateTime
import com.example.project_skripsi.utils.helper.DisplayHelper.Companion.getDurationDisplay

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

        binding.rvQuestion.layoutManager = LinearLayoutManager(context)
        binding.rvQuestion.isNestedScrollingEnabled = true
        viewModel.taskForm.observe(viewLifecycleOwner, { taskForm ->
            with(binding) {
                tvTitle.text = taskForm.title
                tvSubjectName.text = taskForm.subjectName
                taskForm.startTime?.let {
                    tvStartDate.text = getFormattedDateTime(DateHelper.DMY, it)
                    tvStartTime.text = getFormattedDateTime(DateHelper.hm, it)
                }
                taskForm.endTime?.let {
                    tvEndDate.text = getFormattedDateTime(DateHelper.DMY, it)
                    tvEndTime.text = getFormattedDateTime(DateHelper.hm, it)
                }
                taskForm.questions?.let { rvQuestion.adapter = TcPreviewFormAdapter(it) }
                val durationDis = getDuration(taskForm.startTime, taskForm.endTime)
                tvDuration.text = getDurationDisplay(durationDis.first, durationDis.second)
            }
        })

        retrieveArgs()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs() {
        val args: TcPreviewTaskFormFragmentArgs by navArgs()
        viewModel.setTaskForm(args.taskFormId)
    }
}