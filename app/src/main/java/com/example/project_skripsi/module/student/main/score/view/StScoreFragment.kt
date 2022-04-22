package com.example.project_skripsi.module.student.main.score.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.project_skripsi.databinding.FragmentStProgressBinding
import com.example.project_skripsi.module.student.main.score.view.adapter.StScoreViewPagerAdapter
import com.example.project_skripsi.module.student.main.score.viewmodel.StScoreViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.google.android.material.tabs.TabLayoutMediator
import com.github.mikephil.charting.data.BarDataSet

import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BarData

class StScoreFragment : Fragment()  {

    private lateinit var viewModel: StScoreViewModel
    private var _binding: FragmentStProgressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[StScoreViewModel::class.java]
        _binding = FragmentStProgressBinding.inflate(inflater, container, false)

        binding.vpContainer.adapter = StScoreViewPagerAdapter(activity!!, viewModel)
        TabLayoutMediator(binding.tabLayout, binding.vpContainer) { tab, position ->
            tab.text = StScoreViewModel.tabHeader[position]
        }.attach()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            setupBarChart(chart = chart1)
        }
        setScoreTopData()
    }

    private fun setScoreTopData() {
        viewModel.scoreFragmentData.observe(viewLifecycleOwner, {
            with(binding) {
                tvScore.text = it.totalScore.toString()
                tvAbsent.text = it.totalAbsent.toString()
            }
        })
        viewModel.achievements.observe(viewLifecycleOwner, {
            with(binding) {
                tvAchievement.text = it.count().toString()
            }
        })
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
        data.barWidth = 0.9f // set custom bar width
        chart.data = data
        chart.setFitBars(true) // make the x-axis fit exactly all bars
        chart.invalidate() // refresh

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}