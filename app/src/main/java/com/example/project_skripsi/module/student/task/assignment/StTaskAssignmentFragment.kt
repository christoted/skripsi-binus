package com.example.project_skripsi.module.student.task.assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentStTaskAssignmentBinding
import com.example.project_skripsi.module.student.task.TaskViewHolder

class StTaskAssignmentFragment : Fragment() {

    private lateinit var viewModel: StTaskAssignmentViewModel
    private var _binding: FragmentStTaskAssignmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this)[StTaskAssignmentViewModel::class.java]
        _binding = FragmentStTaskAssignmentBinding.inflate(inflater, container, false)

        binding.rvAssignment.layoutManager = LinearLayoutManager(context)
        viewModel.list.observe(viewLifecycleOwner, {
            binding.rvAssignment.adapter = TaskViewHolder(TaskViewHolder.TYPE_ASSIGNMENT,it).getAdapter()
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}