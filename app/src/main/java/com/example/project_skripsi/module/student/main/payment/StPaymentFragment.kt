package com.example.project_skripsi.module.student.main.payment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.project_skripsi.databinding.FragmentStPaymentBinding
import com.example.project_skripsi.module.student.main.payment.variant.StPaymentVariantFragment
import com.example.project_skripsi.utils.helper.CurrencyHelper
import com.google.android.material.tabs.TabLayoutMediator

class StPaymentFragment : Fragment() {

    private lateinit var viewModel: StPaymentViewModel
    private var _binding: FragmentStPaymentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStPaymentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[StPaymentViewModel::class.java]

        viewModel.totalCharge.observe(viewLifecycleOwner, {
            binding.tvTotalCharge.text = CurrencyHelper.toRupiah(it)
        })

        viewModel.totalPaid.observe(viewLifecycleOwner, {
            binding.tvTotalPaid.text = CurrencyHelper.toRupiah(it)
        })

        viewModel.accountNumber.observe(viewLifecycleOwner, {
            binding.tvAccountNumber.text = it
        })

        binding.vpContainer.adapter = ScreenSlidePagerAdapter(activity!!)
        TabLayoutMediator(binding.tabLayout, binding.vpContainer) { tab, position ->
            tab.text = StPaymentViewModel.tabHeader[position]
        }.attach()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int =
            StPaymentViewModel.tabCount

        override fun createFragment(position: Int): Fragment =
            StPaymentVariantFragment(viewModel, position)
    }

}