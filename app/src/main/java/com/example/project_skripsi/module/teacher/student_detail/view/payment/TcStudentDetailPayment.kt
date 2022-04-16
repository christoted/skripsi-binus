package com.example.project_skripsi.module.teacher.student_detail.view.payment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.FragmentTcStudentBinding
import com.example.project_skripsi.databinding.FragmentTcStudentDetailPaymentBinding
import com.example.project_skripsi.module.teacher.student_detail.viewmodel.TcStudentDetailViewModel

class TcStudentDetailPayment(private val viewModel: TcStudentDetailViewModel) : Fragment() {
    private var _binding: FragmentTcStudentDetailPaymentBinding? = null
    private val binding get() = _binding!!
    private lateinit var paymentAdapter: TcStudentDetailPaymentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentTcStudentDetailPaymentBinding.inflate(inflater, container, false)
        //  binding.tvTest.text = this.toString().split("{")[0]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.listPaymentSection.observe(viewLifecycleOwner) {
            paymentAdapter = TcStudentDetailPaymentAdapter(it)
            with(binding) {
                rvListPayment.layoutManager = LinearLayoutManager(context)
                rvListPayment.adapter = paymentAdapter.getAdapter()
                rvListPayment.setHasFixedSize(true)
            }
        }
        viewModel.getPayments()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}