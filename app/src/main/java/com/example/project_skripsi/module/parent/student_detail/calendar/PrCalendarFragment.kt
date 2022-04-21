package com.example.project_skripsi.module.parent.student_detail.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentPrCalendarBinding
import com.example.project_skripsi.databinding.FragmentPrHomeBinding
import com.example.project_skripsi.module.student.main.calendar.StCalendarAdapter
import com.example.project_skripsi.utils.decorator.EventDecorator
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener

class PrCalendarFragment : Fragment(), OnDateSelectedListener {

    private lateinit var viewModel: PrCalendarViewModel
    private var _binding: FragmentPrCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[PrCalendarViewModel::class.java]
        _binding = FragmentPrCalendarBinding.inflate(inflater, container, false)

        binding.calendar.setOnDateChangedListener(this)
        binding.rvEvent.layoutManager = LinearLayoutManager(context)

        viewModel.eventList.observe(viewLifecycleOwner) { eventList ->
            eventList.map { dayEvent ->
                binding.calendar.addDecorator(EventDecorator(dayEvent.key, dayEvent.value))
            }
        }

        retrieveArgs()

        return binding.root
    }

    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {
        binding.rvEvent.adapter = PrCalendarAdapter(viewModel.currentDataList[date] ?: emptyList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs() {
        val args : PrCalendarFragmentArgs by navArgs()
        viewModel.setStudent(args.studentId);
    }
}