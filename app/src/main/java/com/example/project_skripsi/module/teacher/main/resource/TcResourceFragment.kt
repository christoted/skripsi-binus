package com.example.project_skripsi.module.teacher.main.resource

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentTcResourceBinding
import com.example.project_skripsi.module.teacher.main.resource.adapter.ResourceAdapter
import com.example.project_skripsi.module.teacher.main.resource.viewmodel.TcResourceViewModel
import com.example.project_skripsi.module.teacher.main.task.TcTaskFragmentDirections
import com.google.android.material.chip.Chip
import android.R

class TcResourceFragment : Fragment() {

    private var _binding: FragmentTcResourceBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TcResourceViewModel
    private lateinit var resourceAdapter: ResourceAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[TcResourceViewModel::class.java]
        _binding = FragmentTcResourceBinding.inflate(inflater, container, false)
        binding.btnAdd.setOnClickListener{
            val action = TcResourceFragmentDirections.actionTcResourceFragmentToTcAlterResourceFragment()
            view?.findNavController()?.navigate(action)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resourceAdapter = ResourceAdapter(viewModel)
        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = resourceAdapter
        }
        viewModel.subjectByClass.observe(viewLifecycleOwner, {
            var hasItem = false
            it.map { resource ->
                val chip =
                    this.layoutInflater.inflate(com.example.project_skripsi.R.layout.tc_item_chip, binding.chipGroup, false) as Chip
                chip.id = View.generateViewId()
                chip.text = "${resource.gradeLevel}-${resource.subjectName}"
                binding.chipGroup.addView(chip)
                chip.setOnCheckedChangeListener { chip, isChecked ->
//                    Log.d("Check", "onViewCreated: " + resource.subjectName + " status " + isChecked)
                }
                if (!hasItem) {
                    chip.isChecked = true
                    hasItem = true
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}