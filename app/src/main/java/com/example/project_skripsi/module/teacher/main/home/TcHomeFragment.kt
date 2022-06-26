package com.example.project_skripsi.module.teacher.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.local.TeacherAgendaMeeting
import com.example.project_skripsi.databinding.FragmentTcHomeBinding
import com.example.project_skripsi.module.common.zoom.MeetingHandler
import com.example.project_skripsi.module.teacher._sharing.agenda.TcAgendaItemListener
import com.example.project_skripsi.module.teacher.main.home.adapter.TcHomeMainAdapter
import com.example.project_skripsi.module.teacher.main.home.viewmodel.TcHomeViewModel
import com.example.project_skripsi.utils.service.notification.NotificationUtil
import com.example.project_skripsi.utils.service.zoom.ZoomService

class TcHomeFragment : Fragment(), TcAgendaItemListener {

    private var _binding: FragmentTcHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TcHomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[TcHomeViewModel::class.java]
        _binding = FragmentTcHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.teacherData.observe(viewLifecycleOwner) {
            with(binding) {
                tvProfileName.text = it.name
                Glide
                    .with(root)
                    .load(it.profile)
                    .placeholder(R.drawable.profile_empty)
                    .into(ivProfilePicture)
            }
        }

        viewModel.studyClass.observe(viewLifecycleOwner) {
            with(binding) {
                tvProfileClass.text = it.name
            }
        }

        // Main Section
        with(binding.mainRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            viewModel.sectionData.observe(viewLifecycleOwner) {
                val mainSectionAdapter = TcHomeMainAdapter(it, this@TcHomeFragment)
                adapter = mainSectionAdapter
            }
        }

        binding.imvAnnouncement.setOnClickListener {
            view.findNavController().navigate(
                TcHomeFragmentDirections.actionTcHomeFragmentToTcAnnouncementFragment()
            )
        }

        binding.imvSettings.setOnClickListener {
            view.findNavController().navigate(
                TcHomeFragmentDirections.actionTcHomeFragmentToTcProfileFragment()
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
    }

    private fun triggerNotification() {
        classMeetingNotification()
        examAssignmentNotification()
        everyDayNotification()
    }

    private fun everyDayNotification() {
        NotificationUtil.scheduleDailyNotification(requireContext(), false)
    }

    private fun classMeetingNotification() {
        viewModel.listMeeting.observe(viewLifecycleOwner) {
            it.map { classMeeting ->
                classMeeting.classMeeting.startTime?.let { date ->
                    NotificationUtil.cancelNotification(requireActivity(),  classMeeting.classMeeting.id!!)
                    NotificationUtil.scheduleSingleNotification(requireActivity()
                    ,date, "Hai bapak/ibu guru", "10 menit lagi, pertemuan ${classMeeting.studyClassName} dimulai, semangat!", classMeeting.classMeeting.id )
                }
            }
        }
    }

    private fun examAssignmentNotification() {
        // Exam
        viewModel.examList.observe(viewLifecycleOwner) {
            it.map { teacherAgendaTaskForm ->
                val taskForm = teacherAgendaTaskForm.taskForm
                taskForm.startTime?.let { dt ->
                    NotificationUtil.cancelNotification(requireActivity(), teacherAgendaTaskForm.taskForm.id!! + "start")
                    NotificationUtil.scheduleSingleNotification(
                        requireActivity(),
                        dt,
                        "Hai bapak/ibu guru, 10 menit lagi",
                        "ujian ${taskForm.subjectName} dimulai",
                        teacherAgendaTaskForm.taskForm.id + "start"
                    )
                }
                taskForm.endTime?.let { dt ->
                    NotificationUtil.cancelNotification(requireActivity(),  teacherAgendaTaskForm.taskForm.id!! + "end")
                    NotificationUtil.scheduleSingleNotification(
                        requireActivity(),
                        dt,
                        "Hai bapak/ibu guru, 10 menit lagi",
                        "ujian ${taskForm.subjectName} selesai",
                        teacherAgendaTaskForm.taskForm.id + "end"
                    )
                }
            }
        }
        // Assignment
        viewModel.assignmentList.observe(viewLifecycleOwner) {
            it.map { teacherAgendaTaskForm ->
                val taskForm = teacherAgendaTaskForm.taskForm
                taskForm.startTime?.let { dt ->
                    NotificationUtil.cancelNotification(requireActivity(), teacherAgendaTaskForm.taskForm.id!! + "start")
                    NotificationUtil.scheduleSingleNotification(
                        requireActivity(),
                        dt,
                        "Hai bapak/ibu guru, 10 menit lagi",
                        "tugas ${taskForm.subjectName} dimulai",
                        teacherAgendaTaskForm.taskForm.id + "start"
                    )
                }
                taskForm.endTime?.let { dt ->
                    NotificationUtil.cancelNotification(requireActivity(), teacherAgendaTaskForm.taskForm.id!! + "end")
                    NotificationUtil.scheduleSingleNotification(
                        requireActivity(),
                        dt,
                        "Hai bapak/ibu guru, 10 menit lagi",
                        "tugas ${taskForm.subjectName} selesai",
                        teacherAgendaTaskForm.taskForm.id + "end"
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTaskFormItemClicked(
        taskFormId: String,
        studyClassId: String,
        subjectName: String
    ) {
        val toTaskActivity =
            TcHomeFragmentDirections.actionTcHomeFragmentToTcStudyClassTaskDetailFragment(
                studyClassId,
                subjectName,
                taskFormId
            )
        view?.findNavController()?.navigate(toTaskActivity)
    }

    override fun onClassItemClicked(agendaMeeting: TeacherAgendaMeeting) {
        MeetingHandler.inst.startMeetingAsTeacher()
        ZoomService.inst.joinMeeting(
            requireContext(),
            "Guru - ${viewModel.teacherData.value?.name}"
        )
    }

    override fun onResourceItemClicked(resourceId: String) {
        viewModel.openLink(requireContext(), resourceId)
    }
}