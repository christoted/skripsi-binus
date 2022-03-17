package com.example.project_skripsi.module.student.main.calendar

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.project_skripsi.databinding.FragmentStCalendarBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import android.R
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import java.text.ParseException
import java.util.*


class StCalendarFragment : Fragment(), OnDateSelectedListener {



    private lateinit var viewModel: StCalendarViewModel
    private var _binding: FragmentStCalendarBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this)[StCalendarViewModel::class.java]
        _binding = FragmentStCalendarBinding.inflate(inflater, container, false)

        binding.calendar.setOnDateChangedListener(this)
        binding.rvEvent.layoutManager = LinearLayoutManager(context)

        viewModel.eventList.observe(viewLifecycleOwner, { eventList ->
            eventList.map { dayEvent ->
                binding.calendar.addDecorator(EventDecorator(dayEvent.key, dayEvent.value))
            }
        })


        return binding.root
    }

    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {
        binding.rvEvent.adapter = StCalendarAdapter(viewModel.eventList.value?.get(date) ?: listOf())
//        Toast.makeText(context, date.day.toString() + " " + date.month + " " + date.year, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

