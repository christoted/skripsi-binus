package com.example.project_skripsi.module.student.main._sharing

import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.core.model.firestore.ClassMeeting
import com.example.project_skripsi.core.model.local.HomeSectionData
import com.example.project_skripsi.databinding.ItemStHomeSectionItemBinding
import com.example.project_skripsi.module.student.main.home.view.adapter.ItemListener
import com.example.project_skripsi.utils.helper.DateHelper

class StHomeClassViewHolder(private val binding: ItemStHomeSectionItemBinding, private val listener: ItemListener):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item : HomeSectionData) {
        val data = item as ClassMeeting
        with(binding) {
            title.text = data.subjectName
            status.text = data.location
            jamKelas.text = ("${DateHelper.getFormattedDateTime(DateHelper.hm, data.startTime!!)} - " +
                    "${DateHelper.getFormattedDateTime(DateHelper.hm, data.endTime!!)}")
            btnMateri.setOnClickListener {
                listener.onMaterialItemClicked(absoluteAdapterPosition)
            }
            btnKelas.setOnClickListener {
                listener.onClassItemClicked(absoluteAdapterPosition)
            }
        }
    }
}