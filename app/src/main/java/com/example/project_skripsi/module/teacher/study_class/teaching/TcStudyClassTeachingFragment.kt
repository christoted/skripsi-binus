package com.example.project_skripsi.module.teacher.study_class.teaching

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.FragmentTcStudyClassTeachingBinding
import com.example.project_skripsi.module.teacher.study_class.homeroom.HomeroomStudentViewHolder


class TcStudyClassTeachingFragment : Fragment() {

    private lateinit var viewModel : TcStudyClassTeachingViewModel
    private var _binding: FragmentTcStudyClassTeachingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[TcStudyClassTeachingViewModel::class.java]
        _binding = FragmentTcStudyClassTeachingBinding.inflate(inflater, container, false)

        retrieveArgs()

        viewModel.studyClass.observe(viewLifecycleOwner, {
            binding.tvClassName.text = it.name
        })

        viewModel.classChief.observe(viewLifecycleOwner, {
            with(binding) {
                tvChiefName.text = it.name
                it.phoneNumber?.let { imvChiefPhone.setImageResource(R.drawable.whatsapp) }
            }
        })

        binding.rvItem.layoutManager = LinearLayoutManager(context)
        viewModel.studentList.observe(viewLifecycleOwner, {
            binding.rvItem.adapter = TeachingStudentViewHolder(viewModel, it).getAdapter()
        })

        binding.btnResource.setOnClickListener {
            view?.findNavController()?.navigate(TcStudyClassTeachingFragmentDirections
                .actionTcStudyClassTeachingFragmentToTcStudyClassResourceFragment(
                    viewModel.studyClassId, viewModel.subjectName
                )
            )
        }

        binding.btnTask.setOnClickListener {
            view?.findNavController()?.navigate(TcStudyClassTeachingFragmentDirections
                .actionTcStudyClassTeachingFragmentToTcStudyClassTaskFragment(
                    viewModel.studyClassId, viewModel.subjectName
                )
            )
        }

        binding.imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs() {
        val args = "eMsulnik6kEpW0ESKI9V"
        val subjectName = "Biologi"
        viewModel.setClassAndSubject(args, subjectName)
        binding.tvRole.text = ("Guru $subjectName")
    }

}