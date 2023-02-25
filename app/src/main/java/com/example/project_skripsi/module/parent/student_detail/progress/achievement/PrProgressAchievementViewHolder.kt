package com.example.project_skripsi.module.parent.student_detail.progress.achievement

import android.view.LayoutInflater
import androidx.core.content.res.ResourcesCompat
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.Achievement
import com.example.project_skripsi.databinding.ItemStProgressAchievementBinding
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.generic.GenericAdapter

class PrProgressAchievementViewHolder(private val dataSet: List<Achievement>) {

    fun getAdapter(): GenericAdapter<Achievement> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemStProgressAchievementBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item, viewBinding, _ ->
            val view = viewBinding as ItemStProgressAchievementBinding
            with(view) {
                tvTitle.text = item.title
                tvDescription.text = item.description
                viewIndicator.setBackgroundColor(
                    ResourcesCompat.getColor(
                        App.res!!,
                        R.color.indicator_achievement,
                        null
                    )
                )
            }
        }
        return adapter
    }

}