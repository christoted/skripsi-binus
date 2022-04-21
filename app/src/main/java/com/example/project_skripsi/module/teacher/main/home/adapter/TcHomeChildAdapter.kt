package com.example.project_skripsi.module.teacher.main.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.core.model.local.HomeMainSection
import com.example.project_skripsi.databinding.ItemStHomeSectionAnnouncementBinding
import com.example.project_skripsi.databinding.ItemStHomeSectionPaymentBinding
import com.example.project_skripsi.databinding.ItemTcAgendaGeneralBinding
import com.example.project_skripsi.module.student.main._sharing.agenda.StHomeAnnouncementViewHolder
import com.example.project_skripsi.module.student.main._sharing.agenda.StHomePaymentViewHolder
import com.example.project_skripsi.module.student.main.calendar.StCalendarViewModel
import com.example.project_skripsi.module.student.main.home.view.adapter.ItemListener
import com.example.project_skripsi.module.teacher._sharing.agenda.TcAgendaAssignmentViewHolder
import com.example.project_skripsi.module.teacher._sharing.agenda.TcAgendaExamViewHolder
import com.example.project_skripsi.module.teacher._sharing.agenda.TcAgendaItemListener
import com.example.project_skripsi.module.teacher._sharing.agenda.TcAgendaMeetingViewHolder
import com.example.project_skripsi.utils.Constant

class TcHomeChildAdapter(val item: HomeMainSection, private val listener: TcAgendaItemListener) :
RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (item.sectionName) {
            Constant.SECTION_MEETING -> TcAgendaMeetingViewHolder(
                ItemTcAgendaGeneralBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false), listener)
            Constant.SECTION_EXAM -> TcAgendaExamViewHolder(
                ItemTcAgendaGeneralBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false),listener)
            Constant.SECTION_ASSIGNMENT -> TcAgendaAssignmentViewHolder(
                ItemTcAgendaGeneralBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false),listener)
            Constant.SECTION_PAYMENT -> StHomePaymentViewHolder(
                ItemStHomeSectionPaymentBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false))
            else -> StHomeAnnouncementViewHolder(
                ItemStHomeSectionAnnouncementBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false))
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val singleData = item.sectionItem[position]
        when (item.sectionName) {
            Constant.SECTION_MEETING -> (holder as TcAgendaMeetingViewHolder).bind(singleData)
            Constant.SECTION_EXAM  -> (holder as TcAgendaExamViewHolder).bind(singleData)
            Constant.SECTION_ASSIGNMENT -> (holder as TcAgendaAssignmentViewHolder).bind(singleData)
            Constant.SECTION_PAYMENT -> (holder as StHomePaymentViewHolder).bind(singleData)
            else -> (holder as StHomeAnnouncementViewHolder).bind(singleData)
        }
    }

    override fun getItemCount(): Int {
        Log.d("88", "getItemCount: ${item.sectionItem}")
        return item.sectionItem.size
    }

}