package com.example.project_skripsi.module.teacher.student_detail.view.payment

import android.view.LayoutInflater
import com.example.project_skripsi.core.model.firestore.Payment
import com.example.project_skripsi.databinding.ItemTcStudentDetailPaymentBinding
import com.example.project_skripsi.databinding.ItemTcStudentDetailPaymentContentBinding
import com.example.project_skripsi.utils.generic.GenericAdapter
import com.example.project_skripsi.utils.helper.DateHelper

class TcStudentDetailPaymentContentViewHolder(private val dataset: List<Payment>) {
    fun getAdapter(): GenericAdapter<Payment> {
        val genericAdapter = GenericAdapter(dataset)
        genericAdapter.expressionOnCreateViewHolder = {
            ItemTcStudentDetailPaymentContentBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        genericAdapter.expressionViewHolderBinding = { item, viewBinding, _ ->
            val view = viewBinding as ItemTcStudentDetailPaymentContentBinding
            with(view) {
                paymentTitle.text = item.title
                paymentDeadline.text =
                    item.paymentDeadline?.let { DateHelper.getFormattedDateTime(DateHelper.DMY, it) }
                paymentAmount.text = item.nominal.toString()
            }

        }
        return genericAdapter
    }
}