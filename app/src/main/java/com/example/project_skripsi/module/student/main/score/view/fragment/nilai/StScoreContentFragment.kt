package com.example.project_skripsi.module.student.main.score.view.fragment.nilai

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.FragmentStScoreContentBinding
import com.example.project_skripsi.module.student.main.score.view.adapter.StScoreContentAdapter
import com.example.project_skripsi.module.student.main.score.viewmodel.StScoreViewModel


class StScoreContentFragment(private val viewModel: StScoreViewModel) : Fragment() {

    private lateinit var contentAdapter: StScoreContentAdapter
    private var _binding: FragmentStScoreContentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       _binding = FragmentStScoreContentBinding.inflate(inflater, container, false)
        contentAdapter = StScoreContentAdapter(viewModel,0)

        viewModel.sectionDatas.observe(viewLifecycleOwner, {
            with(binding.recyclerView) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = contentAdapter
            }
        })

        return binding.root
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
