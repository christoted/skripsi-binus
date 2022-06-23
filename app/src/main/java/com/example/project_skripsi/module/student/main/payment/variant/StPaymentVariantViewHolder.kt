package com.example.project_skripsi.module.student.main.payment.variant

import android.view.LayoutInflater
import androidx.core.content.res.ResourcesCompat
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.Payment
import com.example.project_skripsi.databinding.ItemStPaymentVariantBinding
import com.example.project_skripsi.module.parent.student_detail.payment.variant.PrPaymentVariantViewHolder
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.generic.GenericAdapter
import com.example.project_skripsi.utils.helper.CurrencyHelper
import com.example.project_skripsi.utils.helper.DateHelper

class StPaymentVariantViewHolder(private val taskType : Int, private val dataSet : List<Payment>) {

    companion object {
        const val TYPE_UPCOMING = 0
        const val TYPE_UNPAID = 1
        const val TYPE_PAID = 2
    }

    fun getAdapter(): GenericAdapter<Payment> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemStPaymentVariantBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item,viewBinding,_ ->
            val view = viewBinding as ItemStPaymentVariantBinding
            with(view) {
                tvTitle.text = item.title
                tvNominal.text = CurrencyHelper.toRupiah(item.nominal!!)
                tvDate.text = DateHelper.getFormattedDateTime(DateHelper.DMY, item.paymentDeadline!!)
            }
            when(taskType){
                PrPaymentVariantViewHolder.TYPE_PAID -> {
                    view.tvDateTitle.text = ("Terbayar pada")
                    view.viewIndicator.setBackgroundColor(ResourcesCompat.getColor(App.resourses!!, R.color.payment_complete, null))
                }
                PrPaymentVariantViewHolder.TYPE_UNPAID -> {
                    view.viewIndicator.setBackgroundColor(ResourcesCompat.getColor(App.resourses!!, R.color.payment_late, null))
                }
                PrPaymentVariantViewHolder.TYPE_UPCOMING -> {
                    view.viewIndicator.setBackgroundColor(ResourcesCompat.getColor(App.resourses!!, R.color.payment_incoming, null))
                }
            }
        }
        return adapter
    }

}