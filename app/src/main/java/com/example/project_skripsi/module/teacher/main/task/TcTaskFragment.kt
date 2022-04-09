package com.example.project_skripsi.module.teacher.main.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.FragmentTcTaskBinding
import com.example.project_skripsi.module.teacher.form.alter_task.TcAlterTaskViewModel
import com.example.project_skripsi.module.teacher.study_class.task.TaskViewHolder
import com.example.project_skripsi.utils.generic.ItemClickListener
import com.google.android.material.chip.Chip

class TcTaskFragment : Fragment(), ItemClickListener {

    private lateinit var viewModel : TcTaskViewModel
    private var _binding: FragmentTcTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[TcTaskViewModel::class.java]
        _binding = FragmentTcTaskBinding.inflate(inflater, container, false)

        viewModel.subjectGroupList.observe(viewLifecycleOwner, {
            binding.cgSubjectGroup.removeAllViews()
            var hasItem = false
            it.map { subjectGroup ->
                val chip = inflater.inflate(R.layout.standard_chip_choice, binding.cgSubjectGroup, false) as Chip
                chip.id = View.generateViewId()
                chip.text = ("${subjectGroup.subjectName} - ${subjectGroup.gradeLevel}")
                chip.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) viewModel.selectSubjectGroup(subjectGroup)
                }
                binding.cgSubjectGroup.addView(chip)
                if (!hasItem) {
                    chip.isChecked = true
                    viewModel.selectSubjectGroup(subjectGroup)
                    hasItem = true
                }
            }
        })

        binding.rvExam.layoutManager = LinearLayoutManager(context)
        viewModel.examList.observe(viewLifecycleOwner, {
            binding.rvExam.adapter = TaskViewHolder(it, this).getAdapter()
        })

        binding.rvAssignment.layoutManager = LinearLayoutManager(context)
        viewModel.assignmentList.observe(viewLifecycleOwner, {
            binding.rvAssignment.adapter = TaskViewHolder(it, this).getAdapter()
        })

        binding.btnAddExam.setOnClickListener{
            viewModel.currentSubjectGroup?.let {
                view?.findNavController()?.navigate(TcTaskFragmentDirections.actionTcTaskFragmentToTcAlterTaskFragment(
                    it.subjectName, it.gradeLevel, TcAlterTaskViewModel.TYPE_EXAM))
            }
        }

        binding.btnAddAssignment.setOnClickListener{
            viewModel.currentSubjectGroup?.let {
                view?.findNavController()?.navigate(TcTaskFragmentDirections.actionTcTaskFragmentToTcAlterTaskFragment(
                    it.subjectName, it.gradeLevel, TcAlterTaskViewModel.TYPE_ASSIGNMENT))
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(itemId: String) {
//        TODO("Not yet implemented")
    }
}