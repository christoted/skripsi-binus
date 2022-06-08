package com.example.project_skripsi.module.teacher.main.home.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.project_skripsi.databinding.FragmentTcProfileBinding
import com.example.project_skripsi.module.common.auth.AuthActivity
import com.example.project_skripsi.service.StorageSP

class TcProfileFragment : Fragment() {

    private lateinit var viewModel: TcProfileViewModel
    private var _binding: FragmentTcProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[TcProfileViewModel::class.java]
        _binding = FragmentTcProfileBinding.inflate(inflater, container, false)

        viewModel.teacher.observe(viewLifecycleOwner, {
            with(binding) {
                tvProfileName.text = it.name
                tvPhoneNumber.text = it.phoneNumber
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
            StorageSP.set(requireActivity(), StorageSP.SP_EMAIL, "")
            StorageSP.set(requireActivity(), StorageSP.SP_PASSWORD, "")
            StorageSP.setInt(requireActivity(), StorageSP.SP_LOGIN_AS, -1)
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