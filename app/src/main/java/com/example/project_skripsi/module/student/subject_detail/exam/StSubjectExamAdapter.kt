package com.example.project_skripsi.module.student.subject_detail.exam

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.databinding.ItemStSubjectExamBinding

class StSubjectExamAdapter(private val examList: List<String>) :
    RecyclerView.Adapter<StSubjectExamAdapter.AttendanceViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AttendanceViewHolder =
        AttendanceViewHolder(
            ItemStSubjectExamBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false))

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        holder.bind(examList[position])
    }

    override fun getItemCount() = examList.size

    class AttendanceViewHolder (private val binding : ItemStSubjectExamBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(exam: String) {
            with(binding) {
                tvName.text = exam
            }
        }
    }
}