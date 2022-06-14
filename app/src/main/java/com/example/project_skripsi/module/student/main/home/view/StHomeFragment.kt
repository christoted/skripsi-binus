package com.example.project_skripsi.module.student.main.home.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.example.project_skripsi.core.model.firestore.ClassMeeting
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.model.local.NotificationModel
import com.example.project_skripsi.databinding.FragmentStHomeBinding
import com.example.project_skripsi.module.student.main.home.view.adapter.ItemListener
import com.example.project_skripsi.module.student.main.home.view.adapter.StHomeRecyclerViewMainAdapter
import com.example.project_skripsi.module.student.main.home.viewmodel.StHomeViewModel
import com.example.project_skripsi.utils.Constant
import com.example.project_skripsi.utils.helper.DateHelper
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
        classMeetingNotification()
        examNotification()
        assignmentNotification()
        everyDayNotification()
    }

    private fun everyDayNotification() {
        val listEveryDayNotificationObservable = MutableLiveData<List<NotificationModel>>()
        val listEveryDayNotification = mutableListOf<NotificationModel>()
        var totalClassMeeting = 0
        var totalAssignment = 0
        var totalExam = 0
        var count = 0

        viewModel.listHomeSectionDataExam.observe(viewLifecycleOwner) { listExamTaskForms ->
            listExamTaskForms.map {
                val date = it.startTime
                date?.let {
                    val notificationModel = NotificationModel(body = "Exam", date = it)
                    totalExam+=1
                    listEveryDayNotification.add(notificationModel)
                }
            }
            count++
            listEveryDayNotificationObservable.postValue(listEveryDayNotification)
        }

        viewModel.listHomeSectionDataAssignment.observe(viewLifecycleOwner) { listAssignmentTaskForms ->
            listAssignmentTaskForms.map {
                val date = it.startTime
                date?.let {
                    val notificationModel = NotificationModel(body = "Assignment", date = it)
                    totalAssignment+=1
                    listEveryDayNotification.add(notificationModel)
                }
            }
            count++
            listEveryDayNotificationObservable.postValue(listEveryDayNotification)
        }

        viewModel.listHomeSectionDataClassSchedule.observe(viewLifecycleOwner) { listClassSchedule ->
            listClassSchedule.map {
                val date = it.startTime
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

    private fun classMeetingNotification() {
        // Class Meeting
        viewModel.attendedMeeting.observe(viewLifecycleOwner) {
            it.map { attendedMeeting ->
                attendedMeeting.startTime?.let { it ->
                    NotificationUtil.cancelNotification(requireActivity(), it)
                    NotificationUtil.scheduleSingleNotification(
                        requireActivity(),
                        it,
                        "Hai, jangan lupa",
                        "Pertemuan kelas ${attendedMeeting.subjectName}"
                    )
                }
            }
        }
    }
    private fun examNotification() {
        // Exam
        viewModel.listHomeSectionDataExam.observe(viewLifecycleOwner) {
            it.map { taskForm ->
                Log.d("987", "triggerNotification Exam start Time: $taskForm")
                // Start time
                taskForm.startTime?.let {
                    NotificationUtil.cancelNotification(requireActivity(), it)
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
                    NotificationUtil.cancelNotification(requireActivity(), it)
                    NotificationUtil.scheduleSingleNotification(
                        requireActivity(),
                        it,
                        "Hai, kurang 10 menit lagi, ujian kamu",
                        "${taskForm.subjectName} selesai"
                    )
                }
            }
        }
    }
    private fun assignmentNotification() {
        // Task
        viewModel.listHomeSectionDataAssignment.observe(viewLifecycleOwner) {
            it.map { taskForm ->
                // Start time
                taskForm.startTime?.let {
                    NotificationUtil.cancelNotification(requireActivity(), it)
                    NotificationUtil.scheduleSingleNotification(
                        requireActivity(),
                        it,
                        "Hai, 10 menit lagi, tugas kamu",
                        "${taskForm.subjectName} dimulai"
                    )
                }
                // End time
                taskForm.endTime?.let {
                    NotificationUtil.cancelNotification(requireActivity(), it)
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

    override fun onClassItemClicked(Position: Int, classMeeting: ClassMeeting) {
       goToClassMeeting("https://sea.zoom.us/j/3242673339?pwd=SGlVRWswNmRiRU10d0kzNHBjQmVIQT09")
    }

    override fun onMaterialItemClicked(Position: Int) {
        goToGoogleDrive("https://drive.google.com/drive/folders/1DIFexFEdlRVILpxZt8Qcdgr842Eo0FcY?usp=sharing")
    }
    private fun goToGoogleDrive(driveLink: String) {
        val uri = Uri.parse(driveLink)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
    private fun goToClassMeeting(classLink: String) {
        val uri = Uri.parse(classLink)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}
