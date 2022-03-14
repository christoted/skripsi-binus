package com.example.project_skripsi.module.student.main.stclass.subject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.project_skripsi.databinding.FragmentStClassSubjectBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

class StClassSubjectFragment(private val subjectList: List<String>) : Fragment() {

    private var _binding: FragmentStClassSubjectBinding? = null
    private val binding get() = _binding!!

    init {
        Log.d("12345", "oi init")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("12345", "frag create")
        _binding = FragmentStClassSubjectBinding.inflate(inflater, container, false)

        binding.rvSubjectContainer.layoutManager = GridLayoutManager(context, 4)
        binding.rvSubjectContainer.adapter = SubjectAdapter(subjectList)

        return binding.root
    }

    override fun onDestroyView() {
        Log.d("12345", "frag dest")
        super.onDestroyView()
        _binding = null
    }
}