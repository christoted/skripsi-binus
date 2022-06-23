package com.example.project_skripsi.module.parent.home.viewholder.agenda

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.core.model.local.HomeMainSection
import com.example.project_skripsi.databinding.ItemPrHomeGeneralBinding
import com.example.project_skripsi.databinding.ItemPrHomePaymentBinding
import com.example.project_skripsi.databinding.ItemStHomeSectionAnnouncementBinding
import com.example.project_skripsi.module.student.main._sharing.agenda.StHomeAnnouncementViewHolder
import com.example.project_skripsi.utils.Constant

class PrHomeRecyclerViewChildAdapter(val item: HomeMainSection) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder =

        when (item.sectionName) {
            Constant.SECTION_MEETING -> PrHomeMeetingViewHolder(
                ItemPrHomeGeneralBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                )
            )
            Constant.SECTION_EXAM -> PrHomeExamViewHolder(
                ItemPrHomeGeneralBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                )
            )
            Constant.SECTION_ASSIGNMENT -> PrHomeAssignmentViewHolder(
                ItemPrHomeGeneralBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                )
            )
            Constant.SECTION_PAYMENT -> PrHomePaymentViewHolder(
                ItemPrHomePaymentBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                )
            )
            else -> StHomeAnnouncementViewHolder(
                ItemStHomeSectionAnnouncementBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                )
            )
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val singleData = item.sectionItem[position]
        when (item.sectionName) {
            Constant.SECTION_PAYMENT -> (holder as PrHomePaymentViewHolder).bind(singleData)
            Constant.SECTION_ANNOUNCEMENT -> (holder as StHomeAnnouncementViewHolder).bind(
                singleData
            )
            Constant.SECTION_MEETING -> (holder as PrHomeMeetingViewHolder).bind(singleData)
            Constant.SECTION_EXAM -> (holder as PrHomeExamViewHolder).bind(singleData)
            Constant.SECTION_ASSIGNMENT -> (holder as PrHomeAssignmentViewHolder).bind(singleData)
        }
    }

    override fun getItemCount(): Int {
        return item.sectionItem.size
    }
}