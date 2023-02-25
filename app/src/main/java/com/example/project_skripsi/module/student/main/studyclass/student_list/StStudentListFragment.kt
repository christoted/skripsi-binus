package com.example.project_skripsi.module.student.main.studyclass.student_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentStStudentListBinding
import com.example.project_skripsi.databinding.ViewEmptyListBinding

class StStudentListFragment : Fragment() {

    private lateinit var viewModel: StStudentListViewModel
    private var _binding: FragmentStStudentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[StStudentListViewModel::class.java]
        _binding = FragmentStStudentListBinding.inflate(inflater, container, false)

        binding.rvContainer.layoutManager = LinearLayoutManager(context)
        viewModel.studentList.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                val emptyView =
                    ViewEmptyListBinding.inflate(layoutInflater, binding.llParent, false)
                emptyView.tvEmpty.text = ("Tidak ada siswa")
                binding.llParent.addView(emptyView.root)
            } else {
                binding.rvContainer.adapter = StudentListViewHolder(it).getAdapter()
            }

        })

        binding.imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }

        retrieveArgs()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs() {
        val args: StStudentListFragmentArgs by navArgs()
        viewModel.setStudyClass(args.studyClassId)
        binding.tvTitle.text = ("Siswa Kelas ${args.studyClassName}")
    }
}