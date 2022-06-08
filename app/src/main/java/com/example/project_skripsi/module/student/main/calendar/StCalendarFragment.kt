package com.example.project_skripsi.module.student.main.calendar

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.project_skripsi.databinding.FragmentStCalendarBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.ViewEmptyItemBinding
import com.example.project_skripsi.databinding.ViewEmptyListBinding
import com.example.project_skripsi.module.student.main.home.view.adapter.ItemListener
import com.example.project_skripsi.utils.decorator.EventDecorator
import com.example.project_skripsi.utils.helper.DateHelper
import com.google.android.material.appbar.AppBarLayout
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
                binding.collapseLayout.title =
                    DateHelper.getFormattedDateTimeWithWeekDay(viewModel.currentSelectedDate.date)
            } else {
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
        return binding.root
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
            emptyView.tvEmpty.text = "Tidak ada kegiatan"
            binding.llRvParent.addView(emptyView.root)
            curEmptyView = emptyView.root
        }
        binding.rvEvent.adapter = StCalendarAdapter(viewModel.currentDataList[date] ?: emptyList(), this)
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

    override fun onClassItemClicked(Position: Int) {
        TODO("Not yet implemented")
    }

    override fun onMaterialItemClicked(Position: Int) {
        TODO("Not yet implemented")
    }

}

