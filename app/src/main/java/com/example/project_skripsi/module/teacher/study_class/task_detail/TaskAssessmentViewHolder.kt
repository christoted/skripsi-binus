package com.example.project_skripsi.module.teacher.study_class.task_detail

import android.graphics.drawable.Drawable
import com.example.project_skripsi.utils.generic.GenericAdapter
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.databinding.ItemTcStudyClassStudentBinding
import com.example.project_skripsi.databinding.ItemTcStudyClassTaskDetailBinding
import com.example.project_skripsi.utils.app.App
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
        adapter.expressionViewHolderBinding = { item, viewBinding->
            val view = viewBinding as ItemTcStudyClassTaskDetailBinding
            with(view) {
                tvAbsentNumber.text = "1."
                tvName.text = item.name

                if (taskType == TcStudyClassTaskDetailViewModel.TASK_UNCHECKED)
                    tvScore.visibility = View.GONE
                else
                    tvScore.text = viewModel.getTaskScore(item).toString()

                item.id?.let { id -> root.setOnClickListener { listener.onItemClick(id) } }
            }
        }
        return adapter
    }

}