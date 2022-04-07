package com.example.project_skripsi.module.student.task._sharing

import android.os.Bundle
import com.example.project_skripsi.utils.generic.GenericAdapter
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.local.TaskFormStatus
import com.example.project_skripsi.databinding.ItemStTaskBinding
import com.example.project_skripsi.module.student.task.assignment.StTaskAssignmentFragmentDirections
import com.example.project_skripsi.module.student.task.exam.StTaskExamFragmentDirections
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.custom_views.DummyFragmentDirections
import com.example.project_skripsi.utils.helper.DateHelper

class TaskViewHolder(private val taskType : Int, private val dataSet : List<TaskFormStatus>) {

    companion object{
        const val TYPE_EXAM = 1
        const val TYPE_ASSIGNMENT = 2
    }


    fun getAdapter(): GenericAdapter<TaskFormStatus> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemStTaskBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item,viewBinding,_ ->
            val view = viewBinding as ItemStTaskBinding

            with(view) {
                tvTitle.text = item.title
                if (item.score == null) tvScore.visibility = View.GONE
                else tvScore.text = item.score.toString()
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
                item.id?.let { id ->
                    root.setOnClickListener {
                        it.findNavController().navigate(
                            when (taskType) {
                                TYPE_EXAM -> StTaskExamFragmentDirections
                                    .actionStTaskExamFragmentToStTaskFormFragment(id)
                                else -> StTaskAssignmentFragmentDirections
                                    .actionStTaskAssignmentFragmentToStTaskFormFragment(id)
                            }
                        )
                    }
                }
            }
        }
        return adapter
    }

}