package com.example.project_skripsi.module.student.main._sharing.agenda

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.Announcement
import com.example.project_skripsi.core.model.local.HomeSectionData
import com.example.project_skripsi.databinding.ItemStHomeSectionAnnouncementBinding
import com.example.project_skripsi.utils.app.App

class StHomeAnnouncementViewHolder(private val binding: ItemStHomeSectionAnnouncementBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HomeSectionData) {
        val data = item as Announcement
        with(binding) {
            viewIndicator.setBackgroundColor(
                ResourcesCompat.getColor(App.resourses!!, R.color.indicator_announcement, null))
            tvTitle.text = data.title
            tvDescription.text = data.description
        }
    }
}