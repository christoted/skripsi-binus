package com.example.project_skripsi.module.student.main.score.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.core.model.firestore.Achievement
import com.example.project_skripsi.databinding.ItemStScorePencapaianBinding
import com.example.project_skripsi.module.student.main.score.viewmodel.StScoreViewModel

class StScoreAchievementAdapter(private val viewModel: StScoreViewModel): RecyclerView.Adapter<StScoreAchievementAdapter.StScoreAchievementViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StScoreAchievementViewHolder {
        val item = ItemStScorePencapaianBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StScoreAchievementViewHolder(item)
    }

    override fun onBindViewHolder(holder: StScoreAchievementViewHolder, position: Int) {
        viewModel.achievements.value?.let {
            val singleData = it[position]
            holder.bind(singleData)
        }
    }

    override fun getItemCount(): Int = viewModel.achievements.value?.size?: 0

    inner class StScoreAchievementViewHolder(private val binding: ItemStScorePencapaianBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Achievement) {
            with(binding) {
                title.text = item.title
                deskripsi.text = item.description
            }
        }
    }
}