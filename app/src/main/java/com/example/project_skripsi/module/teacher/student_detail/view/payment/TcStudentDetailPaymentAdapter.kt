package com.example.project_skripsi.module.teacher.student_detail.view.payment

import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.R
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
            val contentAdapter = TcStudentDetailPaymentContentViewHolder(item.payments,
                if (item.title == "Jatuh Tempo") R.color.payment_late
                else R.color.payment_incoming
            )
            Log.d("987", "getAdapter: " + item.payments)
            with(view) {
                sectionTitle.text = item.title
                sectionTitleCount.text = item.payments.size.toString()
                rvPaymentListContent.layoutManager = LinearLayoutManager(root.context)
                rvPaymentListContent.adapter = contentAdapter.getAdapter()
            }
        }
        return adapter
    }
}