package com.example.project_skripsi.module.parent.student_detail.exam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.project_skripsi.databinding.FragmentPrExamBinding
import com.example.project_skripsi.databinding.FragmentPrHomeBinding

class PrExamFragment : Fragment() {

    private lateinit var viewModel: PrExamViewModel
    private var _binding: FragmentPrExamBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[PrExamViewModel::class.java]
        _binding = FragmentPrExamBinding.inflate(inflater, container, false)

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