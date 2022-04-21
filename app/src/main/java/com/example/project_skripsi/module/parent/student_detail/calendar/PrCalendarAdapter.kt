package com.example.project_skripsi.module.parent.student_detail.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.*
import com.example.project_skripsi.core.model.local.CalendarItem
import com.example.project_skripsi.databinding.*
import com.example.project_skripsi.module.parent.student_detail.calendar.PrCalendarViewModel.Companion.TYPE_ASSIGNMENT
import com.example.project_skripsi.module.parent.student_detail.calendar.PrCalendarViewModel.Companion.TYPE_EXAM
import com.example.project_skripsi.module.parent.student_detail.calendar.PrCalendarViewModel.Companion.TYPE_MEETING
import com.example.project_skripsi.module.parent.student_detail.calendar.PrCalendarViewModel.Companion.TYPE_PAYMENT
import com.example.project_skripsi.module.parent.student_detail.calendar.viewholder.PrHomeAssignmentViewHolder
import com.example.project_skripsi.module.parent.student_detail.calendar.viewholder.PrHomeExamViewHolder
import com.example.project_skripsi.module.parent.student_detail.calendar.viewholder.PrHomeMeetingViewHolder
import com.example.project_skripsi.module.student.main._sharing.agenda.*

class PrCalendarAdapter(private val dataList: List<CalendarItem>) :
    Adapter<ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            TYPE_MEETING -> PrHomeMeetingViewHolder(
                ItemPrCalendarGeneralBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false))
            TYPE_EXAM -> PrHomeExamViewHolder(
                ItemPrCalendarGeneralBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false))
            TYPE_ASSIGNMENT -> PrHomeAssignmentViewHolder(
                ItemPrCalendarGeneralBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false))
            TYPE_PAYMENT -> StHomePaymentViewHolder(
                ItemStHomeSectionPaymentBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false))
            else -> StHomeAnnouncementViewHolder(
                ItemStHomeSectionAnnouncementBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false))
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (dataList[position].viewType) {
            TYPE_MEETING -> (holder as PrHomeMeetingViewHolder).bind(dataList[position].item)
            TYPE_EXAM -> (holder as PrHomeExamViewHolder).bind(dataList[position].item)
            TYPE_ASSIGNMENT -> (holder as PrHomeAssignmentViewHolder).bind(dataList[position].item)
            TYPE_PAYMENT -> (holder as StHomePaymentViewHolder).bind(dataList[position].item)
            else -> (holder as StHomeAnnouncementViewHolder).bind(dataList[position].item)
        }
    }

    override fun getItemCount(): Int = dataList.size

    override fun getItemViewType(position: Int): Int {
        return dataList[position].viewType
    }

}
