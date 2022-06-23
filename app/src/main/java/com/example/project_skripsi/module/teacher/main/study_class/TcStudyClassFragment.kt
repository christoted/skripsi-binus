package com.example.project_skripsi.module.teacher.main.study_class

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.FragmentTcStudyClassBinding
import com.google.android.material.chip.Chip

class TcStudyClassFragment : Fragment(), ClassClickListener {

    private lateinit var viewModel: TcStudyClassViewModel
    private var _binding: FragmentTcStudyClassBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[TcStudyClassViewModel::class.java]
        _binding = FragmentTcStudyClassBinding.inflate(inflater, container, false)

        binding.llParent.visibility = View.GONE
        viewModel.homeroomClass.observe(viewLifecycleOwner, { studyClass ->
            with(binding) {
                tvClassName.text = studyClass.name
                tvStudentCount.text = (studyClass.students?.size ?: 0).toString()
                llParent.visibility = View.VISIBLE
                llParent.setOnClickListener {
                    view?.findNavController()?.navigate(
                        TcStudyClassFragmentDirections
                            .actionTcStudyClassFragmentToTcStudyClassHomeroomFragment(studyClass.id!!)
                    )
                }
            }
        })

        viewModel.teachingSubjects.observe(viewLifecycleOwner, {
            binding.cgSubject.removeAllViews()
            var hasItem = false
            it.map { subjectName ->
                val chip =
                    inflater.inflate(R.layout.view_chip_choice, binding.cgSubject, false) as Chip
                chip.id = View.generateViewId()
                chip.text = subjectName
                chip.setOnCheckedChangeListener { _, isChecked ->
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

        binding.rvClass.layoutManager = LinearLayoutManager(context)
        viewModel.teachingClasses.observe(viewLifecycleOwner, { pairData ->
            binding.rvClass.adapter = TcStudyClassViewHolder(
                pairData.second,
                pairData.first,
                this@TcStudyClassFragment
            ).getAdapter()
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(classId: String, subjectName: String) {
        view?.findNavController()?.navigate(
            TcStudyClassFragmentDirections
                .actionTcStudyClassFragmentToTcStudyClassTeachingFragment(classId, subjectName)
        )
    }
}