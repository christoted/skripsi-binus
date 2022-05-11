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
import com.example.project_skripsi.module.parent.student_detail.calendar.viewholder.PrCalendarAssignmentViewHolder
import com.example.project_skripsi.module.parent.student_detail.calendar.viewholder.PrCalendarExamViewHolder
import com.example.project_skripsi.module.parent.student_detail.calendar.viewholder.PrCalendarMeetingViewHolder
import com.example.project_skripsi.module.student.main._sharing.agenda.*

class PrCalendarAdapter(private val dataList: List<CalendarItem>) :
    Adapter<ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            TYPE_MEETING -> PrCalendarMeetingViewHolder(
                ItemPrCalendarGeneralBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false))
            TYPE_EXAM -> PrCalendarExamViewHolder(
                ItemPrCalendarGeneralBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false))
            TYPE_ASSIGNMENT -> PrCalendarAssignmentViewHolder(
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
            TYPE_MEETING -> (holder as PrCalendarMeetingViewHolder).bind(dataList[position].item)
            TYPE_EXAM -> (holder as PrCalendarExamViewHolder).bind(dataList[position].item)
            TYPE_ASSIGNMENT -> (holder as PrCalendarAssignmentViewHolder).bind(dataList[position].item)
            TYPE_PAYMENT -> (holder as StHomePaymentViewHolder).bind(dataList[position].item)
            else -> (holder as StHomeAnnouncementViewHolder).bind(dataList[position].item)
        }
    }

    override fun getItemCount(): Int = dataList.size

    override fun getItemViewType(position: Int): Int {
        return dataList[position].viewType
    }

}
