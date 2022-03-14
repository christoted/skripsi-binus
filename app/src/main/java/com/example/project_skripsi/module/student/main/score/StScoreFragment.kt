package com.example.project_skripsi.module.student.main.score

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.project_skripsi.databinding.FragmentStScoreBinding

class StScoreFragment : Fragment() {

    private lateinit var viewModel: StScoreViewModel
    private var _binding: FragmentStScoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(StScoreViewModel::class.java)
        _binding = FragmentStScoreBinding.inflate(inflater, container, false)

        viewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textHome.text = it
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}