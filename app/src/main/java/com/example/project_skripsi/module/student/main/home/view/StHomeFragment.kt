package com.example.project_skripsi.module.student.main.home.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.project_skripsi.databinding.FragmentStHomeBinding
import com.example.project_skripsi.module.student.main.home.viewmodel.StHomeViewModel



class StHomeFragment : Fragment() {

    private lateinit var viewModel: StHomeViewModel
    private var _binding: FragmentStHomeBinding? = null
    private val binding get() = _binding!!

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

        viewModel.profileName.observe(viewLifecycleOwner, Observer {
            binding.textviewProfileName.text = it
        })

        viewModel.profileClass.observe(viewLifecycleOwner, Observer {
            binding.textviewProfileClass.text = it
        })

        viewModel.sectionDatas.observe(viewLifecycleOwner, Observer {
//            binding.textviewProfileClass.text = it.toString()
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}