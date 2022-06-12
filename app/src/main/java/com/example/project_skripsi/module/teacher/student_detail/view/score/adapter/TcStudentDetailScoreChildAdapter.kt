package com.example.project_skripsi.module.teacher.student_detail.view.score.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.core.model.firestore.AssignedTaskForm
import com.example.project_skripsi.core.model.local.ScoreMainSection
import com.example.project_skripsi.core.model.local.ScoreSectionData
import com.example.project_skripsi.databinding.ItemStProgressScoreChildBinding
import com.example.project_skripsi.module.teacher.student_detail.viewmodel.TcStudentDetailViewModel
import com.example.project_skripsi.utils.Constant


class TcStudentDetailScoreChildAdapter(private val viewModel: TcStudentDetailViewModel, private val scoreMainData: ScoreMainSection): RecyclerView.Adapter<TcStudentDetailScoreChildAdapter.StScoreContentChildViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StScoreContentChildViewHolder {
        val itemScoreContentChildV = ItemStProgressScoreChildBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StScoreContentChildViewHolder(itemScoreContentChildV)
    }

    override fun onBindViewHolder(holder: StScoreContentChildViewHolder, position: Int) {
        val singleData = scoreMainData.sectionItem[position]
        holder.bind(singleData)
    }

    override fun getItemCount(): Int {
        return scoreMainData.sectionItem.size
    }


    inner class StScoreContentChildViewHolder(private val binding: ItemStProgressScoreChildBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(sectionData: ScoreSectionData) {
            val item = sectionData as AssignedTaskForm
            with(binding) {
                tvTitle.text = item.title
                when (item.type) {
                    Constant.TASK_TYPE_MID_EXAM -> tvMid
                    Constant.TASK_TYPE_FINAL_EXAM -> tvFinal
                    else -> tvAssignment
                }.text = item.score.toString()
            }
        }
    }
}