package com.example.project_skripsi.module.teacher.student_detail.view.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentTcStudentDetailPaymentBinding
import com.example.project_skripsi.module.teacher.student_detail.viewmodel.TcStudentDetailViewModel

class TcStudentDetailPayment(private val viewModel: TcStudentDetailViewModel) : Fragment() {
    private var _binding: FragmentTcStudentDetailPaymentBinding? = null
    private val binding get() = _binding!!
    private lateinit var paymentAdapter: TcStudentDetailPaymentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTcStudentDetailPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.sectionPayment.observe(viewLifecycleOwner) {
            paymentAdapter = TcStudentDetailPaymentAdapter(it)
            with(binding) {
                rvListPayment.layoutManager = LinearLayoutManager(context)
                rvListPayment.adapter = paymentAdapter.getAdapter()
                rvListPayment.setHasFixedSize(true)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}