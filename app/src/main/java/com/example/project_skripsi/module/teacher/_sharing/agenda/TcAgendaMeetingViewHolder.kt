package com.example.project_skripsi.module.teacher._sharing.agenda

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.local.HomeSectionData
import com.example.project_skripsi.core.model.local.TeacherAgendaMeeting
import com.example.project_skripsi.databinding.ItemTcAgendaGeneralBinding
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.helper.DateHelper

class TcAgendaMeetingViewHolder(private val binding: ItemTcAgendaGeneralBinding, private val listener: TcAgendaItemListener):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item : HomeSectionData) {
        val data = item as TeacherAgendaMeeting
        with(binding) {
            viewIndicator.setBackgroundColor(
                ResourcesCompat.getColor(App.resourses!!, R.color.indicator_meeting, null))
            tvTitle.text = data.classMeeting.subjectName
            tvClassName.text = data.studyClassName
            tvLocation.text = data.classMeeting.location
            tvTime.text = ("${DateHelper.getFormattedDateTime(DateHelper.hm, data.classMeeting.startTime!!)} - " +
                    "${DateHelper.getFormattedDateTime(DateHelper.hm, data.classMeeting.endTime!!)}")

            if (data.classMeeting.meetingResource.isNullOrEmpty()) {
                btnResource.isEnabled = false
            } else {
                btnResource.setOnClickListener { listener.onResourceItemClicked(data.classMeeting.meetingResource!!) }
            }

            if (DateHelper.getCurrentTime() > data.classMeeting.endTime) {
                btnClass.isEnabled = false
            } else {
                btnClass.setOnClickListener { listener.onClassItemClicked(data) }
            }
        }
    }
}