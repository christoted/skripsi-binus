package com.example.project_skripsi.module.parent.home.viewholder.agenda

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.local.HomeSectionData
import com.example.project_skripsi.core.model.local.ParentAgendaTaskForm
import com.example.project_skripsi.databinding.ItemPrHomeGeneralBinding
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.helper.DateHelper

class PrHomeAssignmentViewHolder(private val binding: ItemPrHomeGeneralBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HomeSectionData) {
        val data = item as ParentAgendaTaskForm
        with(binding) {
            tvStudentName.text = data.studentName
            tvSubjectName.text = data.assignedTaskForm?.subjectName
            tvLocation.text = ("Online")
            tvTime.text = ("${
                DateHelper.getFormattedDateTime(
                    DateHelper.hm,
                    data.assignedTaskForm?.startTime
                )
            } - " +
                    "${
                        DateHelper.getFormattedDateTime(
                            DateHelper.hm,
                            data.assignedTaskForm?.endTime
                        )
                    }")
            viewIndicator.setBackgroundColor(
                ResourcesCompat.getColor(App.res!!, R.color.indicator_assignment, null)
            )
        }
    }
}