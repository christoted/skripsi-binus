package com.example.project_skripsi.module.teacher.study_class.resource

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentTcStudyClassResourceBinding


class TcStudyClassResourceFragment : Fragment() {

    private lateinit var viewModel : TcStudyClassResourceViewModel
    private var _binding: FragmentTcStudyClassResourceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[TcStudyClassResourceViewModel::class.java]
        _binding = FragmentTcStudyClassResourceBinding.inflate(inflater, container, false)

        retrieveArgs()
        viewModel.studyClass.observe(viewLifecycleOwner, {
            binding.tvClassName.text = it.name
        })

        binding.rvItem.layoutManager = LinearLayoutManager(context)
        viewModel.resourceList.observe(viewLifecycleOwner, {
            binding.rvItem.adapter = ResourceViewHolder(it).getAdapter()
        })

        binding.imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs() {
        val args : TcStudyClassResourceFragmentArgs by navArgs()
        viewModel.setClassAndSubject(args.studyClassId, args.subjectName)
        binding.tvSubjectName.text = args.subjectName
    }
}