package com.example.project_skripsi.module.teacher.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.FragmentTcHomeBinding
import com.example.project_skripsi.module.teacher._sharing.agenda.TcAgendaItemListener
import com.example.project_skripsi.module.teacher.main.home.adapter.TcHomeMainAdapter
import com.example.project_skripsi.module.teacher.main.home.viewmodel.TcHomeViewModel

class TcHomeFragment : Fragment(), TcAgendaItemListener {

    private var _binding: FragmentTcHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TcHomeViewModel

    var studyClassId = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[TcHomeViewModel::class.java]
        _binding = FragmentTcHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.teacherData.observe(viewLifecycleOwner) {
            with(binding) {
                tvProfileName.text = it.name
                studyClassId = it.homeroomClass.toString()
                Glide
                    .with(root)
                    .load(it.profile)
                    .placeholder(R.drawable.profile_empty)
                    .into(ivProfilePicture)
            }
        }

        viewModel.studyClass.observe(viewLifecycleOwner) {
            with(binding) {
                tvProfileClass.text = it.name
            }
        }

        // Main Section
        with(binding.mainRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            viewModel.sectionData.observe(viewLifecycleOwner) {
                val mainSectionAdapter = TcHomeMainAdapter(it, this@TcHomeFragment)
                adapter = mainSectionAdapter
            }
        }

        binding.imvSettings.setOnClickListener {
            view.findNavController().navigate(
                TcHomeFragmentDirections.actionTcHomeFragmentToTcProfileFragment()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("12345-","destroy")
        _binding = null
    }

    override fun onTaskFormItemClicked(
        taskFormId: String,
        studyClassId: String,
        subjectName: String
    ) {
        val toTaskActivity = TcHomeFragmentDirections.actionTcHomeFragmentToTcStudyClassTaskDetailFragment(studyClassId, subjectName, taskFormId)
        view?.findNavController()?.navigate(toTaskActivity)
    }

    override fun onClassItemClicked(Position: Int) {
        // TODO: Link
    }

    override fun onMaterialItemClicked(Position: Int) {
        // TODO: Link
    }

}