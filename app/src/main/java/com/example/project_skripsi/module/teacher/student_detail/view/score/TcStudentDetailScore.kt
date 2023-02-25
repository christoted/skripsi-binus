package com.example.project_skripsi.module.teacher.student_detail.view.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentTcStudentDetailScoreBinding
import com.example.project_skripsi.module.student.main.progress.view.adapter.ScoreContentListener
import com.example.project_skripsi.module.teacher.student_detail.view.score.adapter.TcStudentDetailScoreAdapter
import com.example.project_skripsi.module.teacher.student_detail.viewmodel.TcStudentDetailViewModel
import com.example.project_skripsi.utils.helper.UIHelper

class TcStudentDetailScore(private val viewModel: TcStudentDetailViewModel) : Fragment(), ScoreContentListener {
    private var _binding: FragmentTcStudentDetailScoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTcStudentDetailScoreBinding.inflate(inflater, container, false)
        viewModel.sectionScore.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.llParent.addView(
                    UIHelper.getEmptyList("Tidak ada nilai", inflater, binding.llParent)
                )
            } else {
                with(binding.recyclerView) {
                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(true)
                    adapter = TcStudentDetailScoreAdapter(viewModel, 0, this@TcStudentDetailScore)
                }
            }
        }
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onAttendanceTapped() {

    }

}