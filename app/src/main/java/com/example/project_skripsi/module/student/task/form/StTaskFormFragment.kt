package com.example.project_skripsi.module.student.task.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentStTaskFormBinding
import com.example.project_skripsi.databinding.FragmentTemplateBinding

class StTaskFormFragment : Fragment() {

    private lateinit var viewModel: StTaskFormViewModel
    private var _binding: FragmentStTaskFormBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this)[StTaskFormViewModel::class.java]
        _binding = FragmentStTaskFormBinding.inflate(inflater, container, false)

        binding.rvQuestion.layoutManager = LinearLayoutManager(context)
        viewModel.formQuestion.observe(viewLifecycleOwner, {
            binding.rvQuestion.adapter = FormAdapter(it)
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}