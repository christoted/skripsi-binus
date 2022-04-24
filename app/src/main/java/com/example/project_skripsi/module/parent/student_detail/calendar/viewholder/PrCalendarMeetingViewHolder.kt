package com.example.project_skripsi.module.parent.student_detail.calendar.viewholder

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.ClassMeeting
import com.example.project_skripsi.core.model.local.HomeSectionData
import com.example.project_skripsi.databinding.ItemPrCalendarGeneralBinding
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.helper.DateHelper

class PrCalendarMeetingViewHolder(private val binding: ItemPrCalendarGeneralBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item : HomeSectionData) {
        val data = item as ClassMeeting
        with(binding) {
            viewIndicator.setBackgroundColor(
                ResourcesCompat.getColor(App.resourses!!, R.color.indicator_meeting, null))
            tvTitle.text = data.subjectName
            tvLocation.text = data.location
            tvTime.text = ("${DateHelper.getFormattedDateTime(DateHelper.hm, data.startTime!!)} - " +
                    "${DateHelper.getFormattedDateTime(DateHelper.hm, data.endTime!!)}")
        }
    }
}