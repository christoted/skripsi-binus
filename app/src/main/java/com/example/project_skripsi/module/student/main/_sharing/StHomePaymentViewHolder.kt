package com.example.project_skripsi.module.student.main._sharing

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.databinding.ItemStHomeSectionItemBinding
import com.example.project_skripsi.databinding.ItemStHomeSectionPembayaranBinding
import com.example.project_skripsi.module.student.main.home.viewmodel.HomeItemJadwalKelas

class StHomePaymentViewHolder(private val binding: ItemStHomeSectionPembayaranBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        with(binding) {
//            val data = singleItem as HomeItemPembayaran
            jumlahTagihan.text = "Pembayaran"
        }
    }
}