package com.example.project_skripsi.module.teacher.study_class

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.project_skripsi.databinding.FragmentTcStudyClassHomeroomBinding
import com.example.project_skripsi.databinding.FragmentTcStudyClassResourcceBinding
import com.example.project_skripsi.databinding.FragmentTcStudyClassTeachingBinding


class TcStudyClassResourceFragment : Fragment() {

    private var _binding: FragmentTcStudyClassResourcceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTcStudyClassResourcceBinding.inflate(inflater, container, false)
        binding.tvTest.text = this.toString().split("{")[0]
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}