package com.example.project_skripsi.module.teacher.student_detail.view.score

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentTcStudentDetailScoreBinding
import com.example.project_skripsi.module.student.main.score.view.adapter.ScoreContentListener
import com.example.project_skripsi.module.student.main.score.view.adapter.StScoreContentAdapter
import com.example.project_skripsi.module.teacher.student_detail.viewmodel.TcStudentDetailViewModel

class TcStudentDetailScore(private val viewModel: TcStudentDetailViewModel) : Fragment(), ScoreContentListener {
    private var _binding: FragmentTcStudentDetailScoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTcStudentDetailScoreBinding.inflate(inflater, container, false)
        viewModel.sectionDatas.observe(viewLifecycleOwner) {
            with(binding.recyclerView) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = StScoreContentAdapter(viewModel, 0, this@TcStudentDetailScore)
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