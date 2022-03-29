package com.example.project_skripsi.module.student.main._sharing

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.databinding.ItemStHomeSectionItemBinding

class StHomeClassViewHolder(private val binding: ItemStHomeSectionItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        with(binding) {
//            val data = singleItem as HomeItemJadwalKelas
            title.text = "Kelas"
            btnMateri.setOnClickListener {
                Log.d("JADWAL KELAS", "bind: Materi")
//                listener.onMaterialItemClicked(absoluteAdapterPosition)
            }
            btnKelas.setOnClickListener {
                Log.d("JADWAL KELAS", "bind: Kelas")
//                listener.onClassItemClicked(absoluteAdapterPosition)
            }
        }
    }
}