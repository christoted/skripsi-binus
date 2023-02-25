package com.example.project_skripsi.module.teacher.study_class.task

import android.view.LayoutInflater
import android.view.View
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.model.local.TaskFormStatus
import com.example.project_skripsi.databinding.ItemStTaskBinding
import com.example.project_skripsi.utils.generic.GenericAdapter
import com.example.project_skripsi.utils.generic.ItemClickListener
import com.example.project_skripsi.utils.helper.DateHelper


class TaskViewHolder(
    private val dataSet: List<TaskForm>,
    private val listener: ItemClickListener
) {

    fun getAdapter(): GenericAdapter<TaskForm> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemStTaskBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item, viewBinding, _ ->
            val view = viewBinding as ItemStTaskBinding
            with(view) {
                llDataContainer.visibility = View.GONE
                vwDataSeparator.visibility = View.GONE
                tvScore.visibility = View.GONE

                tvTitle.text = item.title

                item.startTime?.let {
                    tvStartDate.text = DateHelper.getFormattedDateTime(DateHelper.DMY, it)
                    tvStartTime.text = DateHelper.getFormattedDateTime(DateHelper.hm, it)
                }

                item.endTime?.let {
                    tvEndDate.text = DateHelper.getFormattedDateTime(DateHelper.DMY, it)
                    tvEndTime.text = DateHelper.getFormattedDateTime(DateHelper.hm, it)
                }

                tvDuration.text = ("${TaskFormStatus.getDuration(item)} menit")

                item.id?.let { id -> root.setOnClickListener { listener.onItemClick(id) } }

            }
        }
        return adapter
    }

}