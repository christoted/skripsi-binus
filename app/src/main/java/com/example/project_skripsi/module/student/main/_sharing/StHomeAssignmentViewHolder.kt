package com.example.project_skripsi.module.student.main._sharing

import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.model.local.HomeSectionData
import com.example.project_skripsi.databinding.ItemStHomeSectionItemBinding
import com.example.project_skripsi.module.student.main.home.view.adapter.ItemListener
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.helper.DateHelper

class StHomeAssignmentViewHolder(private val binding: ItemStHomeSectionItemBinding, private val listener: ItemListener):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HomeSectionData) {
        val data = item as TaskForm
        with(binding) {
            viewIndicator.setBackgroundColor(
                ResourcesCompat.getColor(App.resourses!!, R.color.indicator_assignment, null))
            tvTitle.text = data.subjectName
            tvLocation.text = data.location
            tvTime.text = ("${DateHelper.getFormattedDateTime(DateHelper.hm, data.startTime!!)} - " +
                    "${DateHelper.getFormattedDateTime(DateHelper.hm, data.endTime!!)}")
            btnResource.visibility = View.GONE
            btnClass.text = ("Tugas")
            btnClass.setOnClickListener {
                data.id?.let { id -> listener.onTaskFormItemClicked(id, data.subjectName ?: "") }
            }
        }
    }
}