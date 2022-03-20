package com.example.project_skripsi.module.student.subject_detail.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentStSubjectAttendanceBinding
import com.example.project_skripsi.module.student.subject_detail.StSubjectViewModel

class StSubjectAttendanceFragment(private val viewModel : StSubjectViewModel) : Fragment() {

    private var _binding: FragmentStSubjectAttendanceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentStSubjectAttendanceBinding.inflate(inflater, container, false)
        binding.rvAttendance.layoutManager = LinearLayoutManager(context)
        viewModel.attendanceList.observe(viewLifecycleOwner, {
            binding.rvAttendance.adapter = StSubjectAttendanceAdapter(it)
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}