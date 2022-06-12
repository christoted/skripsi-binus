package com.example.project_skripsi.module.student.main.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.local.HomeMainSection
import com.example.project_skripsi.databinding.ItemStHomeMainSectionBinding
import com.example.project_skripsi.utils.Constant.Companion.SECTION_ANNOUNCEMENT
import com.example.project_skripsi.utils.Constant.Companion.SECTION_ASSIGNMENT
import com.example.project_skripsi.utils.Constant.Companion.SECTION_EXAM
import com.example.project_skripsi.utils.Constant.Companion.SECTION_MEETING
import com.example.project_skripsi.utils.Constant.Companion.SECTION_PAYMENT
import com.example.project_skripsi.utils.helper.UIHelper


class StHomeRecyclerViewMainAdapter(private val listHomeSectionData: List<HomeMainSection>, val listener: ItemListener):
    RecyclerView.Adapter<StHomeRecyclerViewMainAdapter.StHomeMainSectionViewHolder>() {

    val isExpanded = BooleanArray(listHomeSectionData.size){true}
    val hasAddEmptyView = BooleanArray(listHomeSectionData.size){false}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StHomeMainSectionViewHolder =
        StHomeMainSectionViewHolder(
            ItemStHomeMainSectionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: StHomeMainSectionViewHolder, position: Int) {
        listHomeSectionData.let {
            val singleItemMainSection = it[position]
            holder.bind(singleItemMainSection)
        }
    }

    override fun getItemCount(): Int = listHomeSectionData.size


    inner class StHomeMainSectionViewHolder(private val binding: ItemStHomeMainSectionBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeMainSection) {
            with(binding) {
                sectionTitle.text = item.sectionName
                sectionTitleCount.text = item.sectionItem.count().toString()
                with(binding.sectionItemsRecyclerView) {
                    sectionItemsRecyclerView.layoutManager = LinearLayoutManager(context)
                    sectionItemsRecyclerView.adapter = StHomeRecyclerViewChildAdapter(item, listener)
                    if (item.sectionItem.isEmpty()) {
                        if (!hasAddEmptyView[absoluteAdapterPosition]) {
                            llCollapseGroup.addView(
                                UIHelper.getEmptyList(
                                    when (item.sectionName) {
                                        SECTION_PAYMENT -> "Tidak ada pembayaran hari ini"
                                        SECTION_ANNOUNCEMENT -> "Tidak ada pengumuman hari ini"
                                        SECTION_MEETING -> "Tidak ada kelas hari ini"
                                        SECTION_EXAM -> "Tidak ada ujian hari ini"
                                        SECTION_ASSIGNMENT -> "Tidak ada tugas hari ini"
                                        else -> "Tidak ada konten"
                                    },
                                    LayoutInflater.from(root.context), llCollapseGroup
                                )
                            )
                            hasAddEmptyView[absoluteAdapterPosition] = true
                        }
                    }

                }

                llCollapseGroup.isVisible = isExpanded[absoluteAdapterPosition]
                llHeader.setOnClickListener {
                    isExpanded[absoluteAdapterPosition] = !isExpanded[absoluteAdapterPosition]

                    TransitionManager.beginDelayedTransition(llCollapseGroup, AutoTransition())
                    llCollapseGroup.isVisible = isExpanded[absoluteAdapterPosition]
                    imvShowHide.setImageResource(
                        if (isExpanded[absoluteAdapterPosition]) R.drawable.ic_baseline_arrow_drop_up_24
                        else R.drawable.ic_baseline_arrow_drop_down_24
                    )
                }
            }
        }
    }
}