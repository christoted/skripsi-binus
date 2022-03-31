package com.example.project_skripsi.module.student.main._sharing

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.Payment
import com.example.project_skripsi.core.model.local.HomeSectionData
import com.example.project_skripsi.databinding.ItemStHomeSectionPaymentBinding
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.helper.CurrencyHelper

class StHomePaymentViewHolder(private val binding: ItemStHomeSectionPaymentBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HomeSectionData) {
        val data = item as Payment
        with(binding) {
            viewIndicator.setBackgroundColor(
                ResourcesCompat.getColor(App.resourses!!, R.color.indicator_payment, null))
            tvTitle.text = data.title
            tvNominal.text = CurrencyHelper.toRupiah(data.nominal!!)
            tvAccountNumber.text = ("BCA / ${data.accountNumber}")
        }
    }
}