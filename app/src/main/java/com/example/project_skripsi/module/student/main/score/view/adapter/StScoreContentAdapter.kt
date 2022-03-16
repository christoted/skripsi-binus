package com.example.project_skripsi.module.student.main.score.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.databinding.StItemScoreContentBinding
import com.example.project_skripsi.module.student.main.score.viewmodel.StScoreViewModel


class StScoreContentAdapter(private val viewModel: StScoreViewModel): RecyclerView.Adapter<StScoreContentAdapter.StScoreContentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StScoreContentViewHolder {
        val item = StItemScoreContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StScoreContentViewHolder(item)
    }

    override fun onBindViewHolder(holder: StScoreContentViewHolder, position: Int) {
        viewModel.sectionDatas.value?.let {
            val sinlgeData = it[position]
            holder.bind(sinlgeData)
        }
    }

    override fun getItemCount(): Int {
        viewModel.sectionDatas.value?.let {
            return it.size
        } ?: run {
            return 0
        }
    }


    inner class StScoreContentViewHolder(private val binding: StItemScoreContentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            with(binding) {
                text.text = item
            }
        }
    }

}