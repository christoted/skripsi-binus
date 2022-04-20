package com.example.project_skripsi.module.parent.student_detail.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.project_skripsi.databinding.FragmentPrCalendarBinding
import com.example.project_skripsi.databinding.FragmentPrHomeBinding

class PrCalendarFragment : Fragment() {

    private lateinit var viewModel: PrCalendarViewModel
    private var _binding: FragmentPrCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[PrCalendarViewModel::class.java]
        _binding = FragmentPrCalendarBinding.inflate(inflater, container, false)

        binding.tvTest.text = this.toString().split("{")[0]
        viewModel.text.observe(viewLifecycleOwner, {
            binding.tvTest.text = it
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}