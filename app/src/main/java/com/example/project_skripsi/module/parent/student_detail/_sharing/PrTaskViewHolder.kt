package com.example.project_skripsi.module.parent.student_detail._sharing

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.example.project_skripsi.core.model.local.TaskFormStatus
import com.example.project_skripsi.databinding.ItemStTaskBinding
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.generic.GenericAdapter
import com.example.project_skripsi.utils.helper.DateHelper

class PrTaskViewHolder(private val dataSet : List<TaskFormStatus>) {

    fun getAdapter(): GenericAdapter<TaskFormStatus> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemStTaskBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item,viewBinding,_ ->
            val view = viewBinding as ItemStTaskBinding

            with(view) {
                tvTitle.text = item.title
                tvScore.text = if (item.score == null) "-" else item.score.toString()
                tvSubjectName.text = item.subjectName
                tvClassName.visibility = View.GONE
                tvStatus.text = item.status

                item.statusColor?.let {
                    tvStatus.setTextColor(ResourcesCompat.getColor(App.resourses!!, it, null))
                }

                item.startTime?.let {
                    tvStartDate.text = DateHelper.getFormattedDateTime(DateHelper.DMY, it)
                    tvStartTime.text = DateHelper.getFormattedDateTime(DateHelper.hm, it)
                }

                item.endTime?.let {
                    tvEndDate.text = DateHelper.getFormattedDateTime(DateHelper.DMY, it)
                    tvEndTime.text = DateHelper.getFormattedDateTime(DateHelper.hm, it)
                }

                tvDuration.text = ("${item.duration} menit")
            }
        }
        return adapter
    }

}