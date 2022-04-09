package com.example.project_skripsi.module.teacher.main.resource

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.project_skripsi.databinding.FragmentTcResourceBinding
import com.example.project_skripsi.module.teacher.main.task.TcTaskFragmentDirections

class TcResourceFragment : Fragment() {

    private var _binding: FragmentTcResourceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTcResourceBinding.inflate(inflater, container, false)
        binding.btnAdd.setOnClickListener{
            val action = TcResourceFragmentDirections.actionTcResourceFragmentToTcAlterResourceFragment()
            view?.findNavController()?.navigate(action)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}