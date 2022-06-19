package com.example.project_skripsi.module.student.main.calendar

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.DialogIndicatorInfoBinding
import com.example.project_skripsi.core.model.firestore.ClassMeeting
import com.example.project_skripsi.databinding.FragmentStCalendarBinding
import com.example.project_skripsi.databinding.ViewEmptyItemBinding
import com.example.project_skripsi.module.common.zoom.MeetingHandler
import com.example.project_skripsi.module.student.main.home.view.adapter.ItemListener
import com.example.project_skripsi.utils.decorator.EventDecorator
import com.example.project_skripsi.utils.helper.DateHelper
import com.example.project_skripsi.utils.service.zoom.ZoomService
import com.google.android.material.appbar.AppBarLayout
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlin.math.abs


class StCalendarFragment : Fragment(), OnDateSelectedListener, ItemListener {

    private lateinit var viewModel: StCalendarViewModel
    private var _binding: FragmentStCalendarBinding? = null
    private val binding get() = _binding!!

    private var curEmptyView : View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[StCalendarViewModel::class.java]
        _binding = FragmentStCalendarBinding.inflate(inflater, container, false)

        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                binding.toolbar.visibility = View.VISIBLE
                binding.collapseLayout.title =
                    DateHelper.getFormattedDateTimeWithWeekDay(viewModel.currentSelectedDate.date)
            } else {
                binding.toolbar.visibility = View.GONE
                binding.collapseLayout.title = ""
            }
        })

        binding.calendar.selectedDate = viewModel.currentSelectedDate
        binding.calendar.setOnDateChangedListener(this)
        binding.rvEvent.layoutManager = LinearLayoutManager(context)

        viewModel.eventList.observe(viewLifecycleOwner) { eventList ->
            eventList.map { dayEvent ->
                binding.calendar.addDecorator(EventDecorator(dayEvent.key, dayEvent.value))
            }
            refreshList(viewModel.currentSelectedDate)
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

        binding.btnInfo.setOnClickListener { showInfoDialog(binding.root.context) }



        return binding.root
    }

    private fun showInfoDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.setContentView(DialogIndicatorInfoBinding.inflate(LayoutInflater.from(context)).root)
        dialog.show()
    }

    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {
        viewModel.currentSelectedDate = date
        refreshList(date)
    }

    private fun refreshList(date : CalendarDay){
        curEmptyView?.let { binding.llRvParent.removeView(it) }
        if (viewModel.currentDataList[date].isNullOrEmpty()) {
            val emptyView = ViewEmptyItemBinding.inflate(layoutInflater, binding.llRvParent, false)
            emptyView.tvEmpty.text = ("Tidak ada kegiatan")
            binding.llRvParent.addView(emptyView.root)
            curEmptyView = emptyView.root
        }
        binding.rvEvent.adapter = StCalendarAdapter(
            viewModel.currentDataList[date]
                ?: emptyList(),
            this
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTaskFormItemClicked(taskFormId: String, subjectName: String) {
        view?.findNavController()?.navigate(
            StCalendarFragmentDirections.actionNavigationCalendarFragmentToStTaskFormFragment(taskFormId)
        )
    }

    override fun onClassItemClicked(classMeeting: ClassMeeting) {
        MeetingHandler.inst.startMeetingAsStudent(viewModel.curStudent, classMeeting.id)
        ZoomService.inst.joinMeeting(requireContext(), viewModel.curStudent.name)
    }

    override fun onResourceItemClicked(resourceId: String) {
        viewModel.openResource(requireContext(), resourceId)
    }
}

