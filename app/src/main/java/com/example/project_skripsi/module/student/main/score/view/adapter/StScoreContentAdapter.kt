package com.example.project_skripsi.module.student.main.score.view.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.core.model.firestore.AssignedTaskForm
import com.example.project_skripsi.core.model.local.AttendanceMainSection
import com.example.project_skripsi.core.model.local.ScoreMainSection
import com.example.project_skripsi.core.model.local.ScoreSectionData
import com.example.project_skripsi.databinding.ItemStScoreAbsensiBinding
import com.example.project_skripsi.databinding.ItemStScoreContentBinding
import com.example.project_skripsi.databinding.ItemStScorePencapaianBinding
import com.example.project_skripsi.module.student.main.score.view.StScoreFragmentDirections
import com.example.project_skripsi.module.student.main.score.viewmodel.StScoreViewModel
import com.example.project_skripsi.module.student.main.studyclass.StClassFragmentDirections

interface ScoreContentListener {
    fun onAttendanceTapped()
}


class StScoreContentAdapter(private val viewModel: StScoreViewModel, private val tab: Int, private val listener: ScoreContentListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val isExpanded = BooleanArray(viewModel.sectionDatas.value?.size ?: 0)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(tab) {
            0 -> {
                val item = ItemStScoreContentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return StScoreContentViewHolder(item)
            }
            1 -> {
                val item = ItemStScoreAbsensiBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return StScoreAttendanceViewHolder(item)
            }
            2 -> {
                val item = ItemStScorePencapaianBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return StScoreAchievementViewHolder(item)
            }
        }
        val item = ItemStScoreContentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StScoreContentViewHolder(item)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(tab) {
            0 -> {
                viewModel.sectionDatas.value?.let {
                    Log.d("Data Subject", ": " + position)
                    val singleData = it[position]
                    val adapter = StScoreContentChildAdapter(viewModel, singleData)
                    (holder as StScoreContentViewHolder).bind(singleData, adapter, position)
                }
            }
            1 -> {
                viewModel.sectionAttendances.value?.let {
                    val singleData = it[position]
                    (holder as StScoreAttendanceViewHolder).bind(singleData)
                }
            }
            2 -> {
                viewModel.sectionDatas.value?.let {
                    val singleData =  it[position]
                    (holder as StScoreAchievementViewHolder).bind(singleData)
                }
            }
        }


    }


    override fun getItemCount(): Int = viewModel.sectionDatas.value?.size?: 0


    inner class StScoreContentViewHolder(private val binding: ItemStScoreContentBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ScoreMainSection, adapter: StScoreContentChildAdapter, position: Int) {
            with(binding) {
                subject.text = item.subjectName
                tvMid.text =  item.mid_exam?.toString() ?: "-"
                tvFinal.text = item.final_exam?.toString() ?: "-"
                tvAssignment.text = item.total_assignment?.toString() ?: "-"
                tvTotal.text = item.total_score?.toString() ?: "-"
                with(binding.sectionItemsRecyclerView) {
                    sectionItemsRecyclerView.layoutManager = LinearLayoutManager(context)
                    sectionItemsRecyclerView.adapter = adapter
                    binding.sectionItemsRecyclerView.addItemDecoration(DividerItemDecoration(itemView.context, DividerItemDecoration.VERTICAL))
                }
                sectionItemsRecyclerView.isVisible = isExpanded[position]
                root.setOnClickListener {
                    isExpanded[position] = !isExpanded[position]
                    sectionItemsRecyclerView.isVisible = isExpanded[position]
                }
                viewIndicator.setBackgroundColor(Color.parseColor("#0000FF"))
            }
        }
    }

    inner class StScoreAttendanceViewHolder(private val binding: ItemStScoreAbsensiBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AttendanceMainSection) {
            with(binding) {
                //title.text = item
                tvSubjectName.text = item.subjectName
                tvPresence.text = item.totalPresence.toString()
                tvPresenceSick.text = item.totalSick.toString()
                tvPresenceLeave.text = item.totalLeave.toString()
                tvPresenceNoReason.text = item.totalAlpha.toString()
                viewIndicator.setBackgroundColor(Color.parseColor("#006400"))
                root.setOnClickListener {
                    listener.onAttendanceTapped()
                    val toStSubjectActivity = StScoreFragmentDirections.actionNavigationScoreFragmentToStSubjectActivity()
                    toStSubjectActivity.subjectName = item.subjectName
                    it.findNavController().navigate(toStSubjectActivity)
                }
            }
        }
    }

    inner class StScoreAchievementViewHolder(private val binding: ItemStScorePencapaianBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ScoreMainSection) {
            with(binding) {

            }
        }
    }


}