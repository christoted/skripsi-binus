package com.example.project_skripsi.module.student.main.calendar

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.project_skripsi.databinding.FragmentStCalendarBinding

class StCalendarFragment : Fragment() {

    private lateinit var viewModel: StCalendarViewModel
    private var _binding: FragmentStCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(StCalendarViewModel::class.java)
        _binding = FragmentStCalendarBinding.inflate(inflater, container, false)

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