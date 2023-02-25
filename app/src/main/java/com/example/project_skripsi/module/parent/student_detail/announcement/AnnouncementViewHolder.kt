package com.example.project_skripsi.module.parent.student_detail.announcement

import android.view.LayoutInflater
import com.example.project_skripsi.core.model.firestore.Announcement
import com.example.project_skripsi.databinding.ItemPrStudentAnnouncementBinding
import com.example.project_skripsi.utils.generic.GenericAdapter
import com.example.project_skripsi.utils.helper.DateHelper

class AnnouncementViewHolder(private val dataSet: List<Announcement>) {

    fun getAdapter(): GenericAdapter<Announcement> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemPrStudentAnnouncementBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item, viewBinding, _ ->
            val view = viewBinding as ItemPrStudentAnnouncementBinding
            with(view) {
                tvTitle.text = item.title
                tvDate.text = item.date?.let { DateHelper.getFormattedDateTime(DateHelper.DMY, it) }
                tvDescription.text = item.description
            }
        }
        return adapter
    }

}