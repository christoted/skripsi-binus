package com.example.project_skripsi.module.student.main.payment.variant

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentStPaymentVariantBinding
import com.example.project_skripsi.module.student.main.payment.StPaymentViewModel
import kotlin.math.atan

class StPaymentVariantFragment(private val viewModel: StPaymentViewModel, private val viewType: Int) : Fragment() {

    private var _binding: FragmentStPaymentVariantBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStPaymentVariantBinding.inflate(inflater, container, false)

        binding.rvSubjectContainer.layoutManager = LinearLayoutManager(context)
        getPaymentVariant().observe(viewLifecycleOwner, {
            binding.rvSubjectContainer.adapter = StPaymentVariantViewHolder(viewType, getPaymentVariant().value!!).getAdapter()
        })

        return binding.root
    }

    private fun getPaymentVariant() : LiveData<List<String>> {
        return when (viewType) {
            StPaymentVariantViewHolder.TYPE_UPCOMING -> viewModel.upcomingPayment
            StPaymentVariantViewHolder.TYPE_UNPAID -> viewModel.unpaidPayment
            else -> viewModel.paidPayment
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}