package com.example.project_skripsi.module.teacher.student_detail.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.FragmentTcStudentBinding
import com.example.project_skripsi.module.teacher.student_detail.view.adapter.TcScoreViewPagerAdapter
import com.example.project_skripsi.module.teacher.student_detail.viewmodel.TcStudentDetailViewModel
import com.google.android.material.tabs.TabLayoutMediator


class TcStudentFragment : Fragment() {

    private var _binding: FragmentTcStudentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TcStudentDetailViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[TcStudentDetailViewModel::class.java]
        _binding = FragmentTcStudentBinding.inflate(inflater, container, false)

        binding.vpContainer.adapter = TcScoreViewPagerAdapter(activity!!, viewModel)
        TabLayoutMediator(binding.tabLayout, binding.vpContainer) { tab, position ->
            tab.text = TcStudentDetailViewModel.tabHeader[position]
        }.attach()

        viewModel.student.observe(viewLifecycleOwner) { student ->
            with(binding) {
                tvStudentName.text = student.name
                student.phoneNumber?.let { imvStudentPhone.setImageResource(R.drawable.whatsapp)
                    imvStudentPhone.setOnClickListener {
                        goToWhatsApp(phoneNumber = student.phoneNumber ?: "")
                    }
                }
            }
        }

        viewModel.parent.observe(viewLifecycleOwner) { parent ->
            with(binding) {
                tvParentName.text = parent.name
                parent.phoneNumber?.let {
                    imvParentPhone.setImageResource(R.drawable.whatsapp)
                    imvParentPhone.setOnClickListener {
                        goToWhatsApp(phoneNumber = parent.phoneNumber ?: "")
                    }
                }
            }
        }

        binding.imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }

        return binding.root
    }

    private fun goToWhatsApp(phoneNumber: String) {
        val url = "https://api.whatsapp.com/send/?phone=${phoneNumber}"
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retrieveArgs()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs() {
        val args: TcStudentFragmentArgs by navArgs()
        viewModel.setStudent(args.studentId)
    }
}

