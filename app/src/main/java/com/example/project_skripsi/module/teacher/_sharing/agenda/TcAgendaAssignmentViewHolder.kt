package com.example.project_skripsi.module.teacher._sharing.agenda

import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.local.HomeSectionData
import com.example.project_skripsi.core.model.local.TeacherAgendaTaskForm
import com.example.project_skripsi.databinding.ItemTcAgendaGeneralBinding
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.helper.DateHelper

class TcAgendaAssignmentViewHolder(
    private val binding: ItemTcAgendaGeneralBinding,
    private val listener: TcAgendaItemListener
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HomeSectionData) {
        val data = item as TeacherAgendaTaskForm
        with(binding) {
            viewIndicator.setBackgroundColor(
                ResourcesCompat.getColor(App.res!!, R.color.indicator_assignment, null)
            )
            tvTitle.text = data.taskForm.subjectName
            tvClassName.text = data.studyClassName
            tvLocation.text = data.taskForm.location
            tvTime.text =
                ("${DateHelper.getFormattedDateTime(DateHelper.hm, data.taskForm.startTime!!)} - " +
                        "${
                            DateHelper.getFormattedDateTime(
                                DateHelper.hm,
                                data.taskForm.endTime!!
                            )
                        }")
            btnResource.visibility = View.GONE
            btnClass.text = ("Tugas")
            btnClass.setOnClickListener {
                data.taskForm.id?.let { id ->
                    listener.onTaskFormItemClicked(
                        id,
                        data.studyClassId,
                        data.taskForm.subjectName ?: ""
                    )
                }
            }
        }
    }
}