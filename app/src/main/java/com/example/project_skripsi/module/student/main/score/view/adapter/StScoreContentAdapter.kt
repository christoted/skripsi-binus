package com.example.project_skripsi.module.student.main.score.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.databinding.ItemStScoreAbsensiBinding
import com.example.project_skripsi.databinding.ItemStScoreContentBinding
import com.example.project_skripsi.databinding.ItemStScorePencapaianBinding
import com.example.project_skripsi.module.student.main.score.viewmodel.StScoreViewModel


class StScoreContentAdapter(private val viewModel: StScoreViewModel, private val tab: Int): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when(tab) {
            0 -> {
                val item = ItemStScoreContentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return StScoreContentViewHolder(item)
            }
            1 -> {
                val item = ItemStScoreAbsensiBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return StScoreAbsensiViewHolder(item)
            }
            2 -> {
                val item = ItemStScorePencapaianBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return StScorePencapaianViewHolder(item)
            }
        }
        val item = ItemStScoreContentBinding.inflate(
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
                    val adapter = StScoreContentChildAdapter(viewModel)
                    (holder as StScoreContentViewHolder).bind(singleData, adapter)
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

    inner class StScoreContentViewHolder(private val binding: ItemStScoreContentBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                with(binding) {

                    sectionItemsRecyclerView.isVisible = !sectionItemsRecyclerView.isVisible

                }
            }
        }

        fun bind(item: String, adapter: StScoreContentChildAdapter) {
            with(binding) {
                //text.text = item
                with(binding.sectionItemsRecyclerView) {
                    sectionItemsRecyclerView.layoutManager = LinearLayoutManager(context)
                    sectionItemsRecyclerView.adapter = adapter
                    binding.sectionItemsRecyclerView.addItemDecoration(DividerItemDecoration(itemView.context, DividerItemDecoration.VERTICAL))
                }
            }
        }
    }

    inner class StScoreAbsensiViewHolder(private val binding: ItemStScoreAbsensiBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            with(binding) {
                //title.text = item
            }
        }
    }

    inner class StScorePencapaianViewHolder(private val binding: ItemStScorePencapaianBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            with(binding) {
                title.text = item
            }
        }
    }


}