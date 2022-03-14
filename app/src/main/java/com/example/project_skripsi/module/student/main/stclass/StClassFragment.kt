package com.example.project_skripsi.module.student.main.stclass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.project_skripsi.databinding.FragmentStClassBinding

class StClassFragment : Fragment() {

    private lateinit var viewModel: StClassViewModel
    private var _binding: FragmentStClassBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(StClassViewModel::class.java)
        _binding = FragmentStClassBinding.inflate(inflater, container, false)

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