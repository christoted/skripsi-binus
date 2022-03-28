package com.example.project_skripsi.module.student.subject_detail.resource

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.core.model.firestore.Resource
import com.example.project_skripsi.databinding.ItemStSubjectResourceBinding

class StSubjectResourceAdapter(private val examList: List<Resource>) :
    RecyclerView.Adapter<StSubjectResourceAdapter.AttendanceViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AttendanceViewHolder =
        AttendanceViewHolder(
            ItemStSubjectResourceBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false))

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        holder.bind(examList[position], position)
    }

    override fun getItemCount() = examList.size

    class AttendanceViewHolder (private val binding : ItemStSubjectResourceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Resource, position: Int) {
            with(binding) {
                tvNumber.text = ("${position+1}.")
                tvName.text = item.title
                tvType.text = item.type
            }
        }
    }
}