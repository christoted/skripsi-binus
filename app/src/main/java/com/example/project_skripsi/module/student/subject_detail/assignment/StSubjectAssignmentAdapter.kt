package com.example.project_skripsi.module.student.subject_detail.assignment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.databinding.ItemStSubjectAssignmentBinding
import com.example.project_skripsi.module.student.main.stclass.StClassFragmentDirections

class StSubjectAssignmentAdapter(private val assignmentList: List<String>) :
    RecyclerView.Adapter<StSubjectAssignmentAdapter.AssignmentViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AssignmentViewHolder =
        AssignmentViewHolder(
            ItemStSubjectAssignmentBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false))

    override fun onBindViewHolder(holder: AssignmentViewHolder, position: Int) {
        holder.bind(assignmentList[position])
    }

    override fun getItemCount() = assignmentList.size

    class AssignmentViewHolder ( private val binding : ItemStSubjectAssignmentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(assignment: String) {
            with(binding) {
                tvName.text = assignment
            }
        }
    }
}