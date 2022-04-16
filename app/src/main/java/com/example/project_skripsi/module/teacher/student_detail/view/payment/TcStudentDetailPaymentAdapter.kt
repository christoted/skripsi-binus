package com.example.project_skripsi.module.teacher.student_detail.view.payment

import android.view.LayoutInflater
import com.example.project_skripsi.core.model.local.TcStudentDetailPaymentSection
import com.example.project_skripsi.databinding.ItemTcStudentDetailPaymentBinding
import com.example.project_skripsi.utils.generic.GenericAdapter

class TcStudentDetailPaymentAdapter(private val dataset: List<TcStudentDetailPaymentSection>) {
    fun getAdapter(): GenericAdapter<TcStudentDetailPaymentSection> {
        val adapter = GenericAdapter(dataset)
        adapter.expressionOnCreateViewHolder = {
            ItemTcStudentDetailPaymentBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item, viewBinding, _ ->
            val view = viewBinding as ItemTcStudentDetailPaymentBinding
            val contentAdapter = TcStudentDetailPaymentContentViewHolder(item.payments)
            with(view) {
                paymentDeadlineTitle.text = item.title
                rvPaymentListContent.adapter = contentAdapter.getAdapter()
            }
        }
        return adapter
    }
}