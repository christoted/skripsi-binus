package com.example.project_skripsi.module.student.main.score.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.databinding.StItemScoreContentCollapseBinding
import com.example.project_skripsi.module.student.main.score.viewmodel.StScoreViewModel

class StScoreContentChildAdapter(private val viewModel: StScoreViewModel): RecyclerView.Adapter<StScoreContentChildAdapter.StScoreContentChildViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StScoreContentChildViewHolder {
       val itemScoreContentChildV = StItemScoreContentCollapseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StScoreContentChildViewHolder(itemScoreContentChildV)
    }

    override fun onBindViewHolder(holder: StScoreContentChildViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 5
    }


    inner class StScoreContentChildViewHolder(private val binding: StItemScoreContentCollapseBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind() {

        }
    }
}