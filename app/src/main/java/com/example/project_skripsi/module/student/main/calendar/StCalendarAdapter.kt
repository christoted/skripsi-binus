package com.example.project_skripsi.module.student.main.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.example.project_skripsi.databinding.*
import com.example.project_skripsi.module.student.main._sharing.*
import com.example.project_skripsi.module.student.task.form.FormAdapter
import java.util.*

class StCalendarAdapter(private val dayEventList: List<DayEvent>) :
    Adapter<ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            StCalendarViewModel.TYPE_CLASS -> StHomeClassViewHolder(
                ItemStHomeSectionItemBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false))
            StCalendarViewModel.TYPE_EXAM -> StHomeExamViewHolder(
                ItemStHomeSectionItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false))
            StCalendarViewModel.TYPE_ASSIGNMENT -> StHomeAssignmentViewHolder(
                ItemStHomeSectionItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false))
            StCalendarViewModel.TYPE_PAYMENT -> StHomePaymentViewHolder(
                ItemStHomeSectionPembayaranBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false))
            else -> StHomeAnnouncementViewHolder(
                ItemStHomeSectionPengumumanBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false))
        }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (dayEventList[position].viewType) {
            StCalendarViewModel.TYPE_CLASS -> (holder as StHomeClassViewHolder).bind()
            StCalendarViewModel.TYPE_EXAM -> (holder as StHomeExamViewHolder).bind()
            StCalendarViewModel.TYPE_ASSIGNMENT -> (holder as StHomeAssignmentViewHolder).bind()
            StCalendarViewModel.TYPE_PAYMENT -> (holder as StHomePaymentViewHolder).bind()
            else -> (holder as StHomeAnnouncementViewHolder).bind()
        }
    }

    override fun getItemCount(): Int = dayEventList.size

    override fun getItemViewType(position: Int): Int {
        return dayEventList[position].viewType
    }

//    inner class CalendarViewHolder(private val binding : ItemStCalendarBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(dayEvent: DayEvent) {
//            with(binding) {
//                tvName.text = dayEvent.viewType.toString()
//                val c : Calendar = Calendar.getInstance().let {
//                    it.time = dayEvent.eventTime

//                    it
//                }
//
//                tvDate.text = "${c.get(Calendar.HOUR_OF_DAY)} ${c.get(Calendar.MINUTE)}"
//            }
//
//        }
//    }
}
