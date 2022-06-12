package com.example.project_skripsi.module.student.main.progress.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.Achievement
import com.example.project_skripsi.databinding.ItemStProgressAchievementBinding
import com.example.project_skripsi.module.student.main.progress.viewmodel.StScoreViewModel
import com.example.project_skripsi.utils.app.App

class StScoreAchievementAdapter(private val viewModel: StScoreViewModel): RecyclerView.Adapter<StScoreAchievementAdapter.StScoreAchievementViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StScoreAchievementViewHolder {
        val item = ItemStProgressAchievementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StScoreAchievementViewHolder(item)
    }

    override fun onBindViewHolder(holder: StScoreAchievementViewHolder, position: Int) {
        viewModel.achievements.value?.let {
            val singleData = it[position]
            holder.bind(singleData)
        }
    }

    override fun getItemCount(): Int = viewModel.achievements.value?.size?: 0

    inner class StScoreAchievementViewHolder(private val binding: ItemStProgressAchievementBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Achievement) {
            with(binding) {
                tvTitle.text = item.title
                tvDescription.text = item.description
                viewIndicator.setBackgroundColor(ResourcesCompat.getColor(App.resourses!!, R.color.indicator_achievement, null))
            }
        }
    }
}