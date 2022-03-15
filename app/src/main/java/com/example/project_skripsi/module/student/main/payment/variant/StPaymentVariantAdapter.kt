package com.example.project_skripsi.module.student.main.payment.variant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.databinding.ItemStClassSubjectBinding
import com.example.project_skripsi.databinding.ItemStPaymentVariantBinding

class StPaymentVariantAdapter(private val paymentList: List<String>, private val viewType: Int) :
    RecyclerView.Adapter<StPaymentVariantAdapter.PaymentVariantViewHolder>() {

    companion object {
        const val VIEW_UPCOMING = 1
        const val VIEW_UNPAID = 2
        const val VIEW_PAID = 3
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PaymentVariantViewHolder =
        PaymentVariantViewHolder(
            ItemStPaymentVariantBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false))

    override fun onBindViewHolder(holder: PaymentVariantViewHolder, position: Int) {
        holder.bind(paymentList[position])
    }

    override fun getItemCount(): Int = paymentList.size


    inner class PaymentVariantViewHolder(private val binding : ItemStPaymentVariantBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(subjectName: String) {
            with(binding) {
                this.tvName.text = subjectName
            }
            when(viewType){
                VIEW_PAID -> {}
                VIEW_UNPAID -> {}
            }
        }
    }


}