package com.example.project_skripsi.module.student.main._sharing

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.model.local.HomeSectionData
import com.example.project_skripsi.databinding.ItemStHomeSectionItemBinding
import com.example.project_skripsi.module.student.main.home.view.adapter.ItemListener
import com.example.project_skripsi.utils.helper.DateHelper

class StHomeExamViewHolder(private val binding: ItemStHomeSectionItemBinding, private val listener: ItemListener):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HomeSectionData) {
        val data = item as TaskForm
        with(binding) {
            title.text = data.subjectName
            status.text = data.type
            jamKelas.text = ("${DateHelper.getFormattedDateTime(DateHelper.hm, data.startTime!!)} - " +
                    "${DateHelper.getFormattedDateTime(DateHelper.hm, data.endTime!!)}")
            btnKelas.text = ("Ujian")
            btnMateri.isVisible = false
            btnKelas.setOnClickListener {
                data.id?.let { id -> listener.onTaskFormItemClicked(id) }
            }
        }
    }
}