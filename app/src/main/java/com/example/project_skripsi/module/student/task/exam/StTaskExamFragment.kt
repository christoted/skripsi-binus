package com.example.project_skripsi.module.student.task.exam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentStTaskExamBinding
import com.example.project_skripsi.module.student.task.TaskViewHolder

class StTaskExamFragment : Fragment() {

    private lateinit var viewModel: StTaskExamViewModel
    private var _binding: FragmentStTaskExamBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this)[StTaskExamViewModel::class.java]
        _binding = FragmentStTaskExamBinding.inflate(inflater, container, false)

        binding.rvExam.layoutManager = LinearLayoutManager(context)
        viewModel.list.observe(viewLifecycleOwner, {
            binding.rvExam.adapter = TaskViewHolder(TaskViewHolder.TYPE_EXAM,it).getAdapter()
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}