package com.example.project_skripsi.module.teacher.study_class

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.project_skripsi.databinding.FragmentTcStudyClassHomeroomBinding
import com.example.project_skripsi.databinding.FragmentTcStudyClassTeachingBinding
import com.example.project_skripsi.module.teacher.main.study_class.TcStudyClassFragmentDirections


class TcStudyClassTeachingFragment : Fragment() {

    private var _binding: FragmentTcStudyClassTeachingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTcStudyClassTeachingBinding.inflate(inflater, container, false)
        binding.tvTest.text = this.toString().split("{")[0]

        binding.btnResource.setOnClickListener {
            val action = TcStudyClassTeachingFragmentDirections.actionTcStudyClassTeachingFragmentToTcStudyClassResourceFragment()
            view?.findNavController()?.navigate(action)
        }

        binding.btnTask.setOnClickListener {
            val action = TcStudyClassTeachingFragmentDirections.actionTcStudyClassTeachingFragmentToTcStudyClassTaskFragment()
            view?.findNavController()?.navigate(action)
        }

        binding.btnStudent.setOnClickListener{
            val action = TcStudyClassTeachingFragmentDirections.actionTcStudyClassTeachingFragmentToTcStudentFragment()
            view?.findNavController()?.navigate(action)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}