package com.example.project_skripsi.module.teacher.student_detail.view.attendance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.FragmentTcStudentDetailAttendanceBinding
import com.example.project_skripsi.databinding.FragmentTcStudentDetailPaymentBinding
import com.example.project_skripsi.module.student.main.score.view.adapter.ScoreContentListener
import com.example.project_skripsi.module.student.main.score.view.adapter.StScoreContentAdapter
import com.example.project_skripsi.module.teacher.student_detail.view.score.adapter.TcStudentDetailScoreAdapter
import com.example.project_skripsi.module.teacher.student_detail.viewmodel.TcStudentDetailViewModel


class TcStudentDetailAttendance(private val viewModel: TcStudentDetailViewModel) : Fragment(), ScoreContentListener {
    private var _binding: FragmentTcStudentDetailAttendanceBinding? = null
    private val binding get() = _binding!!
    private lateinit var contentAdapter: TcStudentDetailScoreAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTcStudentDetailAttendanceBinding.inflate(inflater, container, false)
        contentAdapter = TcStudentDetailScoreAdapter(viewModel,1, this)

        viewModel.sectionDatas.observe(viewLifecycleOwner) {
            with(binding.rvAttendance) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = contentAdapter
            }
        }
        viewModel.loadCurrentStudent(viewModel.studentUID)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onAttendanceTapped() {

    }
}