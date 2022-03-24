package com.example.project_skripsi.module.student.main._sharing

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.databinding.ItemStHomeSectionItemBinding
import com.example.project_skripsi.databinding.ItemStHomeSectionPengumumanBinding
import com.example.project_skripsi.module.student.main.calendar.DayEvent
import com.example.project_skripsi.module.student.main.home.viewmodel.HomeItemJadwalKelas
import com.example.project_skripsi.module.student.main.home.viewmodel.HomeItemPengumuman
import com.example.project_skripsi.module.student.main.home.viewmodel.HomeSectionData

class StHomeAnnouncementViewHolder(private val binding: ItemStHomeSectionPengumumanBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        with(binding) {
//            val data = singleItem as HomeItemPengumuman
            judul.text = "Pengumuman"
        }
    }
}