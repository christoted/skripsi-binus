package com.example.project_skripsi.module.parent.student_detail.progress.attendance.viewholder

import android.view.LayoutInflater
import com.example.project_skripsi.core.model.firestore.AttendedMeeting
import com.example.project_skripsi.databinding.ItemPrProgressAttendanceChildBinding
import com.example.project_skripsi.utils.generic.GenericAdapter
import com.example.project_skripsi.utils.helper.DateHelper

class PrProgressAttendanceChildViewHolder(private val dataSet: List<AttendedMeeting>) {

    fun getAdapter(): GenericAdapter<AttendedMeeting> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemPrProgressAttendanceChildBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item, viewBinding, _ ->
            val view = viewBinding as ItemPrProgressAttendanceChildBinding
            with(view) {
                tvDate.text =
                    item.startTime?.let { DateHelper.getFormattedDateTime(DateHelper.DMY, it) }
                tvTime.text = ("${
                    item.startTime?.let {
                        DateHelper.getFormattedDateTime(
                            DateHelper.hm,
                            it
                        )
                    }
                } - " +
                        "${
                            item.endTime?.let {
                                DateHelper.getFormattedDateTime(
                                    DateHelper.hm,
                                    it
                                )
                            }
                        }")

                tvStatus.text = item.status
            }
        }
        return adapter
    }
}