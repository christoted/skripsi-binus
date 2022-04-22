package com.example.project_skripsi.module.student.main.score.view.fragment.nilai

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentStProgressScoreBinding
import com.example.project_skripsi.module.student.main.score.view.adapter.ScoreContentListener
import com.example.project_skripsi.module.student.main.score.view.adapter.StScoreContentAdapter
import com.example.project_skripsi.module.student.main.score.viewmodel.StScoreViewModel


class StScoreContentFragment(private val viewModel: StScoreViewModel) : Fragment(), ScoreContentListener {

    private var _binding: FragmentStProgressScoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
       _binding = FragmentStProgressScoreBinding.inflate(inflater, container, false)
        viewModel.sectionDatas.observe(viewLifecycleOwner, {
            with(binding.recyclerView) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = StScoreContentAdapter(viewModel,0, this@StScoreContentFragment)
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttendanceTapped() {
        Log.d("TEST", "onAttendanceTapped: ")
    }
}

