package com.example.project_skripsi.module.student.main.progress.view

import android.animation.LayoutTransition
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.updateLayoutParams
import androidx.navigation.findNavController
import com.example.project_skripsi.databinding.FragmentStProgressBinding
import com.example.project_skripsi.module.parent.student_detail.progress.PrProgressViewModel
import com.example.project_skripsi.module.student.main.progress.view.adapter.StScoreViewPagerAdapter
import com.example.project_skripsi.module.student.main.progress.viewmodel.StScoreViewModel
import com.google.android.material.tabs.TabLayoutMediator

import com.google.android.material.tabs.TabLayout

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
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position.let { position ->
                    with(binding) {
                        flScore.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                        flAttendance.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                        flAchievement.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

                        val circleExpand = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90f, resources.displayMetrics).toInt()
                        val circleShrink = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80f, resources.displayMetrics).toInt()

                        flScore.updateLayoutParams { height = if (position == PrProgressViewModel.VIEW_TYPE_SCORE) circleExpand else circleShrink }
                        flAttendance.updateLayoutParams { height = if (position == PrProgressViewModel.VIEW_TYPE_ATTENDANCE) circleExpand else circleShrink }
                        flAchievement.updateLayoutParams { height = if (position == PrProgressViewModel.VIEW_TYPE_ACHIEVEMENT) circleExpand else circleShrink }
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        with(binding) {
            imvScore.setOnClickListener { Toast.makeText(context, "Rata-rata nilai akhir", Toast.LENGTH_SHORT).show() }
            imvAttendance.setOnClickListener { Toast.makeText(context, "Jumlah Absen", Toast.LENGTH_SHORT).show() }
            imvAchievement.setOnClickListener { Toast.makeText(context, "Jumlah Pencapaian", Toast.LENGTH_SHORT).show() }

            btnGraphic.setOnClickListener {
                view?.findNavController()?.navigate(
                    StScoreFragmentDirections.actionNavigationScoreFragmentToStProgressGraphicFragment()
                )
            }
        }

        viewModel.scoreFragmentData.observe(viewLifecycleOwner, {
            with(binding) {
                cpvScore.setValueAnimated(it.totalScore.toFloat())
                cpvAttendance.setValueAnimated(it.totalAbsent.toFloat())
                cpvAchievement.setValueAnimated(it.totalAchievement.toFloat())
                tvScore.text = it.totalScore.toString()
                tvAttendance.text = it.totalAbsent.toString()
                tvAchievement.text = it.totalAchievement.toString()
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}