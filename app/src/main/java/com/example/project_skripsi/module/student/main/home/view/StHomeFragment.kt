package com.example.project_skripsi.module.student.main.home.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.ClassMeeting
import com.example.project_skripsi.core.model.local.NotificationModel
import com.example.project_skripsi.databinding.FragmentStHomeBinding
import com.example.project_skripsi.module.common.zoom.MeetingHandler
import com.example.project_skripsi.module.student.main.home.view.adapter.ItemListener
import com.example.project_skripsi.module.student.main.home.view.adapter.StHomeRecyclerViewMainAdapter
import com.example.project_skripsi.module.student.main.home.viewmodel.StHomeViewModel
import com.example.project_skripsi.utils.helper.DateHelper.Companion.getDateWithMinuteOffset
import com.example.project_skripsi.utils.helper.DateHelper.Companion.getDateWithSecondOffset
import com.example.project_skripsi.utils.service.alarm.AlarmService
import com.example.project_skripsi.utils.service.notification.NotificationUtil
import com.example.project_skripsi.utils.service.zoom.ZoomService


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

            viewModel.sectionData.observe(viewLifecycleOwner, {
                adapter = StHomeRecyclerViewMainAdapter(it, this@StHomeFragment)
            })
        }

        viewModel.incompleteResource.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(
                    requireContext(),
                    "Materi \"${it.title}\" (${it.subjectName}) harus dibaca terlebih dahulu",
                    Toast.LENGTH_LONG
                ).show()
            }
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

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = true
            viewModel.refreshData()
        }

        viewModel.isFetchDataCompleted.observe(viewLifecycleOwner) {
            if (it) binding.swipeRefreshLayout.isRefreshing = false
        }

        triggerNotification()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun triggerNotification() {
        classMeetingNotification()
        examNotification()
        assignmentNotification()
        everyDayNotification()
    }

    private fun everyDayNotification() {
        var totalClassMeeting = 0
        var totalAssignment = 0
        var totalExam = 0
        var count = 0

        viewModel.listHomeSectionDataExam.observe(viewLifecycleOwner) { listExamTaskForms ->
            totalExam = listExamTaskForms.size
            if (++count == 3) triggerEveryDayNotification(totalClassMeeting, totalAssignment, totalExam)
        }

        viewModel.listHomeSectionDataAssignment.observe(viewLifecycleOwner) { listAssignmentTaskForms ->
            totalAssignment = listAssignmentTaskForms.size
            if (++count == 3) triggerEveryDayNotification(totalClassMeeting, totalAssignment, totalExam)
        }

        viewModel.listHomeSectionDataClassSchedule.observe(viewLifecycleOwner) { listClassSchedule ->
            totalClassMeeting = listClassSchedule.size
            if (++count == 3) triggerEveryDayNotification(totalClassMeeting, totalAssignment, totalExam)
        }
    }

    private fun triggerEveryDayNotification(totalMeeting: Int, totalAssignment: Int, totalExam: Int) {
        NotificationUtil.cancelEveryDayNotification(requireActivity())
        if (totalAssignment == 0 && totalMeeting == 0 && totalExam == 0){
            NotificationUtil.scheduleEveryDayNotification(requireActivity(), title = "Hai", body = "Tidak ada agenda hari ini")
        } else {
            NotificationUtil.scheduleEveryDayNotification(
                requireActivity(),
                title = "Siap untuk belajar hari ini",
                body = "Kamu punya $totalMeeting Pertemuan, " +
                        "$totalAssignment tugas , $totalExam ujian"
            )
        }
    }

    private fun classMeetingNotification() {
        // Class Meeting
        viewModel.listHomeSectionDataClassScheduleOneWeek.observe(viewLifecycleOwner) {
            it.map { attendedMeeting ->
                attendedMeeting.startTime?.let { dt ->
                    NotificationUtil.cancelNotification(requireActivity(), dt, attendedMeeting.id!!)
                    NotificationUtil.scheduleSingleNotification(
                        requireActivity(),
                        dt,
                        "Hai, jangan lupa",
                        "Pertemuan kelas ${attendedMeeting.subjectName}",
                        attendedMeeting.id
                    )

                    AlarmService.inst.createAlarm(
                        requireContext(),
                        "Kelas ${attendedMeeting.subjectName} sudah mulai 2 menit",
                        dt.getDateWithMinuteOffset(2).getDateWithSecondOffset(-5),
                        attendedMeeting.id
                    )
                }
            }
        }
    }

    private fun examNotification() {
        // Exam
        viewModel.listHomeSectionDataExamOneWeek.observe(viewLifecycleOwner) {
            it.map { taskForm ->
                Log.d("987", "triggerNotification Exam start Time: $taskForm")
                // Start time
                taskForm.startTime?.let { dt ->
                    NotificationUtil.cancelNotification(requireActivity(), dt, taskForm.id!! + "start")
                    NotificationUtil.scheduleSingleNotification(
                        requireActivity(),
                        dt,
                        "Hai, 10 menit lagi, ujian kamu",
                        "${taskForm.subjectName} dimulai",
                        taskForm.id + "start"
                    )

                    AlarmService.inst.createAlarm(
                        requireContext(),
                        "Ujian ${taskForm.subjectName} sudah mulai 2 menit",
                        dt.getDateWithMinuteOffset(2).getDateWithSecondOffset(-5),
                        taskForm.id
                    )
                }
                // End time
                taskForm.endTime?.let { dt ->
                    Log.d("987", "triggerNotification Exam end Time: $taskForm")
                    NotificationUtil.cancelNotification(requireActivity(), dt, taskForm.id!! + "end")
                    NotificationUtil.scheduleSingleNotification(
                        requireActivity(),
                        dt,
                        "Hai, kurang 10 menit lagi, ujian kamu",
                        "${taskForm.subjectName} selesai",
                        taskForm.id + "end"
                    )
                }
            }
        }
    }

    private fun assignmentNotification() {
        // Task
        viewModel.listHomeSectionDataAssignmentOneWeek.observe(viewLifecycleOwner) {
            it.map { taskForm ->
                // Start time
                taskForm.startTime?.let { dt ->
                    NotificationUtil.cancelNotification(requireActivity(), dt, taskForm.id!! + "start")
                    NotificationUtil.scheduleSingleNotification(
                        requireActivity(),
                        dt,
                        "Hai, 10 menit lagi, tugas kamu",
                        "${taskForm.subjectName} dimulai",
                        taskForm.id + "start"
                    )

                    AlarmService.inst.createAlarm(
                        requireContext(),
                        "Tugas ${taskForm.subjectName} sudah mulai 2 menit",
                        dt.getDateWithMinuteOffset(2).getDateWithSecondOffset(-5),
                        taskForm.id
                    )
                }
                // End time
                taskForm.endTime?.let { dt ->
                    NotificationUtil.cancelNotification(requireActivity(), dt, taskForm.id!! + "end")
                    NotificationUtil.scheduleSingleNotification(
                        requireActivity(),
                        dt,
                        "Hai, kurang 10 menit lagi, tugas kamu",
                        "${taskForm.subjectName} selesai",
                        taskForm.id + "end"
                    )
                }
            }
        }
    }

    override fun onTaskFormItemClicked(taskFormId: String, subjectName: String) {
        view?.findNavController()?.navigate(
            StHomeFragmentDirections.actionNavigationHomeFragmentToStTaskFormFragment(taskFormId)
        )
    }

    override fun onClassItemClicked(classMeeting: ClassMeeting) {
        MeetingHandler.inst.startMeetingAsStudent(viewModel.currentStudent.value, classMeeting.id)
        ZoomService.inst.joinMeeting(requireContext(), "Siswa - ${viewModel.currentStudent.value?.name}")
    }

    override fun onResourceItemClicked(resourceId: String) {
        viewModel.openResource(requireContext(), resourceId)
    }
}
