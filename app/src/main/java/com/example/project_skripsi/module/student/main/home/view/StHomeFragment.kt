package com.example.project_skripsi.module.student.main.home.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.FragmentStHomeBinding
import com.example.project_skripsi.module.student.main.home.view.adapter.ItemListener
import com.example.project_skripsi.module.student.main.home.view.adapter.StHomeRecyclerViewMainAdapter
import com.example.project_skripsi.module.student.main.home.viewmodel.StHomeViewModel
import com.example.project_skripsi.utils.service.notification.NotificationUtil
import com.example.project_skripsi.module.student.main.studyclass.StClassFragmentDirections


class StHomeFragment : Fragment(), ItemListener {

    private lateinit var viewModel: StHomeViewModel
    private var _binding: FragmentStHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[StHomeViewModel::class.java]
        _binding = FragmentStHomeBinding.inflate(inflater, container, false)

        viewModel.currentStudent.observe(viewLifecycleOwner) {
            binding.tvProfileName.text = ("${it.name} (${it.attendanceNumber})")
            it.profile?.let { imageUrl ->
                Glide
                    .with(requireContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.profile_empty)
                    .into(binding.ivProfilePicture)
            }
        }

        viewModel.profileClass.observe(viewLifecycleOwner, { binding.tvProfileClass.text = it })

        with(binding.recyclerviewClass) {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            viewModel.sectionData.observe(viewLifecycleOwner, { adapter = StHomeRecyclerViewMainAdapter(it, this@StHomeFragment) })
        }


        binding.imvAnnouncement.setOnClickListener {
            view?.findNavController()?.navigate(
                StHomeFragmentDirections
                .actionNavigationHomeFragmentToStAnnouncementFragment())
        }

        binding.imvSettings.setOnClickListener {
            view?.findNavController()?.navigate(
                StHomeFragmentDirections.actionNavigationHomeFragmentToStProfileFragment()
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        triggerNotification()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun triggerNotification() {
        // Class Meeting
        viewModel.attendedMeeting.observe(viewLifecycleOwner) {
            it.map { attendedMeeting ->
                attendedMeeting.startTime?.let { it ->
                    NotificationUtil.cancelNotificationMeeting(requireActivity(), it)
                    NotificationUtil.scheduleSingleNotification(
                        requireActivity(),
                        it,
                        "Hai, jangan lupa",
                        "Pertemuan kelas ${attendedMeeting.subjectName}"
                    )
                }
            }
        }
        // Exam
        viewModel.listHomeSectionDataExam.observe(viewLifecycleOwner) {
            it.map { taskForm ->
                Log.d("987", "triggerNotification Exam start Time: $taskForm")
                // Start time
                taskForm.startTime?.let {
                    NotificationUtil.cancelNotificationMeeting(requireActivity(), it)
                    NotificationUtil.scheduleSingleNotification(
                        requireActivity(),
                        it,
                        "Hai, 10 menit lagi, ujian kamu",
                        "${taskForm.subjectName} dimulai"
                    )
                }
                // End time
                taskForm.endTime?.let {
                    Log.d("987", "triggerNotification Exam end Time: $taskForm")
                    NotificationUtil.cancelNotificationMeeting(requireActivity(), it)
                    NotificationUtil.scheduleSingleNotification(
                        requireActivity(),
                        it,
                        "Hai, kurang 10 menit lagi, ujian kamu",
                        "${taskForm.subjectName} selesai"
                    )
                }
            }
        }
        // Task
        viewModel.listHomeSectionDataAssignment.observe(viewLifecycleOwner) {
            it.map { taskForm ->
                // Start time
                taskForm.startTime?.let {
                    NotificationUtil.cancelNotificationMeeting(requireActivity(), it)
                    NotificationUtil.scheduleSingleNotification(
                        requireActivity(),
                        it,
                        "Hai, 10 menit lagi, tugas kamu",
                        "${taskForm.subjectName} dimulai"
                    )
                }
                // End time
                taskForm.endTime?.let {
                    NotificationUtil.cancelNotificationMeeting(requireActivity(), it)
                    NotificationUtil.scheduleSingleNotification(
                        requireActivity(),
                        it,
                        "Hai, kurang 10 menit lagi, tugas kamu",
                        "${taskForm.subjectName} selesai"
                    )
                }
            }
        }
    }

    override fun onTaskFormItemClicked(taskFormId: String, subjectName: String) {
            StHomeFragmentDirections.actionNavigationHomeFragmentToStTaskFormFragment(taskFormId)
    }

    override fun onClassItemClicked(Position: Int) {
       
    }

    override fun onMaterialItemClicked(Position: Int) {

    }
}
