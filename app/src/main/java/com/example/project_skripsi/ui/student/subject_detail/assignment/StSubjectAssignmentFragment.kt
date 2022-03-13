package com.example.project_skripsi.ui.student.subject_detail.assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.project_skripsi.databinding.FragmentStSubjectAssignmentBinding

class StSubjectAssignmentFragment : Fragment() {

    private lateinit var viewModel: StSubjectAssignmentViewModel
    private var _binding: FragmentStSubjectAssignmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(StSubjectAssignmentViewModel::class.java)
        _binding = FragmentStSubjectAssignmentBinding.inflate(inflater, container, false)

        viewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textHome.text = it
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}