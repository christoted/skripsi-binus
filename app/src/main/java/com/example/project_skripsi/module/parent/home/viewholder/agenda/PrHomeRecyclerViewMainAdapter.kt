package com.example.project_skripsi.module.parent.home.viewholder.agenda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.core.model.local.HomeMainSection
import com.example.project_skripsi.databinding.ItemStHomeMainSectionBinding
import android.view.animation.Animation


class PrHomeRecyclerViewMainAdapter(private val listHomeSectionData: List<HomeMainSection>):
    RecyclerView.Adapter<PrHomeRecyclerViewMainAdapter.PrHomeMainSectionViewHolder>() {

    val isExpanded = BooleanArray(listHomeSectionData.size){true}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrHomeMainSectionViewHolder =
        PrHomeMainSectionViewHolder(
            ItemStHomeMainSectionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: PrHomeMainSectionViewHolder, position: Int) {
        listHomeSectionData.let {
            val singleItemMainSection = it[position]
            holder.bind(singleItemMainSection)
        }
    }

    override fun getItemCount(): Int = listHomeSectionData.size


    inner class PrHomeMainSectionViewHolder(private val binding: ItemStHomeMainSectionBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeMainSection) {
            with(binding) {
                sectionTitle.text = item.sectionName
                sectionTitleCount.text = item.sectionItem.count().toString()

                sectionItemsRecyclerView.isVisible = isExpanded[absoluteAdapterPosition]
                btnShowHide.setOnClickListener {
                    isExpanded[absoluteAdapterPosition] = !isExpanded[absoluteAdapterPosition]
                    sectionItemsRecyclerView.isVisible = isExpanded[absoluteAdapterPosition]
                    btnShowHide.text = if (isExpanded[absoluteAdapterPosition]) "[sembunyikan]" else "[tampilkan]"
                }
                with(binding.sectionItemsRecyclerView) {
                    sectionItemsRecyclerView.layoutManager = LinearLayoutManager(context)
                    sectionItemsRecyclerView.adapter = PrHomeRecyclerViewChildAdapter(item)
                }
            }
        }
    }
}