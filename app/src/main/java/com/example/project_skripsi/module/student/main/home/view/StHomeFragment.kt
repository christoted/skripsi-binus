package com.example.project_skripsi.module.student.main.home.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.databinding.FragmentStHomeBinding
import com.example.project_skripsi.module.student.main.home.view.adapter.ItemListener
import com.example.project_skripsi.module.student.main.home.view.adapter.StHomeRecyclerViewMainAdapter
import com.example.project_skripsi.module.student.main.home.viewmodel.StHomeViewModel
import com.example.project_skripsi.module.student.task.StTaskViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class StHomeFragment : Fragment(), ItemListener {

    private lateinit var viewModel: StHomeViewModel
    private var _binding: FragmentStHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeMainSectionAdapter: StHomeRecyclerViewMainAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this)[StHomeViewModel::class.java]
        _binding = FragmentStHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeMainSectionAdapter = StHomeRecyclerViewMainAdapter(viewModel, this)

        with(binding.recyclerviewClass) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = homeMainSectionAdapter
        }

        binding.recyclerviewClass.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

        viewModel.profileName.observe(viewLifecycleOwner, Observer {
            binding.textviewProfileName.text = it
        })

        viewModel.profileClass.observe(viewLifecycleOwner, Observer {
            binding.textviewProfileClass.text = it
        })

        viewModel.sectionDatas.observe(viewLifecycleOwner, Observer {
//            binding.textviewProfileClass.text = it.toString()
            with(binding.recyclerviewClass) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = homeMainSectionAdapter
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onExamItemClicked(Position: Int) {
        Log.d("Exam", "onExamItemClicked: " )
        val toTaskActivity = StHomeFragmentDirections.actionNavigationHomeToStTaskActivity("HaWuFgmvLAuZYeG5JuVw")
        toTaskActivity.navigationType = StTaskViewModel.NAVIGATION_FORM
        view?.findNavController()?.navigate(toTaskActivity)
    }

    override fun onAssignmentItemClicked(Position: Int) {
        Log.d("Assignment", "onAssignmentItemClicked: ")
        val toTaskActivity = StHomeFragmentDirections.actionNavigationHomeToStTaskActivity("ripyBsBZObBfarZpd085")
        toTaskActivity.navigationType = StTaskViewModel.NAVIGATION_FORM
        view?.findNavController()?.navigate(toTaskActivity)
    }

    override fun onClassItemClicked(Position: Int) {
       
    }

    override fun onMaterialItemClicked(Position: Int) {

    }
}