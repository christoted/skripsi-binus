package com.example.project_skripsi.module.student.main.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.core.model.local.HomeMainSection
import com.example.project_skripsi.databinding.*
import com.example.project_skripsi.module.student.main._sharing.*
import com.example.project_skripsi.utils.Constant

class StHomeRecyclerViewChildAdapter(val item: HomeMainSection, private val listener: ItemListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder =

        when(item.sectionName) {
            Constant.SECTION_MEETING  -> StHomeMeetingViewHolder(
                ItemStHomeSectionItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false),listener)
            Constant.SECTION_EXAM -> StHomeExamViewHolder(
                ItemStHomeSectionItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false),listener)
            Constant.SECTION_ASSIGNMENT -> StHomeAssignmentViewHolder(
                ItemStHomeSectionItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false),listener)
            Constant.SECTION_PAYMENT-> StHomePaymentViewHolder(
                ItemStHomeSectionPaymentBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false))
            else -> StHomeAnnouncementViewHolder(
                ItemStHomeSectionAnnouncementBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false))
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val singleData = item.sectionItem[position]
        when(item.sectionName) {
            Constant.SECTION_PAYMENT -> (holder as StHomePaymentViewHolder).bind(singleData)
            Constant.SECTION_ANNOUNCEMENT -> (holder as StHomeAnnouncementViewHolder).bind(singleData)
            Constant.SECTION_MEETING -> (holder as StHomeMeetingViewHolder).bind(singleData)
            Constant.SECTION_EXAM -> (holder as StHomeExamViewHolder).bind(singleData)
            Constant.SECTION_ASSIGNMENT -> (holder as StHomeAssignmentViewHolder).bind(singleData)
        }
    }

    override fun getItemCount(): Int {
       return item.sectionItem.size
    }
}