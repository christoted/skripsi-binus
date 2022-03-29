package com.example.project_skripsi.module.student.main.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.*
import com.example.project_skripsi.databinding.*
import com.example.project_skripsi.module.student.main._sharing.*
import com.example.project_skripsi.module.student.main.calendar.StCalendarViewModel.Companion.TYPE_ASSIGNMENT
import com.example.project_skripsi.module.student.main.calendar.StCalendarViewModel.Companion.TYPE_MEETING
import com.example.project_skripsi.module.student.main.calendar.StCalendarViewModel.Companion.TYPE_EXAM
import com.example.project_skripsi.module.student.main.calendar.StCalendarViewModel.Companion.TYPE_PAYMENT

class StCalendarAdapter(private val dayEventList: List<DayEvent>) :
    Adapter<ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            TYPE_MEETING -> StHomeClassViewHolder(
                ItemStHomeSectionItemBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false))
            TYPE_EXAM -> StHomeExamViewHolder(
                ItemStHomeSectionItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false))
            TYPE_ASSIGNMENT -> StHomeAssignmentViewHolder(
                ItemStHomeSectionItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false))
            TYPE_PAYMENT -> StHomePaymentViewHolder(
                ItemStHomeSectionPembayaranBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false))
            else -> StHomeAnnouncementViewHolder(
                ItemStHomeSectionPengumumanBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false))
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (dayEventList[position].viewType) {
            TYPE_MEETING -> (holder as StHomeClassViewHolder).bind()
            TYPE_EXAM -> (holder as StHomeExamViewHolder).bind()
            TYPE_ASSIGNMENT -> (holder as StHomeAssignmentViewHolder).bind()
            TYPE_PAYMENT -> (holder as StHomePaymentViewHolder).bind()
            else -> (holder as StHomeAnnouncementViewHolder).bind()
        }
    }

    override fun getItemCount(): Int = dayEventList.size

    override fun getItemViewType(position: Int): Int {
        return dayEventList[position].viewType
    }

}
