package com.example.project_skripsi.module.student.main.home.profile

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
import com.example.project_skripsi.databinding.FragmentStProfileBinding
import com.example.project_skripsi.module.common.auth.AuthActivity
import com.example.project_skripsi.module.student.main.home.viewmodel.StHomeViewModel
import com.example.project_skripsi.utils.service.alarm.AlarmService
import com.example.project_skripsi.utils.service.notification.NotificationUtil
import com.example.project_skripsi.utils.service.storage.StorageSP
import com.example.project_skripsi.utils.service.storage.StorageSP.Companion.SP_DISABLE_ALARM
import com.example.project_skripsi.utils.service.storage.StorageSP.Companion.SP_DISABLE_NOTIFICATION
import com.example.project_skripsi.utils.service.storage.StorageSP.Companion.SP_EMAIL
import com.example.project_skripsi.utils.service.storage.StorageSP.Companion.SP_LOGIN_AS
import com.example.project_skripsi.utils.service.storage.StorageSP.Companion.SP_PASSWORD

class StProfileFragment : Fragment() {

    private lateinit var viewModel: StProfileViewModel
    private lateinit var homeViewModel: StHomeViewModel
    private var _binding: FragmentStProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[StProfileViewModel::class.java]
        homeViewModel = ViewModelProvider(this)[StHomeViewModel::class.java]
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

                imvLogout.setOnClickListener {
                    showConfirmationDialog()

                }
            }
        })

        viewModel.studyClass.observe(viewLifecycleOwner, { binding.tvClassName.text = it.name })
        viewModel.school.observe(viewLifecycleOwner, { binding.tvSchoolName.text = it.name })

        binding.btnNotification.setCheckedImmediately(
            !StorageSP.getBoolean(requireContext(), SP_DISABLE_NOTIFICATION, false)
        )
        binding.btnNotification.setOnCheckedChangeListener { _, b ->
            StorageSP.setBoolean(requireContext(), SP_DISABLE_NOTIFICATION, !b)
            Toast.makeText(context, "Notification ${if (b) "enabled" else "disabled"}",Toast.LENGTH_SHORT).show()
        }

        binding.btnAlarm.setCheckedImmediately(
            !StorageSP.getBoolean(requireContext(), SP_DISABLE_ALARM, false)
        )
        binding.btnAlarm.setOnCheckedChangeListener { _, b ->
            StorageSP.setBoolean(requireContext(), SP_DISABLE_ALARM, !b)
            Toast.makeText(context, "Alarm ${if (b) "enabled" else "disabled"}",Toast.LENGTH_SHORT).show()
        }

        binding.imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }

        return binding.root
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
                StorageSP.setString(requireContext(), SP_EMAIL, "")
                StorageSP.setString(requireContext(), SP_PASSWORD, "")
                StorageSP.setInt(requireContext(), SP_LOGIN_AS, -1)
                StorageSP.setBoolean(requireContext(), SP_DISABLE_NOTIFICATION, false)
                StorageSP.setBoolean(requireContext(), SP_DISABLE_ALARM, false)
                // Cancel everyday notification
                NotificationUtil.cancelDailyNotification(requireContext(), true)

                // Cancel Notification Exam, Assignment and Meeting
                homeViewModel.listHomeSectionDataClassScheduleOneWeek.observe(viewLifecycleOwner) { list ->
                    NotificationUtil.cancelAllMeetingNotification(requireActivity(), list)
                    list.map { AlarmService.inst.cancelAlarm(requireContext(), it.id, false) }
                }
                homeViewModel.listHomeSectionDataExamOneWeek.observe(viewLifecycleOwner) { list ->
                    NotificationUtil.cancelAllExamAndAssignmentNotification(requireActivity(), list)
                    list.map { AlarmService.inst.cancelAlarm(requireContext(), it.id, false) }
                }
                homeViewModel.listHomeSectionDataAssignmentOneWeek.observe(viewLifecycleOwner) { list ->
                    NotificationUtil.cancelAllExamAndAssignmentNotification(requireActivity(), list)
                    list.map { AlarmService.inst.cancelAlarm(requireContext(), it.id, false) }
                }
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