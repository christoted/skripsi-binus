package com.example.project_skripsi.module.student.main._sharing

import android.util.Log
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.databinding.ItemStHomeSectionItemBinding
import com.example.project_skripsi.module.student.main.home.viewmodel.HomeItemJadwalKelas
import com.example.project_skripsi.module.student.main.home.viewmodel.HomeItemUjian

class StHomeExamViewHolder(private val binding: ItemStHomeSectionItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        with(binding) {
//            val data = singleItem as HomeItemUjian
            title.text = "Ujian"
            btnKelas.text = "Ujian"
            btnMateri.isVisible = false
            btnKelas.setOnClickListener {
//                listener.onExamItemClicked(absoluteAdapterPosition)
            }
        }
    }
}