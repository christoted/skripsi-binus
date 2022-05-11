package com.example.project_skripsi.module.teacher.study_class.task_detail

import com.example.project_skripsi.utils.generic.GenericAdapter
import android.view.LayoutInflater
import android.view.View
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.databinding.ItemTcStudyClassTaskDetailBinding
import com.example.project_skripsi.utils.generic.ItemClickListener

class TaskAssessmentViewHolder(
    private val taskType : Int,
    private val viewModel : TcStudyClassTaskDetailViewModel,
    private val dataSet : List<Student>,
    private val listener: ItemClickListener
) {

    fun getAdapter(): GenericAdapter<Student> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemTcStudyClassTaskDetailBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item, viewBinding,_ ->
            val view = viewBinding as ItemTcStudyClassTaskDetailBinding
            with(view) {
                tvAbsentNumber.text = ("${item.attendanceNumber}")
                tvName.text = item.name

                if (taskType == TcStudyClassTaskDetailViewModel.TASK_CHECKED) {
                    tvScore.text = viewModel.getTaskScore(item).toString()
                    tvScore.visibility = View.VISIBLE
                    tvScoreHeader.visibility = View.VISIBLE
                }

                item.id?.let { id -> root.setOnClickListener { listener.onItemClick(id) } }
            }
        }
        return adapter
    }

}