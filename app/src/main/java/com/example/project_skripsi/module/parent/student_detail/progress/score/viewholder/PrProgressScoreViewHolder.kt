package com.example.project_skripsi.module.parent.student_detail.progress.score.viewholder

import android.view.LayoutInflater
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.local.ScoreMainSection
import com.example.project_skripsi.databinding.ItemStProgressScoreBinding
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.generic.GenericAdapter

class PrProgressScoreViewHolder(private val dataSet : List<ScoreMainSection>) {

    private val isExpanded = BooleanArray(dataSet.size)

    fun getAdapter(): GenericAdapter<ScoreMainSection> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemStProgressScoreBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item, viewBinding, holder ->
            val view = viewBinding as ItemStProgressScoreBinding
            with(view) {
                subject.text = item.subjectName
                tvMid.text = item.mid_exam?.toString() ?: "-"
                tvFinal.text = item.final_exam?.toString() ?: "-"
                tvAssignment.text = item.total_assignment?.toString() ?: "-"
                tvTotal.text = item.total_score?.toString() ?: "-"

                with(view.sectionItemsRecyclerView) {
                    sectionItemsRecyclerView.layoutManager = LinearLayoutManager(context)
                    sectionItemsRecyclerView.adapter = PrProgressScoreChildViewHolder(item.sectionItem).getAdapter()
                    sectionItemsRecyclerView.addItemDecoration(DividerItemDecoration(view.root.context, DividerItemDecoration.VERTICAL))
                }
                sectionItemsRecyclerView.isVisible = isExpanded[holder.absoluteAdapterPosition]
                root.setOnClickListener {
                    isExpanded[holder.absoluteAdapterPosition] =
                        !isExpanded[holder.absoluteAdapterPosition]
                    sectionItemsRecyclerView.isVisible = isExpanded[holder.absoluteAdapterPosition]
                }

                viewIndicator.setBackgroundColor(ResourcesCompat.getColor(App.resourses!!, R.color.indicator_score, null))
            }
        }
        return adapter
    }
}