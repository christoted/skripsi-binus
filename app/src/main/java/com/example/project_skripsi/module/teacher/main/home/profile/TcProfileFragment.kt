package com.example.project_skripsi.module.teacher.main.home.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.databinding.FragmentTcProfileBinding
import com.example.project_skripsi.module.common.auth.AuthActivity
import com.example.project_skripsi.module.teacher.main.home.viewmodel.TcHomeViewModel
import com.example.project_skripsi.utils.service.notification.NotificationUtil
import com.example.project_skripsi.utils.service.storage.StorageSP

class TcProfileFragment : Fragment() {

    private lateinit var viewModel: TcProfileViewModel
    private lateinit var homeViewModel: TcHomeViewModel
    private var _binding: FragmentTcProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[TcProfileViewModel::class.java]
        homeViewModel = ViewModelProvider(this)[TcHomeViewModel::class.java]
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

                imvLogout.setOnClickListener {
                    showConfirmationDialog()
                }
            }
        })

        viewModel.studyClass.observe(viewLifecycleOwner, { binding.tvClassName.text = it.name })
        viewModel.school.observe(viewLifecycleOwner, { binding.tvSchoolName.text = it.name })

        binding.btnNotification.setCheckedImmediately(
            !StorageSP.getBoolean(requireContext(), StorageSP.SP_DISABLE_NOTIFICATION, false)
        )
        binding.btnNotification.setOnCheckedChangeListener { _, b ->
            StorageSP.setBoolean(requireContext(), StorageSP.SP_DISABLE_NOTIFICATION, !b)
            Toast.makeText(context, "Notification ${if (b) "enabled" else "disabled"}", Toast.LENGTH_SHORT).show()
        }

        binding.imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }

        return binding.root
    }

    private fun cancelNotification() {
        // Everyday
        NotificationUtil.cancelDailyNotification(requireActivity(), false)
        // Exam
        homeViewModel.examList.observe(viewLifecycleOwner) {
            NotificationUtil.cancelAllExamAndAssignmentNotificationTeacher(requireActivity(), it)
        }
        // Assignment
        homeViewModel.assignmentList.observe(viewLifecycleOwner) {
            NotificationUtil.cancelAllExamAndAssignmentNotificationTeacher(requireActivity(), it)
        }
        // Meeting
        homeViewModel.listMeeting.observe(viewLifecycleOwner) {
            NotificationUtil.cancelAllMeetingNotificationTeacher(requireActivity(), it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Konfirmasi")
            .setMessage("Apakah anda yakin untuk logout?")
            .setPositiveButton("Ok") { _, _ ->
                StorageSP.setString(requireContext(), StorageSP.SP_EMAIL, "")
                StorageSP.setString(requireContext(), StorageSP.SP_PASSWORD, "")
                StorageSP.setInt(requireContext(), StorageSP.SP_LOGIN_AS, -1)
                StorageSP.setBoolean(requireContext(), StorageSP.SP_DISABLE_NOTIFICATION, false)
                cancelNotification()
                AuthRepository.inst.logOut()
                val intent = Intent(binding.root.context, AuthActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
            .setNegativeButton("Batal") { _, _ -> }
            .create()
            .show()
    }
}
