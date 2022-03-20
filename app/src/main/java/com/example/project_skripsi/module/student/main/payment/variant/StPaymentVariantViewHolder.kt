package com.example.project_skripsi.module.student.main.payment.variant

import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.Navigation
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.ItemStPaymentVariantBinding
import com.example.project_skripsi.databinding.ItemStTaskBinding
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.generic.GenericAdapter

class StPaymentVariantViewHolder(private val taskType : Int, private val dataSet : List<String>) {

    companion object {
        const val TYPE_UPCOMING = 0
        const val TYPE_UNPAID = 1
        const val TYPE_PAID = 2
    }


    fun getAdapter(): GenericAdapter<String> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemStPaymentVariantBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item,viewBinding->
            val view = viewBinding as ItemStPaymentVariantBinding
            with(view) {
                tvName.text = item
            }
            when(taskType){
                TYPE_PAID -> {
                    view.tvDateTitle.text = "Terbayar pada"
                }
                TYPE_UNPAID -> {
                    view.llContainer.setBackgroundColor(ResourcesCompat.getColor(App.resourses!!, R.color.payment_late, null))
                }
            }
        }
        return adapter
    }

}