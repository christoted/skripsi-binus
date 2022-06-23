package com.example.project_skripsi.module.teacher.student_detail.view.payment

import android.view.LayoutInflater
import androidx.core.content.res.ResourcesCompat
import com.example.project_skripsi.core.model.firestore.Payment
import com.example.project_skripsi.databinding.ItemStPaymentVariantBinding
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.generic.GenericAdapter
import com.example.project_skripsi.utils.helper.CurrencyHelper
import com.example.project_skripsi.utils.helper.DateHelper

class TcStudentDetailPaymentContentViewHolder(private val dataset: List<Payment>, private val indicatorColor: Int) {
    fun getAdapter(): GenericAdapter<Payment> {
        val genericAdapter = GenericAdapter(dataset)
        genericAdapter.expressionOnCreateViewHolder = {
            ItemStPaymentVariantBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        genericAdapter.expressionViewHolderBinding = { item, viewBinding, _ ->
            val view = viewBinding as ItemStPaymentVariantBinding
            with(view) {
                tvTitle.text = item.title
                tvDate.text =
                    item.paymentDeadline?.let { DateHelper.getFormattedDateTime(DateHelper.DMY, it) }
                tvNominal.text = CurrencyHelper.toRupiah(item.nominal?:0)
                view.viewIndicator.setBackgroundColor(ResourcesCompat.getColor(App.resourses!!, indicatorColor, null))
            }

        }
        return genericAdapter
    }
}