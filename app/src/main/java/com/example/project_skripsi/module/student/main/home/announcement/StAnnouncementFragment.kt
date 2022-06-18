package com.example.project_skripsi.module.student.main.home.announcement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentStAnnouncementBinding
import com.example.project_skripsi.databinding.ViewEmptyListBinding
import com.example.project_skripsi.module.parent.student_detail.announcement.AnnouncementViewHolder

class StAnnouncementFragment : Fragment() {

    private lateinit var viewModel: StAnnouncementViewModel
    private var _binding: FragmentStAnnouncementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[StAnnouncementViewModel::class.java]
        _binding = FragmentStAnnouncementBinding.inflate(inflater, container, false)

        binding.rvContainer.layoutManager = LinearLayoutManager(context)
        viewModel.announcementList.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                val emptyView = ViewEmptyListBinding.inflate(layoutInflater, binding.llParent, false)
                emptyView.tvEmpty.text = ("Tidak ada pengumuman")
                binding.llParent.addView(emptyView.root)
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