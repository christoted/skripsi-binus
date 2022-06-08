package com.example.project_skripsi.module.student.main.progress.graphic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.project_skripsi.databinding.FragmentStProgressGraphicBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class StProgressGraphicFragment : Fragment() {

    private lateinit var viewModel: StProgressGraphicViewModel
    private var _binding: FragmentStProgressGraphicBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[StProgressGraphicViewModel::class.java]
        _binding = FragmentStProgressGraphicBinding.inflate(inflater, container, false)

        setupBarChart(binding.barchartExam)

        binding.imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBarChart(chart: BarChart) {

        // Set Data
        val entries: MutableList<BarEntry> = ArrayList()
        entries.add(BarEntry(0f, 30f))
        entries.add(BarEntry(1f, 80f))
        entries.add(BarEntry(2f, 60f))
        entries.add(BarEntry(3f, 50f))
        // gap of 2f
        // gap of 2f
        entries.add(BarEntry(4f, 90f))
        entries.add(BarEntry(5f, 70f))
        entries.add(BarEntry(6f, 60f))

        val set = BarDataSet(entries, "BarDataSet")
        val data = BarData(set)
        data.barWidth = 0.9f // set custom bar width

        chart.description.isEnabled = false
        chart.setMaxVisibleValueCount(60)

        chart.setScaleEnabled(false)
        chart.axisLeft.axisMinimum = 0f
        chart.axisLeft.axisMaximum = 100f

        chart.setDrawBarShadow(false)
        chart.setDrawGridBackground(false)

        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)

        chart.axisLeft.setDrawGridLines(false)

        // add a nice and smooth animation
        chart.animateY(1000)
        chart.legend.isEnabled = false
        chart.data = data
        chart.setFitBars(true) // make the x-axis fit exactly all bars
        chart.invalidate() // refresh
    }
}