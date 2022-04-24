package com.example.project_skripsi.module.parent.home.viewholder.agenda

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.Payment
import com.example.project_skripsi.core.model.local.HomeSectionData
import com.example.project_skripsi.core.model.local.ParentAgendaPayment
import com.example.project_skripsi.databinding.ItemPrHomePaymentBinding
import com.example.project_skripsi.databinding.ItemStHomeSectionPaymentBinding
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.helper.CurrencyHelper

class PrHomePaymentViewHolder(private val binding: ItemPrHomePaymentBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HomeSectionData) {
        val data = item as ParentAgendaPayment
        with(binding) {
            tvStudentName.text = data.studentName
            tvTitle.text = data.payment?.title
            tvNominal.text = CurrencyHelper.toRupiah(data.payment?.nominal!!)
            tvAccountNumber.text = ("BCA / ${data.payment?.accountNumber}")
            viewIndicator.setBackgroundColor(
                ResourcesCompat.getColor(App.resourses!!, R.color.indicator_payment, null))
        }
    }
}