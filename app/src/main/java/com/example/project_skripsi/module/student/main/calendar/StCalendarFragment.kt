package com.example.project_skripsi.module.student.main.calendar

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
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
import com.example.project_skripsi.module.student.main.home.view.StHomeFragmentDirections
import com.example.project_skripsi.module.student.main.home.view.adapter.ItemListener
import com.example.project_skripsi.module.student.task.StTaskViewModel
import com.example.project_skripsi.utils.decorator.EventDecorator


class StCalendarFragment : Fragment(), OnDateSelectedListener, ItemListener {

    private lateinit var viewModel: StCalendarViewModel
    private var _binding: FragmentStCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[StCalendarViewModel::class.java]
        _binding = FragmentStCalendarBinding.inflate(inflater, container, false)

        binding.calendar.setOnDateChangedListener(this)
        binding.rvEvent.layoutManager = LinearLayoutManager(context)

        viewModel.eventList.observe(viewLifecycleOwner) { eventList ->
            eventList.map { dayEvent ->
                binding.calendar.addDecorator(EventDecorator(dayEvent.key, dayEvent.value))
            }
        }
        return binding.root
    }

    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {
        binding.rvEvent.adapter = StCalendarAdapter(viewModel.currentDataList[date] ?: emptyList(), this)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTaskFormItemClicked(taskFormId: String, subjectName: String) {
        val toTaskActivity = StCalendarFragmentDirections.actionNavigationCalendarFragmentToStTaskActivity(taskFormId)
        toTaskActivity.navigationType = StTaskViewModel.NAVIGATION_FORM
        view?.findNavController()?.navigate(toTaskActivity)
    }

    override fun onClassItemClicked(Position: Int) {
        TODO("Not yet implemented")
    }

    override fun onMaterialItemClicked(Position: Int) {
        TODO("Not yet implemented")
    }

}

