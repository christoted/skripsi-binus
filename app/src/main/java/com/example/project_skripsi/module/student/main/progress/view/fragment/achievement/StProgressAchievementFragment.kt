package com.example.project_skripsi.module.student.main.progress.view.fragment.achievement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentStProgressAchievementBinding
import com.example.project_skripsi.databinding.ViewEmptyListBinding
import com.example.project_skripsi.module.student.main.progress.view.adapter.ScoreContentListener
import com.example.project_skripsi.module.student.main.progress.view.adapter.StScoreAchievementAdapter
import com.example.project_skripsi.module.student.main.progress.viewmodel.StScoreViewModel


class StProgressAchievementFragment(private val viewModel: StScoreViewModel) : Fragment(), ScoreContentListener {

    private lateinit var contentAdapter: StScoreAchievementAdapter
    private var _binding: FragmentStProgressAchievementBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStProgressAchievementBinding.inflate(inflater, container, false)
        contentAdapter = StScoreAchievementAdapter(viewModel)

        viewModel.sectionScore.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                val emptyView = ViewEmptyListBinding.inflate(layoutInflater, binding.llParent, false)
                emptyView.tvEmpty.text = ("Tidak ada nilai")
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

    }
}