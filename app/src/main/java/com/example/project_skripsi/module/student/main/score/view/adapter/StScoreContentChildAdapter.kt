package com.example.project_skripsi.module.student.main.score.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.core.model.firestore.AssignedTaskForm
import com.example.project_skripsi.core.model.local.ScoreMainSection
import com.example.project_skripsi.core.model.local.ScoreSectionData
import com.example.project_skripsi.databinding.ItemStScoreContentCollapseBinding
import com.example.project_skripsi.module.student.main.score.viewmodel.StScoreViewModel

class StScoreContentChildAdapter(private val viewModel: StScoreViewModel, private val scoreMainData: ScoreMainSection): RecyclerView.Adapter<StScoreContentChildAdapter.StScoreContentChildViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StScoreContentChildViewHolder {
       val itemScoreContentChildV = ItemStScoreContentCollapseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StScoreContentChildViewHolder(itemScoreContentChildV)
    }

    override fun onBindViewHolder(holder: StScoreContentChildViewHolder, position: Int) {
        val singleData = scoreMainData.sectionItem[position]
        holder.bind(singleData)
    }

    override fun getItemCount(): Int {
        return scoreMainData.sectionItem.size
    }


    inner class StScoreContentChildViewHolder(private val binding: ItemStScoreContentCollapseBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(sectionData: ScoreSectionData) {
            binding.subject.text = (sectionData as AssignedTaskForm).title
            binding.score.text = (sectionData as AssignedTaskForm).score.toString()
        }
    }
}