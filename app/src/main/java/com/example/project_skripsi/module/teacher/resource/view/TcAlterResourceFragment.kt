package com.example.project_skripsi.module.teacher.resource.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.project_skripsi.databinding.FragmentTcAlterResourceBinding
import com.example.project_skripsi.module.teacher.main.resource.adapter.ResourceAdapter


class TcAlterResourceFragment : Fragment() {

    private var _binding: FragmentTcAlterResourceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTcAlterResourceBinding.inflate(inflater, container, false)
        binding.tvTest.text = this.toString().split("{")[0]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}