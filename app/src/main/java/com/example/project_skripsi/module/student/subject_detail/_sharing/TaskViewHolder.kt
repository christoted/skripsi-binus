package com.example.project_skripsi.module.student.subject_detail._sharing

import com.example.project_skripsi.utils.generic.GenericAdapter
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.model.local.TaskFormStatus
import com.example.project_skripsi.databinding.ItemStTaskBinding
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.helper.DateHelper

class TaskViewHolder(private val taskType : Int, private val dataSet : List<TaskFormStatus>) {

    companion object{
        const val TYPE_EXAM = 1
        const val TYPE_ASSIGNMENT = 2
        val TASK_NAVIGATION = mapOf(
            TYPE_EXAM to R.id.action_stTaskExamFragment_to_stTaskFormFragment,
            TYPE_ASSIGNMENT to R.id.action_stTaskAssignmentFragment_to_stTaskFormFragment
        )
    }


    fun getAdapter(): GenericAdapter<TaskFormStatus> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemStTaskBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item,viewBinding->
            val view = viewBinding as ItemStTaskBinding
            with(view) {
                tvTitle.text = item.title
                if (item.score == null) tvScore.visibility = View.GONE
                else tvScore.text = item.score.toString()
                tvSubjectName.visibility = View.GONE
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
            view.root.setOnClickListener {

            }
        }
        return adapter
    }

}