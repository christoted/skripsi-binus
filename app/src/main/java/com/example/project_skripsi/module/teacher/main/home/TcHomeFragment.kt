package com.example.project_skripsi.module.teacher.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentTcHomeBinding
import com.example.project_skripsi.module.student.main.home.view.adapter.ItemListener
import com.example.project_skripsi.module.student.main.home.view.adapter.StHomeRecyclerViewMainAdapter
import com.example.project_skripsi.module.teacher.main.home.viewmodel.TcHomeViewModel

class TcHomeFragment : Fragment(), ItemListener {

    private var _binding: FragmentTcHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TcHomeViewModel

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
        viewModel.teacherData.observe(viewLifecycleOwner, {
            with(binding) {
                tvProfileName.text = it.name
            }
        })

        viewModel.studyClass.observe(viewLifecycleOwner, {
            with(binding) {
                tvProfileClass.text = it.name
            }
        })

        // Main Section
        viewModel.sectionData.observe(viewLifecycleOwner, {
            val mainSectionAdapter = StHomeRecyclerViewMainAdapter(it, this)
            with(binding.mainRecyclerView) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = mainSectionAdapter
                addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTaskFormItemClicked(taskFormId: String) {
        // TODO: Navigation
    }

    override fun onClassItemClicked(Position: Int) {
        // TODO: Navigation
    }

    override fun onMaterialItemClicked(Position: Int) {
        // TODO: Navigation
    }
}