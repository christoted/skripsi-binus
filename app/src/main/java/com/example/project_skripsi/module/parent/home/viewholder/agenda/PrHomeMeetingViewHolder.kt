package com.example.project_skripsi.module.parent.home.viewholder.agenda

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.local.HomeSectionData
import com.example.project_skripsi.core.model.local.ParentAgendaMeeting
import com.example.project_skripsi.databinding.ItemPrHomeGeneralBinding
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.helper.DateHelper

class PrHomeMeetingViewHolder(private val binding: ItemPrHomeGeneralBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item : HomeSectionData) {
        val data = item as ParentAgendaMeeting
        with(binding) {
            tvStudentName.text = data.studentName
            tvSubjectName.text = data.attendedMeeting?.subjectName
            tvTime.text = ("${DateHelper.getFormattedDateTime(DateHelper.hm, data.attendedMeeting?.startTime)} - " +
                    "${DateHelper.getFormattedDateTime(DateHelper.hm, data.attendedMeeting?.endTime)}")
            tvLocation.text = ("Online")
            viewIndicator.setBackgroundColor(
                ResourcesCompat.getColor(App.resourses!!, R.color.indicator_meeting, null))
        }
    }
}