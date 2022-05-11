package com.example.project_skripsi

import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class placeholderchart {

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
        xAxis.position = XAxis.XAxisPosition.BOTTOM
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

}