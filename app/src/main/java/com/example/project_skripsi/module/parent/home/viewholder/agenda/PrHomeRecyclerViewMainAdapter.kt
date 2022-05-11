package com.example.project_skripsi.module.parent.home.viewholder.agenda

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.local.HomeMainSection
import com.example.project_skripsi.databinding.ItemStHomeMainSectionBinding


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

                with(binding.sectionItemsRecyclerView) {
                    sectionItemsRecyclerView.layoutManager = LinearLayoutManager(context)
                    sectionItemsRecyclerView.adapter = PrHomeRecyclerViewChildAdapter(item)
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