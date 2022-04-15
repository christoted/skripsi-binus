package com.example.project_skripsi.module.teacher.form.assessment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.project_skripsi.databinding.FragmentTcAssessmentTaskFormBinding
import com.example.project_skripsi.databinding.FragmentTemplateBinding

class TcAssessmentTaskFormFragment : Fragment() {

    private lateinit var viewModel: TcAssessmentTaskFormViewModel
    private var _binding: FragmentTcAssessmentTaskFormBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[TcAssessmentTaskFormViewModel::class.java]
        _binding = FragmentTcAssessmentTaskFormBinding.inflate(inflater, container, false)

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