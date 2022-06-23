package com.example.project_skripsi.module.student.main.progress.view.fragment.attendance

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentStProgressAttendanceBinding
import com.example.project_skripsi.databinding.ViewEmptyListBinding
import com.example.project_skripsi.module.student.main.progress.view.adapter.ScoreContentListener
import com.example.project_skripsi.module.student.main.progress.view.adapter.StScoreContentAdapter
import com.example.project_skripsi.module.student.main.progress.viewmodel.StScoreViewModel


class StProgressAttendanceFragment(private val viewModel: StScoreViewModel) : Fragment(),
    ScoreContentListener {

    private lateinit var contentAdapter: StScoreContentAdapter
    private var _binding: FragmentStProgressAttendanceBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStProgressAttendanceBinding.inflate(inflater, container, false)
        contentAdapter = StScoreContentAdapter(viewModel, 1, this)

        viewModel.sectionScore.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                val emptyView =
                    ViewEmptyListBinding.inflate(layoutInflater, binding.llParent, false)
                emptyView.tvEmpty.text = ("Tidak ada absensi")
                binding.llParent.addView(emptyView.root)
            } else {
                with(binding.recyclerView) {
                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(true)
                    adapter = contentAdapter
                }
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