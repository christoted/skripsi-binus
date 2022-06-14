package com.example.project_skripsi.module.teacher.main.calendar

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.core.model.local.TeacherAgendaMeeting
import com.example.project_skripsi.databinding.FragmentTcCalendarBinding
import com.example.project_skripsi.module.teacher._sharing.agenda.TcAgendaItemListener
import com.example.project_skripsi.utils.decorator.EventDecorator
import com.example.project_skripsi.utils.helper.DateHelper
import com.example.project_skripsi.utils.helper.UIHelper
import com.google.android.material.appbar.AppBarLayout
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import java.util.*
import kotlin.math.abs

class TcCalendarFragment : Fragment(), OnDateSelectedListener, TcAgendaItemListener {

    private lateinit var viewModel: TcCalendarViewModel
    private var _binding: FragmentTcCalendarBinding? = null
    private val binding get() = _binding!!

    private var curEmptyView : View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[TcCalendarViewModel::class.java]
        _binding = FragmentTcCalendarBinding.inflate(inflater, container, false)

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

        viewModel.eventList.observe(viewLifecycleOwner, { eventList ->
            eventList.map { dayEvent ->
                binding.calendar.addDecorator(EventDecorator(dayEvent.key, dayEvent.value))
            }
            refreshList(viewModel.currentSelectedDate)
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            val emptyView = UIHelper.getEmptyItem("Tidak ada kegiatan", layoutInflater, binding.llRvParent)
            binding.llRvParent.addView(emptyView)
            curEmptyView = emptyView
        }
        binding.rvEvent.adapter = TcCalendarAdapter(viewModel.currentDataList[date] ?: emptyList(), this)
    }

    override fun onTaskFormItemClicked(
        taskFormId: String,
        studyClassId: String,
        subjectName: String
    ) {
        view?.findNavController()?.navigate(
            TcCalendarFragmentDirections
                .actionTcCalendarFragmentToTcStudyClassTaskDetailFragment(studyClassId, subjectName, taskFormId)
        )
    }

    override fun onClassItemClicked(Position: Int, data: TeacherAgendaMeeting) {
        val meetingLink = data.classMeeting.meetingLink
        goToClassMeeting("https://sea.zoom.us/j/3242673339?pwd=SGlVRWswNmRiRU10d0kzNHBjQmVIQT09")
    }

    override fun onMaterialItemClicked(Position: Int) {
//        TODO("Not yet implemented")
    }

    private fun goToClassMeeting(classLink: String) {
        val uri = Uri.parse(classLink)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}