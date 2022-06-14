package com.example.project_skripsi.module.student.main._sharing.agenda

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.ClassMeeting
import com.example.project_skripsi.core.model.local.HomeSectionData
import com.example.project_skripsi.databinding.ItemStHomeSectionItemBinding
import com.example.project_skripsi.module.student.main.home.view.adapter.ItemListener
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.helper.DateHelper

class StHomeMeetingViewHolder(private val binding: ItemStHomeSectionItemBinding, private val listener: ItemListener):
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
            btnResource.setOnClickListener {
                listener.onMaterialItemClicked(absoluteAdapterPosition)
            }
            btnClass.setOnClickListener {
                listener.onClassItemClicked(absoluteAdapterPosition, data)
            }
        }
    }
}