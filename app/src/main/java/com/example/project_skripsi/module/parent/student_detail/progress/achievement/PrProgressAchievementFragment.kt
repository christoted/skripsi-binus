package com.example.project_skripsi.module.parent.student_detail.progress.achievement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentPrProgressAchievementBinding
import com.example.project_skripsi.module.parent.student_detail.progress.PrProgressViewModel

class PrProgressAchievementFragment(private val viewModel: PrProgressViewModel) : Fragment() {

    private var _binding: FragmentPrProgressAchievementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrProgressAchievementBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        viewModel.achievements.observe(viewLifecycleOwner, {
            binding.recyclerView.adapter = PrProgressAchievementViewHolder(it).getAdapter()
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}