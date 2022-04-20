package com.example.project_skripsi.module.parent.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.project_skripsi.databinding.FragmentPrHomeBinding
import com.example.project_skripsi.databinding.FragmentTemplateBinding

class PrHomeFragment : Fragment() {

    private lateinit var viewModel: PrHomeViewModel
    private var _binding: FragmentPrHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[PrHomeViewModel::class.java]
        _binding = FragmentPrHomeBinding.inflate(inflater, container, false)

        binding.tvTest.text = this.toString().split("{")[0]
        viewModel.text.observe(viewLifecycleOwner, {
            binding.tvTest.text = it
        })

        binding.btnDetail.setOnClickListener {
            view?.findNavController()?.navigate(PrHomeFragmentDirections.actionPrHomeFragmentToPrStudentDetailFragment())
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}