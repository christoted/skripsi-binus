package com.example.project_skripsi.module.student.main._sharing

import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.core.model.firestore.Payment
import com.example.project_skripsi.core.model.local.HomeSectionData
import com.example.project_skripsi.databinding.ItemStHomeSectionPembayaranBinding
import com.example.project_skripsi.utils.helper.CurrencyHelper

class StHomePaymentViewHolder(private val binding: ItemStHomeSectionPembayaranBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HomeSectionData) {
        val data = item as Payment
        with(binding) {
            judul.text = data.title
            jumlahTagihan.text = CurrencyHelper.toRupiah(data.nominal!!)
            rekening.text = data.accountNumber
        }
    }
}