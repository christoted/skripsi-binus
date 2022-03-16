package com.example.project_skripsi.module.student.subject_detail.attendance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.databinding.ItemStSubjectAttendanceBinding

class StSubjectAttendanceAdapter(private val assignmentList: List<String>) :
    RecyclerView.Adapter<StSubjectAttendanceAdapter.AttendanceViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AttendanceViewHolder =
        AttendanceViewHolder(
            ItemStSubjectAttendanceBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false))

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        holder.bind(assignmentList[position])
    }

    override fun getItemCount() = assignmentList.size

    class AttendanceViewHolder (private val binding : ItemStSubjectAttendanceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(attendance: String) {
            with(binding) {
                tvStatus.text = attendance
            }
        }
    }
}