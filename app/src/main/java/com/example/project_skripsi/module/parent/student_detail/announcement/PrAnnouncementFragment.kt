package com.example.project_skripsi.module.parent.student_detail.announcement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentPrAnnouncementBinding
class PrAnnouncementFragment : Fragment() {

    private lateinit var viewModel: PrAnnouncementViewModel
    private var _binding: FragmentPrAnnouncementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[PrAnnouncementViewModel::class.java]
        _binding = FragmentPrAnnouncementBinding.inflate(inflater, container, false)

        binding.rvContainer.layoutManager = LinearLayoutManager(context)
        viewModel.announcementList.observe(viewLifecycleOwner, {
            binding.rvContainer.adapter = AnnouncementViewHolder(it).getAdapter()
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}