package com.example.project_skripsi.module.common.view_image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentCmViewImageBinding

class CMViewImageFragment : Fragment() {

    private var _binding: FragmentCmViewImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCmViewImageBinding.inflate(inflater, container, false)

        binding.imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }

        retrieveArgs()

        return binding.root
    }

    private fun retrieveArgs() {
        val args: CMViewImageFragmentArgs by navArgs()

        binding.tvTitle.text = ("Foto Soal - ${args.questionNumber}")
        binding.rvContainer.layoutManager = LinearLayoutManager(context)
        binding.rvContainer.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        binding.rvContainer.adapter = ViewImageViewHolder(args.pathImages.toList()).getAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}