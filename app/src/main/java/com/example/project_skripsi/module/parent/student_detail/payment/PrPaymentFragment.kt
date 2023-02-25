package com.example.project_skripsi.module.parent.student_detail.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.project_skripsi.databinding.FragmentPrPaymentBinding
import com.example.project_skripsi.module.parent.student_detail.payment.variant.PrPaymentVariantFragment
import com.example.project_skripsi.module.student.main.payment.StPaymentViewModel
import com.example.project_skripsi.utils.helper.CurrencyHelper
import com.google.android.material.tabs.TabLayoutMediator

class PrPaymentFragment : Fragment() {

    private lateinit var viewModel: PrPaymentViewModel
    private var _binding: FragmentPrPaymentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[PrPaymentViewModel::class.java]
        _binding = FragmentPrPaymentBinding.inflate(inflater, container, false)

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

        binding.imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }

        retrieveArgs()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs() {
        val args: PrPaymentFragmentArgs by navArgs()
        viewModel.setStudent(args.studentId)
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int =
            PrPaymentViewModel.tabCount

        override fun createFragment(position: Int): Fragment =
            PrPaymentVariantFragment(viewModel, position)
    }

}