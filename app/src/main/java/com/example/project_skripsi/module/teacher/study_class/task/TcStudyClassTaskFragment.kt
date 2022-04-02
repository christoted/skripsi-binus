package com.example.project_skripsi.module.teacher.study_class.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentTcStudyClassTaskBinding
import com.example.project_skripsi.utils.generic.ItemClickListener


class TcStudyClassTaskFragment : Fragment(), ItemClickListener {

    private lateinit var viewModel : TcStudyClassTaskViewModel
    private var _binding: FragmentTcStudyClassTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[TcStudyClassTaskViewModel::class.java]
        _binding = FragmentTcStudyClassTaskBinding.inflate(inflater, container, false)

        retrieveArgs()

        viewModel.studyClass.observe(viewLifecycleOwner, {
            binding.tvClassName.text = it.name
        })

        binding.rvExam.layoutManager = LinearLayoutManager(context)
        viewModel.examList.observe(viewLifecycleOwner, {
            binding.rvExam.adapter = TaskViewHolder(it, this).getAdapter()
        })

        binding.rvAssignment.layoutManager = LinearLayoutManager(context)
        viewModel.assignmentList.observe(viewLifecycleOwner, {
            binding.rvAssignment.adapter = TaskViewHolder(it, this).getAdapter()
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs() {
        val args : TcStudyClassTaskFragmentArgs by navArgs()
        viewModel.setClassAndSubject(args.studyClassId, args.subjectName)
        binding.tvSubjectName.text = args.subjectName
    }

    override fun onItemClick(itemId: String) {
        view?.findNavController()?.navigate(TcStudyClassTaskFragmentDirections
            .actionTcStudyClassTaskFragmentToTcStudyClassTaskDetailFragment(
                viewModel.studyClassId, viewModel.subjectName, itemId
            )
        )
    }
}