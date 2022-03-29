package com.example.project_skripsi.module.student.subject_detail.assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentStSubjectAssignmentBinding
import com.example.project_skripsi.module.student.subject_detail.StSubjectFragment
import com.example.project_skripsi.module.student.subject_detail._sharing.TaskViewHolder
import com.example.project_skripsi.module.student.subject_detail.StSubjectViewModel
import com.example.project_skripsi.module.student.subject_detail._sharing.TaskFormListener

class StSubjectAssignmentFragment(private val viewModel: StSubjectViewModel, private val taskFormListener: TaskFormListener) : Fragment() {

    private var _binding: FragmentStSubjectAssignmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentStSubjectAssignmentBinding.inflate(inflater, container, false)

        binding.rvAssignment.layoutManager = LinearLayoutManager(context)
        viewModel.assignmentList.observe(viewLifecycleOwner, {
            binding.rvAssignment.adapter = TaskViewHolder(it, taskFormListener).getAdapter()
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}