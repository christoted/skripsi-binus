package com.example.project_skripsi.module.teacher.resource.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.project_skripsi.databinding.FragmentTcAlterResourceBinding
import com.example.project_skripsi.module.teacher.main.resource.adapter.ResourceAdapter


class TcAlterResourceFragment : Fragment() {

    private var _binding: FragmentTcAlterResourceBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TcAlterResourceViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTcAlterResourceBinding.inflate(inflater, container, false)
        //binding.tvTest.text = this.toString().split("{")[0]
        viewModel = ViewModelProvider(this)[TcAlterResourceViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrieveArgs()

        submit()
    }

    private fun submit() {
        binding.btnUpload.setOnClickListener {
            viewModel.submitResource("Test", "Slide", "https://www.google.com")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs() {
        val args: TcAlterResourceFragmentArgs by navArgs()
        with(binding) {
            tvClass.text = ("${args.subjectName} - ${args.gradeLevel}")
        }
        viewModel.initData(args.subjectName, args.gradeLevel)
    }
}