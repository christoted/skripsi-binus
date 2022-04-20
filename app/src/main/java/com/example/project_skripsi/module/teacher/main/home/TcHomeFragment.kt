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
import com.example.project_skripsi.databinding.FragmentTcHomeBinding
import com.example.project_skripsi.module.student.main.home.view.StHomeFragmentDirections
import com.example.project_skripsi.module.student.main.home.view.adapter.ItemListener
import com.example.project_skripsi.module.student.main.home.view.adapter.StHomeRecyclerViewMainAdapter
import com.example.project_skripsi.module.student.task.StTaskViewModel
import com.example.project_skripsi.module.teacher._sharing.agenda.TcAgendaItemListener
import com.example.project_skripsi.module.teacher.main.home.adapter.TcHomeMainAdapter
import com.example.project_skripsi.module.teacher.main.home.viewmodel.TcHomeViewModel
import com.example.project_skripsi.module.teacher.study_class.homeroom.TcStudyClassHomeroomFragmentDirections

class TcHomeFragment : Fragment(), TcAgendaItemListener {

    private var _binding: FragmentTcHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TcHomeViewModel

    var studyClassId = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            }
        }

        viewModel.studyClass.observe(viewLifecycleOwner) {
            with(binding) {
                tvProfileClass.text = it.name
            }
        }

        // Main Section
        viewModel.sectionData.observe(viewLifecycleOwner) {
            val mainSectionAdapter = TcHomeMainAdapter(it, this)
            with(binding.mainRecyclerView) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = mainSectionAdapter
                addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
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