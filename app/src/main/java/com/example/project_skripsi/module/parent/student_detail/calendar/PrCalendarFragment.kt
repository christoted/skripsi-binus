package com.example.project_skripsi.module.parent.student_detail.calendar

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.DialogIndicatorInfoBinding
import com.example.project_skripsi.databinding.FragmentPrCalendarBinding
import com.example.project_skripsi.utils.decorator.EventDecorator
import com.example.project_skripsi.utils.helper.DateHelper
import com.example.project_skripsi.utils.helper.UIHelper
import com.google.android.material.appbar.AppBarLayout
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlin.math.abs

class PrCalendarFragment : Fragment(), OnDateSelectedListener {

    private lateinit var viewModel: PrCalendarViewModel
    private var _binding: FragmentPrCalendarBinding? = null
    private val binding get() = _binding!!

    private var curEmptyView : View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[PrCalendarViewModel::class.java]
        _binding = FragmentPrCalendarBinding.inflate(inflater, container, false)

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

        binding.btnInfo.setOnClickListener { showInfoDialog(binding.root.context) }

        binding.imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }

        retrieveArgs()

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
            val emptyView = UIHelper.getEmptyItem("Tidak ada kegiatan", layoutInflater, binding.llRvParent)
            binding.llRvParent.addView(emptyView)
            curEmptyView = emptyView
        }
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