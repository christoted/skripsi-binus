package com.example.project_skripsi.module.student.main._sharing

import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.core.model.firestore.Announcement
import com.example.project_skripsi.core.model.local.HomeSectionData
import com.example.project_skripsi.databinding.ItemStHomeSectionPengumumanBinding

class StHomeAnnouncementViewHolder(private val binding: ItemStHomeSectionPengumumanBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HomeSectionData) {
        val data = item as Announcement
        with(binding) {
            judul.text = data.title
            deskripsi.text = data.description
        }
    }
}