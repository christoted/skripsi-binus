package com.example.project_skripsi.module.parent.student_detail.announcement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentPrAnnouncementBinding
import com.example.project_skripsi.utils.helper.UIHelper

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
            if (it.isEmpty()) {
                binding.llParent.addView(
                    UIHelper.getEmptyList(
                        "Tidak ada pengumuman",
                        inflater, binding.llParent
                    )
                )
            } else {
                binding.rvContainer.adapter = AnnouncementViewHolder(it).getAdapter()
            }
        })

        binding.imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}