package com.example.project_skripsi.module.teacher._sharing.agenda

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.ClassMeeting
import com.example.project_skripsi.core.model.local.HomeSectionData
import com.example.project_skripsi.core.model.local.TeacherAgendaMeeting
import com.example.project_skripsi.core.model.local.TeacherAgendaTaskForm
import com.example.project_skripsi.databinding.ItemStHomeSectionItemBinding
import com.example.project_skripsi.databinding.ItemTcAgendaGeneralBinding
import com.example.project_skripsi.module.student.main.home.view.adapter.ItemListener
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
            btnResource.setOnClickListener { listener.onMaterialItemClicked(absoluteAdapterPosition) }
            btnClass.setOnClickListener { listener.onClassItemClicked(absoluteAdapterPosition, data) }
        }
    }
}