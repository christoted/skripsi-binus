package com.example.project_skripsi.module.teacher.main.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.local.HomeMainSection
import com.example.project_skripsi.databinding.ItemTcHomeMainSectionBinding
import com.example.project_skripsi.databinding.ViewEmptyItemBinding
import com.example.project_skripsi.module.teacher._sharing.agenda.TcAgendaItemListener
import com.example.project_skripsi.utils.Constant
import com.example.project_skripsi.utils.helper.UIHelper

class TcHomeMainAdapter(private val listHomeSectionData: List<HomeMainSection>, val listener: TcAgendaItemListener):
    RecyclerView.Adapter<TcHomeMainAdapter.TcHomeMainSectionViewHolder>() {

    val isExpanded = BooleanArray(listHomeSectionData.size){true}
    val hasAddEmptyView = BooleanArray(listHomeSectionData.size){false}

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
        fun bind(item: HomeMainSection, adapter: TcHomeChildAdapter) {
            with(binding) {
                sectionTitle.text = item.sectionName
                sectionTitleCount.text = item.sectionItem.count().toString()

                with(binding.sectionItemsRecyclerView) {
                    if (item.sectionItem.isEmpty()) {
                        if (!hasAddEmptyView[absoluteAdapterPosition]) {
                            llCollapseGroup.addView(
                                UIHelper.getEmptyList(
                                    when (item.sectionName) {
                                        Constant.SECTION_PAYMENT -> "Tidak ada pembayaran hari ini"
                                        Constant.SECTION_ANNOUNCEMENT -> "Tidak ada pengumuman hari ini"
                                        Constant.SECTION_MEETING -> "Tidak ada kelas hari ini"
                                        Constant.SECTION_EXAM -> "Tidak ada ujian hari ini"
                                        Constant.SECTION_ASSIGNMENT -> "Tidak ada tugas hari ini"
                                        else -> "Tidak ada konten"
                                    },
                                    LayoutInflater.from(root.context), llCollapseGroup
                                )
                            )
                            hasAddEmptyView[absoluteAdapterPosition] = true
                        }
                    } else {
                        sectionItemsRecyclerView.layoutManager = LinearLayoutManager(context)
                        sectionItemsRecyclerView.adapter = adapter
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