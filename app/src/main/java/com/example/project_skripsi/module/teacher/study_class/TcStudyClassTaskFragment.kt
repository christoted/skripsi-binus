package com.example.project_skripsi.module.teacher.study_class

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.project_skripsi.databinding.FragmentTcStudyClassHomeroomBinding
import com.example.project_skripsi.databinding.FragmentTcStudyClassTaskBinding
import com.example.project_skripsi.databinding.FragmentTcStudyClassTeachingBinding


class TcStudyClassTaskFragment : Fragment() {

    private var _binding: FragmentTcStudyClassTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTcStudyClassTaskBinding.inflate(inflater, container, false)
        binding.tvTest.text = this.toString().split("{")[0]

        binding.btnTask.setOnClickListener {
            val action = TcStudyClassTaskFragmentDirections.actionTcStudyClassTaskFragmentToTcStudyClassTaskDetailFragment()
            view?.findNavController()?.navigate(action)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}