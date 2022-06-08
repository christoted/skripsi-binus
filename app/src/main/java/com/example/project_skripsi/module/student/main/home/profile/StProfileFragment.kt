package com.example.project_skripsi.module.student.main.home.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.project_skripsi.databinding.FragmentStProfileBinding
import com.example.project_skripsi.module.common.auth.AuthActivity
import com.example.project_skripsi.service.StorageSP
import com.example.project_skripsi.service.StorageSP.Companion.SP_EMAIL
import com.example.project_skripsi.service.StorageSP.Companion.SP_LOGIN_AS
import com.example.project_skripsi.service.StorageSP.Companion.SP_PASSWORD

class StProfileFragment : Fragment() {

    private lateinit var viewModel: StProfileViewModel
    private var _binding: FragmentStProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[StProfileViewModel::class.java]
        _binding = FragmentStProfileBinding.inflate(inflater, container, false)

        viewModel.student.observe(viewLifecycleOwner, {
            with(binding) {
                tvProfileName.text = it.name
                tvNis.text = it.nis

                tvAttendanceNumber.text = it.attendanceNumber.toString()

                tvAddress.text = it.address
                tvPhoneNumber.text = it.phoneNumber

                tvAge.text = it.age.toString()
                tvGender.text = it.gender

                Glide
                    .with(root.context)
                    .load(it.profile)
                    .into(ivProfilePicture)
            }
        })

        viewModel.studyClass.observe(viewLifecycleOwner, { binding.tvClassName.text = it.name })
        viewModel.school.observe(viewLifecycleOwner, { binding.tvSchoolName.text = it.name })

        binding.imvLogout.setOnClickListener {
            StorageSP.set(requireActivity(), SP_EMAIL, "")
            StorageSP.set(requireActivity(), SP_PASSWORD, "")
            StorageSP.setInt(requireActivity(), SP_LOGIN_AS, -1)
            val intent = Intent(binding.root.context, AuthActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        binding.imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}