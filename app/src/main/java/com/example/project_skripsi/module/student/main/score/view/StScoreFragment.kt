package com.example.project_skripsi.module.student.main.score.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.Observer
import com.example.project_skripsi.databinding.FragmentStScoreBinding
import com.example.project_skripsi.module.student.main.score.view.adapter.StScoreViewPagerAdapter
import com.example.project_skripsi.module.student.main.score.viewmodel.StScoreViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.google.android.material.tabs.TabLayoutMediator
import com.github.mikephil.charting.data.BarDataSet

import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BarData

class StScoreFragment : Fragment(), SeekBar.OnSeekBarChangeListener {

    private lateinit var viewModel: StScoreViewModel
    private var _binding: FragmentStScoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(StScoreViewModel::class.java)
        _binding = FragmentStScoreBinding.inflate(inflater, container, false)

        viewModel.text.observe(viewLifecycleOwner, Observer {

        })

        binding.vpContainer.adapter = StScoreViewPagerAdapter(activity!!, viewModel)
        TabLayoutMediator(binding.tabLayout, binding.vpContainer) {
            tab, position ->
            tab.text = StScoreViewModel.tabHeader[position]
        }.attach()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            setupBarChart(chart = chart1)
        }
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


        chart.description.isEnabled = false
        chart.setMaxVisibleValueCount(60)
        // scaling can now only be done on x- and y-axis separately

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false)

        chart.setDrawBarShadow(false)
        chart.setDrawGridBackground(false)

        val xAxis = chart.xAxis
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)

        chart.axisLeft.setDrawGridLines(false)

        // add a nice and smooth animation

        // add a nice and smooth animation
        chart.animateY(1500)
        chart.legend.isEnabled = false
        data.setBarWidth(0.9f) // set custom bar width
        chart.setData(data)
        chart.setFitBars(true) // make the x-axis fit exactly all bars
        chart.invalidate() // refresh

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

    }

    override fun onStartTrackingTouch(p0: SeekBar?) {

    }

    override fun onStopTrackingTouch(p0: SeekBar?) {

    }
}