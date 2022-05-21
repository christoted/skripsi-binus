package com.example.project_skripsi.module.teacher.student_detail.view.score.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.Achievement
import com.example.project_skripsi.core.model.local.AttendanceMainSection
import com.example.project_skripsi.core.model.local.Score
import com.example.project_skripsi.core.model.local.ScoreMainSection
import com.example.project_skripsi.databinding.ItemStProgressAchievementBinding
import com.example.project_skripsi.databinding.ItemStProgressAttendanceBinding
import com.example.project_skripsi.databinding.ItemStProgressScoreBinding
import com.example.project_skripsi.module.student.main.score.view.StScoreFragmentDirections
import com.example.project_skripsi.module.student.main.score.view.adapter.ScoreContentListener
import com.example.project_skripsi.module.student.main.score.view.adapter.StScoreContentChildAdapter
import com.example.project_skripsi.module.student.main.score.viewmodel.StScoreViewModel
import com.example.project_skripsi.module.teacher.student_detail.viewmodel.TcStudentDetailViewModel
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.generic.GenericAdapter

class TcStudentDetailScoreAdapter(private val viewModel: TcStudentDetailViewModel, private val tab: Int, private val listener: ScoreContentListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val isExpanded = BooleanArray(viewModel.sectionDatas.value?.size ?: 0)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(tab) {
            0 -> {
                val item = ItemStProgressScoreBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return StScoreContentViewHolder(item)
            }
            1 -> {
                val item = ItemStProgressAttendanceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return StScoreAttendanceViewHolder(item)
            }
            else -> {
                val item = ItemStProgressAchievementBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return StScoreAchievementViewHolder(item)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(tab) {
            0 -> {
                viewModel.sectionDatas.value?.let {
                    Log.d("Data Subject", ": " + position)
                    val singleData = it[position]
                    val adapter = TcStudentDetailScoreChildAdapter(viewModel, singleData)
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

            }
        }
    }

    override fun getItemCount(): Int = viewModel.sectionDatas.value?.size ?: 0

    inner class StScoreContentViewHolder(private val binding: ItemStProgressScoreBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ScoreMainSection, adapter: TcStudentDetailScoreChildAdapter, position: Int) {
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

    inner class StScoreAttendanceViewHolder(private val binding: ItemStProgressAttendanceBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AttendanceMainSection) {
            with(binding) {
                //title.text = item
                tvSubjectName.text = item.subjectName
                tvPresence.text = item.totalPresence.toString()
                tvPresenceSick.text = item.totalSick.toString()
                tvPresenceLeave.text = item.totalLeave.toString()
                tvPresenceNoReason.text = item.totalAlpha.toString()
                viewIndicator.setBackgroundColor(ResourcesCompat.getColor(App.resourses!!, R.color.indicator_attendance, null))
//                root.setOnClickListener {
//                    listener.onAttendanceTapped()
//                    val toStSubjectActivity = StScoreFragmentDirections.actionNavigationScoreFragmentToStSubjectActivity()
//                    toStSubjectActivity.subjectName = item.subjectName
//                    it.findNavController().navigate(toStSubjectActivity)
//                }
            }
        }
    }

    inner class StScoreAchievementViewHolder(private val binding: ItemStProgressAchievementBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Achievement) {
            with(binding) {
                tvTitle.text = item.title
                tvDescription.text = item.description
            }
        }
    }


}