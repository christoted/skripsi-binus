package com.example.project_skripsi.module.parent.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.Announcement
import com.example.project_skripsi.core.model.firestore.Payment
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.model.local.HomeSectionData
import com.example.project_skripsi.databinding.ItemPrHomeStudentBinding
import com.example.project_skripsi.databinding.ItemStHomeSectionAnnouncementBinding
import com.example.project_skripsi.databinding.ItemStPaymentVariantBinding
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.generic.GenericAdapter
import com.example.project_skripsi.utils.generic.ItemClickListener
import com.example.project_skripsi.utils.helper.CurrencyHelper
import com.example.project_skripsi.utils.helper.DateHelper

class StudentViewHolder(private val dataSet : List<Student>, private val listener: ItemClickListener) {

    fun getAdapter(): GenericAdapter<Student> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemPrHomeStudentBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item,viewBinding,_ ->
            val view = viewBinding as ItemPrHomeStudentBinding
            with(view) {
                tvStudentName.text = item.name
                root.setOnClickListener { item.id?.let { id -> listener.onItemClick(id) } }
            }
        }
        return adapter
    }

}