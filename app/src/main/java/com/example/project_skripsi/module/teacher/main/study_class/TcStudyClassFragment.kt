package com.example.project_skripsi.module.teacher.main.study_class

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.project_skripsi.databinding.FragmentTcStudyClassBinding

class TcStudyClassFragment : Fragment() {

    private var _binding: FragmentTcStudyClassBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTcStudyClassBinding.inflate(inflater, container, false)

        binding.btnHomeroomTeacher.setOnClickListener{
            val action = TcStudyClassFragmentDirections.actionTcStudyClassFragmentToTcStudyClassHomeroomFragment()
            view?.findNavController()?.navigate(action)
        }

        binding.btnTeachingTeacher.setOnClickListener{
            val action = TcStudyClassFragmentDirections.actionTcStudyClassFragmentToTcStudyClassTeachingFragment()
            view?.findNavController()?.navigate(action)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}