package com.example.project_skripsi.module.teacher.study_class

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.project_skripsi.databinding.FragmentTcStudyClassHomeroomBinding
import com.example.project_skripsi.databinding.FragmentTcStudyClassTaskDetailBinding
import com.example.project_skripsi.databinding.FragmentTcStudyClassTeachingBinding


class TcStudyClassTaskDetailFragment : Fragment() {

    private var _binding: FragmentTcStudyClassTaskDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTcStudyClassTaskDetailBinding.inflate(inflater, container, false)
        binding.tvTest.text = this.toString().split("{")[0]
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}