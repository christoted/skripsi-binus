package com.example.project_skripsi.module.student.main.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.local.HomeMainSection
import com.example.project_skripsi.databinding.ItemStHomeMainSectionBinding

class StHomeRecyclerViewMainAdapter(private val listHomeSectionData: List<HomeMainSection>, val listener: ItemListener):
    RecyclerView.Adapter<StHomeRecyclerViewMainAdapter.StHomeMainSectionViewHolder>() {

    val isExpanded = BooleanArray(listHomeSectionData.size){true}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StHomeMainSectionViewHolder =
        StHomeMainSectionViewHolder(
            ItemStHomeMainSectionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: StHomeMainSectionViewHolder, position: Int) {
        listHomeSectionData.let {
            val singleItemMainSection = it[position]
            // Declare the child adapter
            val childAdapter = StHomeRecyclerViewChildAdapter(singleItemMainSection, listener)
            holder.bind(singleItemMainSection, childAdapter)
        }
    }

    override fun getItemCount(): Int = listHomeSectionData.size


    inner class StHomeMainSectionViewHolder(private val binding: ItemStHomeMainSectionBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(singleHomeMainSectionItem: HomeMainSection, adapter: StHomeRecyclerViewChildAdapter) {
            with(binding) {
                sectionTitle.text = singleHomeMainSectionItem.sectionName
                sectionTitleCount.text = singleHomeMainSectionItem.sectionItem.count().toString()
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