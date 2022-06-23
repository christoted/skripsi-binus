package com.example.project_skripsi.module.parent.student_detail.progress.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentPrProgressScoreBinding
import com.example.project_skripsi.databinding.ViewEmptyListBinding
import com.example.project_skripsi.module.parent.student_detail.progress.PrProgressViewModel
import com.example.project_skripsi.module.parent.student_detail.progress.score.viewholder.PrProgressScoreViewHolder

class PrProgressScoreFragment(private val viewModel: PrProgressViewModel) : Fragment() {

    private var _binding: FragmentPrProgressScoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrProgressScoreBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        viewModel.sectionScore.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                val emptyView =
                    ViewEmptyListBinding.inflate(layoutInflater, binding.llParent, false)
                emptyView.tvEmpty.text = ("Tidak ada nilai")
                binding.llParent.addView(emptyView.root)
            } else {
                binding.recyclerView.adapter = PrProgressScoreViewHolder(it).getAdapter()
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}