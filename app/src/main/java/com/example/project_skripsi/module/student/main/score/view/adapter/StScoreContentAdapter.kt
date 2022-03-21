package com.example.project_skripsi.module.student.main.score.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.databinding.StItemScoreAbsensiBinding
import com.example.project_skripsi.databinding.StItemScoreContentBinding
import com.example.project_skripsi.databinding.StItemScorePencapaianBinding
import com.example.project_skripsi.module.student.main.score.viewmodel.StScoreViewModel


class StScoreContentAdapter(private val viewModel: StScoreViewModel, private val tab: Int): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when(tab) {
            0 -> {
                val item = StItemScoreContentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return StScoreContentViewHolder(item)
            }
            1 -> {
                val item = StItemScoreAbsensiBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return StScoreAbsensiViewHolder(item)
            }
            2 -> {
                val item = StItemScorePencapaianBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return StScorePencapaianViewHolder(item)
            }
        }
        val item = StItemScoreContentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StScoreContentViewHolder(item)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(tab) {
            0 -> {
                viewModel.sectionDatas.value?.let {
                    val singleData = it[position]
                    (holder as StScoreContentViewHolder).bind(singleData)
                }
            }
            1 -> {
                viewModel.sectionDatas.value?.let {
                    val singleData = it[position]
                    (holder as StScoreAbsensiViewHolder).bind(singleData)
                }
            }
            2 -> {
                viewModel.sectionDatas.value?.let {
                    val singleData = it[position]
                    (holder as StScorePencapaianViewHolder).bind(singleData)
                }
            }
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

    inner class StScoreAbsensiViewHolder(private val binding: StItemScoreAbsensiBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            with(binding) {
                //title.text = item
            }
        }
    }

    inner class StScorePencapaianViewHolder(private val binding: StItemScorePencapaianBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            with(binding) {
                title.text = item
            }
        }
    }


}