package com.example.project_skripsi.module.teacher.main.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.local.NotificationModel
import com.example.project_skripsi.core.model.local.TeacherAgendaMeeting
import com.example.project_skripsi.core.model.local.TeacherAgendaTaskForm
import com.example.project_skripsi.databinding.FragmentTcHomeBinding
import com.example.project_skripsi.module.teacher._sharing.agenda.TcAgendaItemListener
import com.example.project_skripsi.module.teacher.main.home.adapter.TcHomeMainAdapter
import com.example.project_skripsi.module.teacher.main.home.viewmodel.TcHomeViewModel
import com.example.project_skripsi.utils.service.notification.NotificationUtil

class TcHomeFragment : Fragment(), TcAgendaItemListener {

    private var _binding: FragmentTcHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TcHomeViewModel

    var studyClassId = ""
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
                studyClassId = it.homeroomClass.toString()
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
        triggerNotification()
    }

    private fun triggerNotification() {
        classMeetingNotification()
        examAssignmentNotification()
        everyDayNotification()
    }

    private fun classMeetingNotification() {
        viewModel.listMeeting.observe(viewLifecycleOwner) {
            it.map { classMeeting ->
                classMeeting.classMeeting.startTime?.let { date ->
                    NotificationUtil.cancelNotification(requireActivity(), date)
                    NotificationUtil.scheduleSingleNotification(requireActivity()
                    ,date, "Hai bapak/ibu guru", "10 menit lagi, pertemuan ${classMeeting.studyClassName} dimulai, semangat!" )
                }
            }
        }
    }

    private fun examAssignmentNotification() {
        // Exam
        viewModel.examList.observe(viewLifecycleOwner) {
            it.map { teacherAgendaTaskForm ->
                val taskForm = teacherAgendaTaskForm.taskForm
                taskForm.startTime?.let {
                    NotificationUtil.cancelNotification(requireActivity(), it)
                    NotificationUtil.scheduleSingleNotification(
                        requireActivity(),
                        it,
                        "Hai bapak/ibu guru, 10 menit lagi",
                        "ujian ${taskForm.subjectName} dimulai"
                    )
                }
                taskForm.endTime?.let {
                    NotificationUtil.cancelNotification(requireActivity(), it)
                    NotificationUtil.scheduleSingleNotification(
                        requireActivity(),
                        it,
                        "Hai bapak/ibu guru, 10 menit lagi",
                        "ujian ${taskForm.subjectName} selesai"
                    )
                }
            }
        }
        // Assignment
        viewModel.assignmentList.observe(viewLifecycleOwner) {
            it.map { teacherAgendaTaskForm ->
                val taskForm = teacherAgendaTaskForm.taskForm
                taskForm.startTime?.let {
                    NotificationUtil.cancelNotification(requireActivity(), it)
                    NotificationUtil.scheduleSingleNotification(
                        requireActivity(),
                        it,
                        "Hai bapak/ibu guru, 10 menit lagi",
                        "tugas ${taskForm.subjectName} dimulai"
                    )
                }
                taskForm.endTime?.let {
                    NotificationUtil.cancelNotification(requireActivity(), it)
                    NotificationUtil.scheduleSingleNotification(
                        requireActivity(),
                        it,
                        "Hai bapak/ibu guru, 10 menit lagi",
                        "tugas ${taskForm.subjectName} selesai"
                    )
                }
            }
        }
    }

    private fun everyDayNotification() {
        val listEveryDayNotificationObservable = MutableLiveData<List<NotificationModel>>()
        val listEveryDayNotification = mutableListOf<NotificationModel>()
        var totalClassMeeting = 0
        var totalAssignment = 0
        var totalExam = 0
        var count = 0

        viewModel.examList.observe(viewLifecycleOwner) { listExamTaskForms ->
            listExamTaskForms.map {
                val date = it.taskForm.startTime
                date?.let {
                    val notificationModel = NotificationModel(body = "Exam", date = it)
                    totalExam+=1
                    listEveryDayNotification.add(notificationModel)
                }
            }
            count++
            listEveryDayNotificationObservable.postValue(listEveryDayNotification)
        }

        viewModel.assignmentList.observe(viewLifecycleOwner) { listAssignmentTaskForms ->
            listAssignmentTaskForms.map {
                val date = it.taskForm.startTime
                date?.let {
                    val notificationModel = NotificationModel(body = "Assignment", date = it)
                    totalAssignment+=1
                    listEveryDayNotification.add(notificationModel)
                }
            }
            count++
            listEveryDayNotificationObservable.postValue(listEveryDayNotification)
        }

        viewModel.listMeeting.observe(viewLifecycleOwner) { listClassSchedule ->
            listClassSchedule.map {
                val date = it.classMeeting.startTime
                date?.let {
                    val notificationModel = NotificationModel(body = "Meeting", date = it)
                    totalClassMeeting+=1
                    listEveryDayNotification.add(notificationModel)
                }
            }
            count++
            listEveryDayNotificationObservable.postValue(listEveryDayNotification)
        }

        listEveryDayNotificationObservable.observe(viewLifecycleOwner) {
            if (count == 3) {
                triggerEveryDayNotification(totalMeeting = totalClassMeeting, totalAssignment, totalExam)
            }
        }
    }

    private fun triggerEveryDayNotification(totalMeeting: Int, totalAssignment: Int, totalExam: Int) {
        NotificationUtil.cancelEveryDayNotification(requireActivity())
        if (totalAssignment == 0 && totalMeeting == 0 && totalExam == 0){
            NotificationUtil.scheduleEveryDayNotification(requireActivity(), title = "Hai", body = "Tidak ada agenda hari ini")
        } else {
            NotificationUtil.scheduleEveryDayNotification(requireActivity(), title = "Siap untuk belajar hari ini", body = "Kamu punya ${totalMeeting} Pertemuan, " +
                    "${totalAssignment} tugas , ${totalExam} ujian")
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
        val toTaskActivity = TcHomeFragmentDirections.actionTcHomeFragmentToTcStudyClassTaskDetailFragment(studyClassId, subjectName, taskFormId)
        view?.findNavController()?.navigate(toTaskActivity)
    }

    override fun onClassItemClicked(Position: Int, data: TeacherAgendaMeeting) {
//        val meetingLink = data.classMeeting.meetingLink
        goToClassMeeting("https://sea.zoom.us/j/3242673339?pwd=SGlVRWswNmRiRU10d0kzNHBjQmVIQT09")
    }

    override fun onResourceItemClicked(resourceId: String) {
        viewModel.openLink(requireContext(), resourceId)
    }

    private fun goToClassMeeting(classLink: String) {
        val uri = Uri.parse(classLink)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

}