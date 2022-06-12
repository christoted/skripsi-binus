package com.example.project_skripsi.module.parent.student_detail.progress.graphic

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.AssignedTaskForm
import com.example.project_skripsi.databinding.FragmentPrProgressGraphicBinding
import com.example.project_skripsi.module.student.main.progress.graphic.StSubjectFilterViewHolder
import com.example.project_skripsi.utils.generic.ItemClickListener
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.components.YAxis


class PrProgressGraphicFragment : Fragment(), ItemClickListener {

    private lateinit var viewModel: PrProgressGraphicViewModel
    private var _binding: FragmentPrProgressGraphicBinding? = null
    private val binding get() = _binding!!

    private var dialog : BottomSheetDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[PrProgressGraphicViewModel::class.java]
        _binding = FragmentPrProgressGraphicBinding.inflate(inflater, container, false)

        reloadGraph("")

        viewModel.subjects.observe(viewLifecycleOwner, { list ->
            binding.btnFilter.setOnClickListener { showBottomSheet(list) }
            if (list.isNotEmpty()) reloadGraph(list[0])
        })

        binding.imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }

        retrieveArgs()

        return binding.root
    }

    private fun retrieveArgs() {
        val args: PrProgressGraphicFragmentArgs by navArgs()
        viewModel.setStudent(args.studentId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("InflateParams")
    private fun showBottomSheet(list: List<String>) {
        dialog = BottomSheetDialog(context!!)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_st_filter, null)

        val rvItem = view.findViewById<RecyclerView>(R.id.rv_item)
        rvItem.layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        rvItem.addItemDecoration(dividerItemDecoration)

        rvItem.adapter = StSubjectFilterViewHolder(list, this@PrProgressGraphicFragment).getAdapter()

        dialog?.let {
            it.setContentView(view)
            it.show()
        }
    }

    override fun onItemClick(itemId: String) {
        dialog?.hide()
        reloadGraph(itemId)
    }

    private fun reloadGraph(subjectName: String) {
        binding.tvExam.text = ("Ujian $subjectName")
        binding.tvAssignment.text = ("Tugas $subjectName")
        if (subjectName.isEmpty()) {
            setupBarChart(binding.barchartExam, binding.tvEmptyExam, emptyList())
            setupBarChart(binding.barchartAssignment, binding.tvEmptyAssignment, emptyList())
        } else {
            viewModel.exams.removeObservers(viewLifecycleOwner)
            viewModel.assignment.removeObservers(viewLifecycleOwner)

            viewModel.exams.observe(viewLifecycleOwner) {
                setupBarChart(binding.barchartExam, binding.tvEmptyExam,it[subjectName] ?: emptyList())
            }
            viewModel.assignment.observe(viewLifecycleOwner) {
                setupBarChart(binding.barchartAssignment, binding.tvEmptyAssignment, it[subjectName] ?: emptyList())
            }
        }
    }

    private fun setupBarChart(chart: BarChart, tvEmpty: TextView, list: List<AssignedTaskForm>) {
        chart.clear()
        val labels: MutableList<String> = ArrayList()
        val entries: MutableList<BarEntry> = ArrayList()

        list.mapIndexed { idx, it  ->
            labels.add(it.title ?: "null")
            entries.add(BarEntry(idx.toFloat(), it.score?.toFloat() ?: 0f))
        }

        tvEmpty.isVisible = labels.isEmpty()

        chart.setScaleEnabled(false)
        chart.axisLeft.axisMinimum = 0f
        chart.axisLeft.axisMaximum = 100f
        chart.setDrawBarShadow(false)
        chart.setDrawValueAboveBar(true)
        chart.description.isEnabled = false
        chart.setPinchZoom(false)
        chart.setScaleEnabled(false)
        chart.setDrawGridBackground(false)
        val yAxis: YAxis = chart.axisLeft

        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        yAxis.granularity = 1f
        yAxis.isGranularityEnabled = true
        chart.axisRight.isEnabled = false
        val xAxis: XAxis = chart.xAxis
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setDrawGridLines(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)

        val set1: BarDataSet
        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as BarDataSet
            set1.values = entries
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create 2 datasets with different types
            set1 = BarDataSet(entries, "SCORE")
            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
            data.barWidth = 0.5f

            chart.data = data
        }
        chart.setFitBars(true)
        chart.legend.isEnabled = false
        chart.setDrawGridBackground(false)
        chart.axisLeft.setDrawGridLines(false)
        chart.xAxis.setDrawGridLines(false)
        chart.animateY(1000)
        chart.invalidate()
    }

}