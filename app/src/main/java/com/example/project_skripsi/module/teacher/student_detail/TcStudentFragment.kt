package com.example.project_skripsi.module.teacher.student_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.project_skripsi.databinding.FragmentTcStudentBinding
import com.example.project_skripsi.databinding.FragmentTcStudyClassHomeroomBinding


class TcStudentFragment : Fragment() {

    private var _binding: FragmentTcStudentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTcStudentBinding.inflate(inflater, container, false)
//        binding.tvTest.text = this.toString().split("{")[0]
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}