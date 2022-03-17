package com.example.project_skripsi.module.student.main.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.ItemStCalendarBinding
import com.example.project_skripsi.databinding.ItemStPaymentVariantBinding
import java.util.*

class StCalendarAdapter(private val dayEventList: List<DayEvent>) :
    RecyclerView.Adapter<StCalendarAdapter.CalendarViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CalendarViewHolder {
        return CalendarViewHolder(
            ItemStCalendarBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false))
    }


    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(dayEventList[position])
    }

    override fun getItemCount(): Int = dayEventList.size


    inner class CalendarViewHolder(private val binding : ItemStCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dayEvent: DayEvent) {
            with(binding) {
                tvName.text = dayEvent.viewType.toString()
                val c : Calendar = Calendar.getInstance().let {
                    it.time = dayEvent.eventTime
                    it
                }

                tvDate.text = "${c.get(Calendar.HOUR_OF_DAY)} ${c.get(Calendar.MINUTE)}"
            }

        }
    }
}
