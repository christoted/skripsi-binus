package com.example.project_skripsi.module.student.task._sharing

import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import com.example.project_skripsi.core.model.local.TaskFormStatus
import com.example.project_skripsi.databinding.ItemStTaskBinding
import com.example.project_skripsi.module.student.task.assignment.StTaskAssignmentFragmentDirections
import com.example.project_skripsi.module.student.task.exam.StTaskExamFragmentDirections
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.generic.GenericAdapter
import com.example.project_skripsi.utils.helper.DateHelper

class TaskViewHolder(private val taskType: Int, private val dataSet: List<TaskFormStatus>) {

    companion object{
        const val TYPE_EXAM = 1
        const val TYPE_ASSIGNMENT = 2

        val mapOfTask = mapOf(
            TYPE_EXAM to "Ujian",
            TYPE_ASSIGNMENT to "Tugas",
        )
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

                root.setOnClickListener {
                    when {
                        DateHelper.getCurrentTime() < item.startTime ->
                            Toast.makeText(
                                root.context,
                                "${mapOfTask[taskType]} belum dimulai",
                                Toast.LENGTH_SHORT
                            ).show()
                        DateHelper.getCurrentTime() > item.endTime && item.isChecked == false ->
                            Toast.makeText(
                                root.context,
                                "${mapOfTask[taskType]} belum dikoreksi",
                                Toast.LENGTH_SHORT
                            ).show()
                        else -> {
                            item.id?.let { id ->
                                it.findNavController().navigate(
                                    when (taskType) {
                                        TYPE_EXAM -> StTaskExamFragmentDirections
                                            .actionStTaskExamFragmentToStTaskFormFragment(id)
                                        else -> StTaskAssignmentFragmentDirections
                                            .actionStTaskAssignmentFragmentToStTaskFormFragment(
                                                id
                                            )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        return adapter
    }

}