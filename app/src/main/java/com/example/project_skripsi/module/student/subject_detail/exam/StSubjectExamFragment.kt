package com.example.project_skripsi.module.student.subject_detail.exam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentStSubjectExamBinding
import com.example.project_skripsi.module.student.subject_detail._sharing.TaskViewHolder
import com.example.project_skripsi.module.student.subject_detail.StSubjectViewModel
import com.example.project_skripsi.utils.generic.ItemClickListener

class StSubjectExamFragment(private val viewModel: StSubjectViewModel, private val listener: ItemClickListener) : Fragment() {

    private var _binding: FragmentStSubjectExamBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentStSubjectExamBinding.inflate(inflater, container, false)
        binding.rvExam.layoutManager = LinearLayoutManager(context)
        viewModel.examList.observe(viewLifecycleOwner, {
            binding.rvExam.adapter = TaskViewHolder(it, listener).getAdapter()
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}