package com.example.project_skripsi.module.parent.student_detail.progress.attendance.viewholder

import android.graphics.Color
import android.view.LayoutInflater
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.local.AttendanceMainSection
import com.example.project_skripsi.databinding.ItemStProgressAttendanceBinding
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.generic.GenericAdapter

class PrProgressAttendanceViewHolder(private val dataSet : List<AttendanceMainSection>) {

    private val isExpanded = BooleanArray(dataSet.size)

    fun getAdapter(): GenericAdapter<AttendanceMainSection> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemStProgressAttendanceBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item, viewBinding, holder ->
            val view = viewBinding as ItemStProgressAttendanceBinding
            with(view) {
                tvSubjectName.text = item.subjectName
                tvPresence.text = item.totalPresence.toString()
                tvPresenceSick.text = item.totalSick.toString()
                tvPresenceLeave.text = item.totalLeave.toString()
                tvPresenceNoReason.text = item.totalAlpha.toString()

                rvContainer.layoutManager = LinearLayoutManager(root.context)
                rvContainer.adapter = PrProgressAttendanceChildViewHolder(item.sectionItem).getAdapter()
                rvContainer.addItemDecoration(DividerItemDecoration(view.root.context, androidx.recyclerview.widget.DividerItemDecoration.VERTICAL))
                rvContainer.isVisible = isExpanded[holder.absoluteAdapterPosition]
                root.setOnClickListener {
                    isExpanded[holder.absoluteAdapterPosition] =
                        !isExpanded[holder.absoluteAdapterPosition]
                    rvContainer.isVisible = isExpanded[holder.absoluteAdapterPosition]
                }

                viewIndicator.setBackgroundColor(ResourcesCompat.getColor(App.resourses!!, R.color.indicator_attendance, null))
            }
        }
        return adapter
    }
}