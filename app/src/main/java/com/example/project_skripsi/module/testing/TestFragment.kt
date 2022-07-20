package com.example.project_skripsi.module.testing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.project_skripsi.databinding.FragmentTemplateBinding
import com.example.project_skripsi.databinding.FragmentTestBinding
import com.example.project_skripsi.module.template.TemplateViewModel

class TestFragment : Fragment() {

    private lateinit var viewModel: TestViewModel
    private var _binding: FragmentTestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[TestViewModel::class.java]
        _binding = FragmentTestBinding.inflate(inflater, container, false)

        viewModel.vvLiveData.observe(viewLifecycleOwner, {
            binding.tvTest.text = it.toString()
            binding.tvTest2.text = (binding.tvTest2.text.toString().toInt() + 1).toString()
        })

        binding.btnWoi.setOnClickListener {
            viewModel.increase()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}