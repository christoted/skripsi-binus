package com.example.project_skripsi.module.teacher.main.study_class

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.FragmentTcStudyClassBinding
import com.google.android.material.chip.Chip

class TcStudyClassFragment : Fragment() {

    private lateinit var viewModel : TcStudyClassViewModel
    private var _binding: FragmentTcStudyClassBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[TcStudyClassViewModel::class.java]
        _binding = FragmentTcStudyClassBinding.inflate(inflater, container, false)

        binding.cpHomeroomClass.visibility = View.GONE
        viewModel.homeroomClass.observe(viewLifecycleOwner, { studyClass ->
            with(binding.cpHomeroomClass) {
                text = studyClass.name
                visibility = android.view.View.VISIBLE
                setOnClickListener {
                    view?.findNavController()?.navigate(
                        com.example.project_skripsi.module.teacher.main.study_class.TcStudyClassFragmentDirections
                        .actionTcStudyClassFragmentToTcStudyClassHomeroomFragment(studyClass.id!!))
                }
            }
        })

        viewModel.teachingSubjects.observe(viewLifecycleOwner, {
            binding.cgSubject.removeAllViews()
            var hasItem = false
            it.map { subjectName ->
                val chip = inflater.inflate(R.layout.standard_chip_choice, binding.cgSubject, false) as Chip
                chip.id = View.generateViewId()
                chip.text = subjectName
                chip.setOnCheckedChangeListener { _, isChecked ->
                    binding.cgClass.removeAllViews()
                    if (isChecked) viewModel.loadClasses(subjectName)
                }
                binding.cgSubject.addView(chip)
                if (!hasItem) {
                    chip.isChecked = true
                    viewModel.loadClasses(chip.text.toString())
                    hasItem = true
                }
            }
        })

        viewModel.teachingClasses.observe(viewLifecycleOwner, { pairData ->
            binding.cgClass.removeAllViews()
            pairData.second.map { studyClass ->
                val chip = inflater.inflate(R.layout.standard_chip_action, binding.cgSubject, false) as Chip
                chip.id = View.generateViewId()
                chip.text = studyClass.name
                chip.setOnClickListener {
                    view?.findNavController()?.navigate(TcStudyClassFragmentDirections
                        .actionTcStudyClassFragmentToTcStudyClassTeachingFragment(studyClass.id!!, pairData.first))
                }
                binding.cgClass.addView(chip)
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}