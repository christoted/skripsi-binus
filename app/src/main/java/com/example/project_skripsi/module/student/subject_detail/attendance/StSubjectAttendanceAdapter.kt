package com.example.project_skripsi.module.student.subject_detail.attendance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.core.model.local.Attendance
import com.example.project_skripsi.databinding.ItemStSubjectAttendanceBinding
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.helper.DateHelper
import com.example.project_skripsi.utils.helper.TextHelper

class StSubjectAttendanceAdapter(private val assignmentList: List<Attendance>) :
    RecyclerView.Adapter<StSubjectAttendanceAdapter.AttendanceViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AttendanceViewHolder =
        AttendanceViewHolder(
            ItemStSubjectAttendanceBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false
            )
        )

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        holder.bind(assignmentList[position])
    }

    override fun getItemCount() = assignmentList.size

    class AttendanceViewHolder(private val binding: ItemStSubjectAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Attendance) {
            with(binding) {

                with(item) {
                    tvNumber.text = ("${absoluteAdapterPosition + 1}.")
                    startTime?.let {
                        tvDate.text = DateHelper.getFormattedDateTime(DateHelper.DMY, it)
                    }
                    statusColor?.let {
                        tvStatus.setTextColor(ResourcesCompat.getColor(App.res!!, it, null))
                    }
                }

                tvTime.text = ("${DateHelper.getFormattedDateTime(DateHelper.hm, item.startTime!!)}"
                        + " - ${DateHelper.getFormattedDateTime(DateHelper.hm, item.endTime!!)}")
                tvStatus.text = item.status?.let { TextHelper.capitalize(it) }

            }
        }
    }
}