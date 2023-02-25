package com.example.project_skripsi.module.student.subject_detail._sharing


import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.project_skripsi.core.model.local.TaskFormStatus
import com.example.project_skripsi.databinding.ItemStTaskBinding
import com.example.project_skripsi.utils.Constant.Companion.TASK_TYPE_ASSIGNMENT
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.generic.GenericAdapter
import com.example.project_skripsi.utils.generic.ItemClickListener
import com.example.project_skripsi.utils.helper.DateHelper

class TaskViewHolder(
    private val dataSet: List<TaskFormStatus>,
    private val listener: ItemClickListener
) {

    fun getAdapter(): GenericAdapter<TaskFormStatus> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemStTaskBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item, viewBinding, _ ->
            val view = viewBinding as ItemStTaskBinding
            with(view) {
                tvTitle.text = item.title
                tvScore.text = item.score?.toString() ?: "-"
                tvSubjectName.text = ("nilai")
                tvClassName.visibility = View.GONE
                tvStatus.text = item.status

                item.statusColor?.let {
                    tvStatus.setTextColor(ResourcesCompat.getColor(App.res!!, it, null))
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

                root.setOnClickListener {
                    val taskType = if (item.type == TASK_TYPE_ASSIGNMENT) "Tugas" else "Ujian"
                    when {
                        DateHelper.getCurrentTime() < item.startTime ->
                            Toast.makeText(
                                root.context,
                                "$taskType belum dimulai",
                                Toast.LENGTH_SHORT
                            ).show()
                        DateHelper.getCurrentTime() > item.endTime && item.isChecked == false ->
                            Toast.makeText(
                                root.context,
                                "$taskType belum dikoreksi",
                                Toast.LENGTH_SHORT
                            ).show()
                        else -> {
                            item.id?.let { id -> root.setOnClickListener { listener.onItemClick(id) } }
                        }
                    }
                }
            }
        }
        return adapter
    }

}