package com.example.project_skripsi.module.parent.student_detail.payment.variant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.core.model.firestore.Payment
import com.example.project_skripsi.databinding.FragmentStPaymentVariantBinding
import com.example.project_skripsi.databinding.ViewRecyclerViewBinding
import com.example.project_skripsi.module.parent.student_detail.payment.PrPaymentViewModel

class PrPaymentVariantFragment(private val viewModel: PrPaymentViewModel, private val viewType: Int) : Fragment() {

    private var _binding: ViewRecyclerViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ViewRecyclerViewBinding.inflate(inflater, container, false)

        binding.rvContainer.layoutManager = LinearLayoutManager(context)
        getPaymentVariant().observe(viewLifecycleOwner, {
            binding.rvContainer.adapter = PrPaymentVariantViewHolder(viewType, getPaymentVariant().value!!).getAdapter()
        })

        return binding.root
    }

    private fun getPaymentVariant() : LiveData<List<Payment>> {
        return when (viewType) {
            PrPaymentVariantViewHolder.TYPE_UPCOMING -> viewModel.upcomingPayment
            PrPaymentVariantViewHolder.TYPE_UNPAID -> viewModel.unpaidPayment
            else -> viewModel.paidPayment
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}