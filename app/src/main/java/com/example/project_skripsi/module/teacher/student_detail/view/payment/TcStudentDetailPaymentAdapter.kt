package com.example.project_skripsi.module.teacher.student_detail.view.payment

import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.local.TcStudentDetailPaymentSection
import com.example.project_skripsi.databinding.ItemTcStudentDetailPaymentBinding
import com.example.project_skripsi.utils.generic.GenericAdapter
import com.example.project_skripsi.utils.helper.UIHelper

class TcStudentDetailPaymentAdapter(private val dataset: List<TcStudentDetailPaymentSection>) {

    private val isExpanded = BooleanArray(dataset.size){true}
    private val hasAddEmptyView = BooleanArray(dataset.size){false}

    fun getAdapter(): GenericAdapter<TcStudentDetailPaymentSection> {
        val adapter = GenericAdapter(dataset)
        adapter.expressionOnCreateViewHolder = {
            ItemTcStudentDetailPaymentBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item, viewBinding, viewHolder ->
            val view = viewBinding as ItemTcStudentDetailPaymentBinding
            with(view) {
                sectionTitle.text = item.title
                sectionTitleCount.text = item.payments.size.toString()

                with(sectionItemsRecyclerView) {
                    sectionItemsRecyclerView.layoutManager = LinearLayoutManager(context)
                    sectionItemsRecyclerView.adapter = TcStudentDetailPaymentContentViewHolder(item.payments,
                        if (item.title == "Jatuh Tempo") R.color.payment_late
                        else R.color.payment_incoming
                    ).getAdapter()


                    if (item.payments.isEmpty()) {
                        if (!hasAddEmptyView[viewHolder.absoluteAdapterPosition]) {
                            llCollapseGroup.addView(
                                UIHelper.getEmptyList(
                                    if (item.title == "Jatuh Tempo") "Tidak ada pembayaran jatuh tempo"
                                    else "Tidak ada pembayaran mendatang",
                                    LayoutInflater.from(root.context), llCollapseGroup
                                )
                            )
                            hasAddEmptyView[viewHolder.absoluteAdapterPosition] = true
                        }
                    }
                }

                llCollapseGroup.isVisible = isExpanded[viewHolder.absoluteAdapterPosition]
                llHeader.setOnClickListener {
                    isExpanded[viewHolder.absoluteAdapterPosition] = !isExpanded[viewHolder.absoluteAdapterPosition]

                    TransitionManager.beginDelayedTransition(llCollapseGroup, AutoTransition())
                    llCollapseGroup.isVisible = isExpanded[viewHolder.absoluteAdapterPosition]
                    imvShowHide.setImageResource(
                        if (isExpanded[viewHolder.absoluteAdapterPosition]) R.drawable.ic_baseline_arrow_drop_up_24
                        else R.drawable.ic_baseline_arrow_drop_down_24
                    )
                }
            }

        }
        return adapter
    }
}