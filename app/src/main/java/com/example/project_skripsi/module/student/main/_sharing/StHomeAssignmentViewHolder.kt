package com.example.project_skripsi.module.student.main._sharing

import android.util.Log
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.databinding.ItemStHomeSectionItemBinding
import com.example.project_skripsi.module.student.main.home.viewmodel.HomeItemJadwalKelas
import com.example.project_skripsi.module.student.main.home.viewmodel.HomeItemTugas
import com.example.project_skripsi.module.student.main.home.viewmodel.HomeSectionData

class StHomeAssignmentViewHolder(private val binding: ItemStHomeSectionItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        with(binding) {
//            val data = singleItem as HomeItemTugas
            title.text = "Tugas"
            btnKelas.text = "Tugas"
            btnMateri.isVisible = false
            btnKelas.setOnClickListener {
//                listener.onAssignmentItemClicked(absoluteAdapterPosition)
            }
        }
    }
}