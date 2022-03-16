package com.example.project_skripsi.module.student.subject_detail.assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentStSubjectAssignmentBinding
import com.example.project_skripsi.module.student.subject_detail.StSubjectViewModel

class StSubjectAssignmentFragment(private val viewModel: StSubjectViewModel) : Fragment() {

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
            binding.rvAssignment.adapter = StSubjectAssignmentAdapter(it)
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}