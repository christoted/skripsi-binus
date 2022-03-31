package com.example.project_skripsi.module.teacher.study_class

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.project_skripsi.databinding.FragmentTcStudyClassHomeroomBinding


class TcStudyClassHomeroomFragment : Fragment() {

    private var _binding: FragmentTcStudyClassHomeroomBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTcStudyClassHomeroomBinding.inflate(inflater, container, false)
        binding.tvTest.text = this.toString().split("{")[0]

        binding.btnStudent.setOnClickListener{
            val action = TcStudyClassHomeroomFragmentDirections.actionTcStudyClassHomeroomFragmentToTcStudentFragment()
            view?.findNavController()?.navigate(action)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}