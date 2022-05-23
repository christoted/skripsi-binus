package com.example.project_skripsi.module.student.main.home.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
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
import com.example.project_skripsi.utils.app.App

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
                    if (adapter.itemCount > 0) cvEmpty.visibility = View.GONE
                    else {
                        tvEmpty.text = when (singleHomeMainSectionItem.sectionName) {
                            SECTION_PAYMENT -> "Tidak ada pembayaran hari ini"
                            SECTION_ANNOUNCEMENT -> "Tidak ada pengumuman hari ini"
                            SECTION_MEETING -> "Tidak ada kelas hari ini"
                            SECTION_EXAM -> "Tidak ada ujian hari ini"
                            SECTION_ASSIGNMENT -> "Tidak ada tugas hari ini"
                            else -> "Tidak ada konten"
                        }

                        viewIndicator.setBackgroundColor(
                            ResourcesCompat.getColor(
                                App.resourses!!,
                                when (singleHomeMainSectionItem.sectionName) {
                                    SECTION_PAYMENT -> R.color.indicator_payment
                                    SECTION_ANNOUNCEMENT -> R.color.indicator_announcement
                                    SECTION_MEETING -> R.color.indicator_meeting
                                    SECTION_EXAM -> R.color.indicator_exam
                                    SECTION_ASSIGNMENT ->  R.color.indicator_assignment
                                    else -> R.color.black
                                },
                                null
                            )
                        )
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