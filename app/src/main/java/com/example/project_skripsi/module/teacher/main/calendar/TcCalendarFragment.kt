package com.example.project_skripsi.module.teacher.main.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentStCalendarBinding
import com.example.project_skripsi.databinding.FragmentTcCalendarBinding
import com.example.project_skripsi.module.student.main.home.view.adapter.ItemListener
import com.example.project_skripsi.utils.decorator.EventDecorator
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener

class TcCalendarFragment : Fragment(), OnDateSelectedListener, ItemListener {

    private var _binding: FragmentStCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStCalendarBinding.inflate(inflater, container, false)

        binding.calendar.setOnDateChangedListener(this)
        binding.rvEvent.layoutManager = LinearLayoutManager(context)

//        viewModel.eventList.observe(viewLifecycleOwner, { eventList ->
//            eventList.map { dayEvent ->
//                binding.calendar.addDecorator(EventDecorator(dayEvent.key, dayEvent.value))
//            }
//        })

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
//        TODO("Not yet implemented")
    }

    override fun onTaskFormItemClicked(taskFormId: String, subjectName: String) {
//        TODO("Not yet implemented")
    }

    override fun onClassItemClicked(Position: Int) {
//        TODO("Not yet implemented")
    }

    override fun onMaterialItemClicked(Position: Int) {
//        TODO("Not yet implemented")
    }
}