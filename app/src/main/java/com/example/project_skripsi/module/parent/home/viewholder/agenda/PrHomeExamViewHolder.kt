package com.example.project_skripsi.module.parent.home.viewholder.agenda

import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.model.local.HomeSectionData
import com.example.project_skripsi.core.model.local.ParentAgendaTaskForm
import com.example.project_skripsi.databinding.ItemPrHomeGeneralBinding
import com.example.project_skripsi.databinding.ItemStHomeSectionItemBinding
import com.example.project_skripsi.module.student.main.home.view.adapter.ItemListener
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.helper.DateHelper

class PrHomeExamViewHolder(private val binding: ItemPrHomeGeneralBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HomeSectionData) {
        val data = item as ParentAgendaTaskForm
        with(binding) {
            tvStudentName.text = data.studentName
            tvSubjectName.text = data.assignedTaskForm?.subjectName
            tvLocation.text = ("Online")
            tvTime.text = ("${DateHelper.getFormattedDateTime(DateHelper.hm, data.assignedTaskForm?.startTime)}")
            viewIndicator.setBackgroundColor(
                ResourcesCompat.getColor(App.resourses!!, R.color.indicator_exam, null))
        }
    }
}