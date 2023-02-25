package com.example.project_skripsi.module.parent.student_detail.progress.score.viewholder

import android.view.LayoutInflater
import com.example.project_skripsi.core.model.firestore.AssignedTaskForm
import com.example.project_skripsi.core.model.local.ScoreSectionData
import com.example.project_skripsi.databinding.ItemStProgressScoreChildBinding
import com.example.project_skripsi.utils.Constant.Companion.TASK_TYPE_FINAL_EXAM
import com.example.project_skripsi.utils.Constant.Companion.TASK_TYPE_MID_EXAM
import com.example.project_skripsi.utils.generic.GenericAdapter

class PrProgressScoreChildViewHolder(private val dataSet: List<ScoreSectionData>) {

    fun getAdapter(): GenericAdapter<ScoreSectionData> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemStProgressScoreChildBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { _item, viewBinding, _ ->
            val view = viewBinding as ItemStProgressScoreChildBinding
            val item = _item as AssignedTaskForm
            with(view) {
                tvTitle.text = item.title
                when (item.type) {
                    TASK_TYPE_MID_EXAM -> tvMid
                    TASK_TYPE_FINAL_EXAM -> tvFinal
                    else -> tvAssignment
                }.text = item.score.toString()
            }
        }
        return adapter
    }
}