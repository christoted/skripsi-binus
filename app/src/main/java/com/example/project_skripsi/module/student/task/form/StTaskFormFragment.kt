package com.example.project_skripsi.module.student.task.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.FragmentStTaskFormBinding
import com.example.project_skripsi.module.student.StMainActivity
import com.example.project_skripsi.utils.Constant.Companion.TASK_FORM_MC
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.helper.DateHelper
import com.example.project_skripsi.utils.helper.DateHelper.Companion.getDuration
import com.example.project_skripsi.utils.helper.DisplayHelper.Companion.getDurationDisplay
import com.example.project_skripsi.utils.service.alarm.AlarmService

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

                    viewModel.isViewOnly = it < DateHelper.getCurrentTime()
                    if (!viewModel.isViewOnly) {
                        btnTime.visibility = View.VISIBLE
                        btnSubmit.visibility = View.VISIBLE
                    }
                }
                val durationDis = getDuration(taskForm.startTime, taskForm.endTime)
                tvDuration.text = getDurationDisplay(durationDis.first, durationDis.second)
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
            val adapter = StFormAdapter(
                it,
                viewModel.curStudent.id,
                viewModel.taskFormId,
                (activity as StMainActivity),
                viewModel.isViewOnly
            )
            binding.rvQuestion.adapter = adapter

            if (viewModel.isViewOnly) {
//                binding.btnSubmit.visibility = View.GONE
//                binding.btnTime.visibility = View.GONE
                return@observe
            }

            binding.btnSubmit.setOnClickListener {
                submitAnswer(adapter)
                binding.btnSubmit.isEnabled = false
            }

            viewModel.timeLeft.observe(viewLifecycleOwner, { timer ->
                if (timer.forceSubmit) {
                    submitAnswer(adapter)
                    binding.btnSubmit.isEnabled = false
                } else binding.btnTime.text = ("${timer.hour} : ${timer.minute} : ${timer.second}")
                binding.btnTime.setBackgroundColor(
                    ResourcesCompat.getColor(
                        App.resourses!!,
                        if (timer.hour == 0L && timer.minute == 0L)
                            R.color.timer_alert
                        else
                            R.color.timer_normal, null
                    )
                )
            })
        })

        viewModel.isSubmitted.observe(viewLifecycleOwner, {
            if (it) view?.findNavController()?.popBackStack()
            binding.btnSubmit.isEnabled = true
        })

        viewModel.incompleteResource.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(
                    requireContext(),
                    "Materi \"${it.title}\" (${it.subjectName}) harus dibaca terlebih dahulu",
                    Toast.LENGTH_LONG
                ).show()
                view?.findNavController()?.popBackStack()
            }
        }

        viewModel.incompleteTask.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(
                    requireContext(),
                    "Tugas \"${it.title}\" (${it.subjectName}) harus dikerjakan terlebih dahulu",
                    Toast.LENGTH_LONG
                ).show()
                view?.findNavController()?.popBackStack()
            }
        }


        return binding.root
    }

    private fun submitAnswer(adapter: StFormAdapter) {
        viewModel.submitAnswer(adapter.questionList.mapIndexed { index, question ->
            val childView = binding.rvQuestion.getChildAt(index)
            Pair(
                when (question.type) {
                    TASK_FORM_MC -> {
                        val id =
                            childView.findViewById<RadioGroup>(R.id.choice_group).checkedRadioButtonId
                        if (id != -1) resources.getResourceEntryName(id).split("_")[1]
                        else "0"
                    }
                    else -> {
                        childView.findViewById<EditText>(R.id.edt_answer).text.toString()
                    }
                },
                adapter.imageList[index]
            )
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs() {
        val args: StTaskFormFragmentArgs by navArgs()
        viewModel.setTaskForm(args.taskFormId)
        AlarmService.inst.cancelAlarm(requireContext(), args.taskFormId)
    }
}