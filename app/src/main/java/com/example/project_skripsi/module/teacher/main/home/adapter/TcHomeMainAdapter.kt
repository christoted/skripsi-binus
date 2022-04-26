package com.example.project_skripsi.module.teacher.main.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.local.HomeMainSection
import com.example.project_skripsi.databinding.ItemTcHomeMainSectionBinding
import com.example.project_skripsi.module.teacher._sharing.agenda.TcAgendaItemListener

class TcHomeMainAdapter(private val listHomeSectionData: List<HomeMainSection>, val listener: TcAgendaItemListener):
    RecyclerView.Adapter<TcHomeMainAdapter.TcHomeMainSectionViewHolder>() {

    val isExpanded = BooleanArray(listHomeSectionData.size){true}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TcHomeMainSectionViewHolder {
        return  TcHomeMainSectionViewHolder(
            ItemTcHomeMainSectionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TcHomeMainSectionViewHolder, position: Int) {
        listHomeSectionData.let {
            val singleItemMainSection = it[position]
            // Declare the child adapter
            val childAdapter = TcHomeChildAdapter(singleItemMainSection, listener)
            holder.bind(singleItemMainSection, childAdapter)
        }
    }

    override fun getItemCount(): Int {
        return listHomeSectionData.size
    }

    inner class TcHomeMainSectionViewHolder(private val binding: ItemTcHomeMainSectionBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(singleHomeMainSectionItem: HomeMainSection, adapter: TcHomeChildAdapter) {
            with(binding) {
                sectionTitle.text = singleHomeMainSectionItem.sectionName
                sectionTitleCount.text = singleHomeMainSectionItem.sectionItem.count().toString()
                Log.d("000", "bind: ${singleHomeMainSectionItem.sectionItem.count()}")
                with(binding.sectionItemsRecyclerView) {
                    sectionItemsRecyclerView.layoutManager = LinearLayoutManager(context)
                    sectionItemsRecyclerView.adapter = adapter
                }

                sectionItemsRecyclerView.isVisible = isExpanded[absoluteAdapterPosition]
                imvShowHide.setOnClickListener {
                    isExpanded[absoluteAdapterPosition] = !isExpanded[absoluteAdapterPosition]
                    sectionItemsRecyclerView.isVisible = isExpanded[absoluteAdapterPosition]
                    imvShowHide.setImageResource(
                        if (isExpanded[absoluteAdapterPosition]) R.drawable.ic_baseline_arrow_drop_up_24
                        else R.drawable.ic_baseline_arrow_drop_down_24
                    )
                }
            }
        }
    }
}