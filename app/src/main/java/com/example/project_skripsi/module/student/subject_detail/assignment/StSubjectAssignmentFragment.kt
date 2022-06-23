package com.example.project_skripsi.module.student.subject_detail.assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentStSubjectAssignmentBinding
import com.example.project_skripsi.databinding.ViewEmptyListBinding
import com.example.project_skripsi.module.student.subject_detail.StSubjectViewModel
import com.example.project_skripsi.module.student.subject_detail._sharing.TaskViewHolder
import com.example.project_skripsi.utils.generic.ItemClickListener

class StSubjectAssignmentFragment(
    private val viewModel: StSubjectViewModel,
    private val listener: ItemClickListener
) : Fragment() {

    private var _binding: FragmentStSubjectAssignmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStSubjectAssignmentBinding.inflate(inflater, container, false)

        binding.rvAssignment.layoutManager = LinearLayoutManager(context)
        viewModel.assignmentList.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                val emptyView =
                    ViewEmptyListBinding.inflate(layoutInflater, binding.llParent, false)
                emptyView.tvEmpty.text = ("Tidak ada tugas")
                binding.llParent.addView(emptyView.root)
            } else {
                binding.rvAssignment.adapter = TaskViewHolder(it, listener).getAdapter()
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}