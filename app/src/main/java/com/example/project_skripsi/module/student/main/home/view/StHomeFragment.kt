package com.example.project_skripsi.module.student.main.home.view

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
import com.example.project_skripsi.databinding.FragmentStHomeBinding
import com.example.project_skripsi.module.student.main.home.view.adapter.ItemListener
import com.example.project_skripsi.module.student.main.home.view.adapter.StHomeRecyclerViewMainAdapter
import com.example.project_skripsi.module.student.main.home.viewmodel.StHomeViewModel
import com.example.project_skripsi.module.student.task.StTaskViewModel


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



        viewModel.profileName.observe(viewLifecycleOwner, { binding.tvProfileName.text = it })
        viewModel.profileClass.observe(viewLifecycleOwner, { binding.tvProfileClass.text = it })

        viewModel.sectionData.observe(viewLifecycleOwner, {
            homeMainSectionAdapter = StHomeRecyclerViewMainAdapter(it, this)
            with(binding.recyclerviewClass) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = homeMainSectionAdapter
                addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTaskFormItemClicked(taskFormId: String) {
        Log.d("TaskForm", "onTaskFormItemClicked: ")
        val toTaskActivity = StHomeFragmentDirections.actionNavigationHomeToStTaskActivity(taskFormId)
        toTaskActivity.navigationType = StTaskViewModel.NAVIGATION_FORM
        view?.findNavController()?.navigate(toTaskActivity)
    }

    override fun onClassItemClicked(Position: Int) {
       
    }

    override fun onMaterialItemClicked(Position: Int) {

    }
}