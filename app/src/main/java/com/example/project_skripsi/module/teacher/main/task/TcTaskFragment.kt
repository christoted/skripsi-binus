package com.example.project_skripsi.module.teacher.main.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.project_skripsi.databinding.FragmentTcTaskBinding
import com.example.project_skripsi.module.teacher.main.study_class.TcStudyClassFragmentDirections

class TcTaskFragment : Fragment() {

    private var _binding: FragmentTcTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTcTaskBinding.inflate(inflater, container, false)
        binding.btnAdd.setOnClickListener{
            val action = TcTaskFragmentDirections.actionTcTaskFragmentToTcAlterTaskFormFragment()
            view?.findNavController()?.navigate(action)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}