package com.example.project_skripsi.module.student.main._sharing.agenda

import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.model.local.HomeSectionData
import com.example.project_skripsi.databinding.ItemStHomeSectionItemBinding
import com.example.project_skripsi.module.student.main.home.view.adapter.ItemListener
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.helper.DateHelper

class StHomeAssignmentViewHolder(
    private val binding: ItemStHomeSectionItemBinding,
    private val listener: ItemListener
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HomeSectionData) {
        val data = item as TaskForm
        with(binding) {
            viewIndicator.setBackgroundColor(
                ResourcesCompat.getColor(App.resourses!!, R.color.indicator_assignment, null)
            )
            tvTitle.text = data.subjectName
            tvLocation.text = data.location
            tvTime.text =
                ("${DateHelper.getFormattedDateTime(DateHelper.hm, data.startTime!!)} - " +
                        "${DateHelper.getFormattedDateTime(DateHelper.hm, data.endTime!!)}")
            btnResource.visibility = View.INVISIBLE
            btnClass.text = ("Tugas")

            if (DateHelper.getCurrentTime() > item.endTime) {
                btnClass.isEnabled = false
            } else {
                btnClass.setOnClickListener {
                    when {
                        DateHelper.getCurrentTime() > item.endTime ->
                            Toast.makeText(root.context, "Tugas sudah selesai", Toast.LENGTH_SHORT).show()
                        DateHelper.getCurrentTime() < item.startTime ->
                            Toast.makeText(root.context, "Tugas belum dimulai", Toast.LENGTH_SHORT)
                                .show()
                        else -> {
                            data.id?.let { id ->
                                listener.onTaskFormItemClicked(
                                    id,
                                    data.subjectName ?: ""
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}