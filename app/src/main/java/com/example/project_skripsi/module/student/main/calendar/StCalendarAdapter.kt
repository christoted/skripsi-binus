package com.example.project_skripsi.module.student.main.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.project_skripsi.core.model.local.CalendarItem
import com.example.project_skripsi.databinding.ItemStHomeSectionAnnouncementBinding
import com.example.project_skripsi.databinding.ItemStHomeSectionItemBinding
import com.example.project_skripsi.databinding.ItemStHomeSectionPaymentBinding
import com.example.project_skripsi.module.student.main._sharing.agenda.*
import com.example.project_skripsi.module.student.main.calendar.StCalendarViewModel.Companion.TYPE_ASSIGNMENT
import com.example.project_skripsi.module.student.main.calendar.StCalendarViewModel.Companion.TYPE_EXAM
import com.example.project_skripsi.module.student.main.calendar.StCalendarViewModel.Companion.TYPE_MEETING
import com.example.project_skripsi.module.student.main.calendar.StCalendarViewModel.Companion.TYPE_PAYMENT
import com.example.project_skripsi.module.student.main.home.view.adapter.ItemListener

class StCalendarAdapter(
    private val dataList: List<CalendarItem>,
    private val listener: ItemListener
) :
    Adapter<ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            TYPE_MEETING -> StHomeMeetingViewHolder(
                ItemStHomeSectionItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                ), listener
            )
            TYPE_EXAM -> StHomeExamViewHolder(
                ItemStHomeSectionItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                ), listener
            )
            TYPE_ASSIGNMENT -> StHomeAssignmentViewHolder(
                ItemStHomeSectionItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                ), listener
            )
            TYPE_PAYMENT -> StHomePaymentViewHolder(
                ItemStHomeSectionPaymentBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                )
            )
            else -> StHomeAnnouncementViewHolder(
                ItemStHomeSectionAnnouncementBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                )
            )
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (dataList[position].viewType) {
            TYPE_MEETING -> (holder as StHomeMeetingViewHolder).bind(dataList[position].item)
            TYPE_EXAM -> (holder as StHomeExamViewHolder).bind(dataList[position].item)
            TYPE_ASSIGNMENT -> (holder as StHomeAssignmentViewHolder).bind(dataList[position].item)
            TYPE_PAYMENT -> (holder as StHomePaymentViewHolder).bind(dataList[position].item)
            else -> (holder as StHomeAnnouncementViewHolder).bind(dataList[position].item)
        }
    }

    override fun getItemCount(): Int = dataList.size

    override fun getItemViewType(position: Int): Int {
        return dataList[position].viewType
    }

}
