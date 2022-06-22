package com.example.project_skripsi.module.student.main._sharing.agenda

import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.ClassMeeting
import com.example.project_skripsi.core.model.local.HomeSectionData
import com.example.project_skripsi.databinding.ItemStHomeSectionItemBinding
import com.example.project_skripsi.module.student.main.home.view.adapter.ItemListener
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.helper.DateHelper
import com.example.project_skripsi.utils.helper.DateHelper.Companion.getDateWithMinuteOffset

class StHomeMeetingViewHolder(private val binding: ItemStHomeSectionItemBinding, private val listener: ItemListener):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item : HomeSectionData) {
        val data = item as ClassMeeting
        with(binding) {
            viewIndicator.setBackgroundColor(
                ResourcesCompat.getColor(App.resourses!!, R.color.indicator_meeting, null))
            tvTitle.text = data.subjectName
            tvLocation.text = data.location
            tvTime.text = ("${DateHelper.getFormattedDateTime(DateHelper.hm, data.startTime!!)} - " +
                    "${DateHelper.getFormattedDateTime(DateHelper.hm, data.endTime!!)}")

            if (item.meetingResource.isNullOrEmpty()) {
                btnResource.isEnabled = false
            } else {
                btnResource.setOnClickListener {
                    listener.onResourceItemClicked(data.meetingResource!!)
                }
            }

            if (DateHelper.getCurrentTime() > item.endTime) {
                btnClass.isEnabled = false
            } else {
                btnClass.setOnClickListener {
                    when {
                        DateHelper.getCurrentTime() < item.startTime.getDateWithMinuteOffset(-5) ->
                            Toast.makeText(root.context, "Kelas dapat diakses 5 menit sebelum jam mulai", Toast.LENGTH_SHORT).show()
                        else -> {
                            listener.onClassItemClicked(data)
                        }
                    }
                }
            }
        }
    }
}