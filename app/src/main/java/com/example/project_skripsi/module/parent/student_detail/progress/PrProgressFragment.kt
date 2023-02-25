package com.example.project_skripsi.module.parent.student_detail.progress

import android.animation.LayoutTransition
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.project_skripsi.databinding.FragmentPrProgressBinding
import com.example.project_skripsi.module.parent.student_detail.payment.PrPaymentFragmentArgs
import com.example.project_skripsi.module.parent.student_detail.progress.PrProgressViewModel.Companion.VIEW_TYPE_ACHIEVEMENT
import com.example.project_skripsi.module.parent.student_detail.progress.PrProgressViewModel.Companion.VIEW_TYPE_ATTENDANCE
import com.example.project_skripsi.module.parent.student_detail.progress.PrProgressViewModel.Companion.VIEW_TYPE_SCORE
import com.example.project_skripsi.module.parent.student_detail.progress.achievement.PrProgressAchievementFragment
import com.example.project_skripsi.module.parent.student_detail.progress.attendance.PrProgressAttendanceFragment
import com.example.project_skripsi.module.parent.student_detail.progress.score.PrProgressScoreFragment
import com.example.project_skripsi.module.student.main.progress.viewmodel.StScoreViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class PrProgressFragment : Fragment() {

    private lateinit var viewModel: PrProgressViewModel
    private var _binding: FragmentPrProgressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[PrProgressViewModel::class.java]
        _binding = FragmentPrProgressBinding.inflate(inflater, container, false)

        binding.vpContainer.adapter = ScreenSlidePagerAdapter(activity!!)
        TabLayoutMediator(binding.tabLayout, binding.vpContainer) { tab, position ->
            tab.text = StScoreViewModel.tabHeader[position]
        }.attach()
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position.let { position ->
                    with(binding) {
                        flScore.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                        flAttendance.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                        flAchievement.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

                        val circleExpand = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            90f,
                            resources.displayMetrics
                        ).toInt()
                        val circleShrink = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            80f,
                            resources.displayMetrics
                        ).toInt()

                        flScore.updateLayoutParams {
                            height = if (position == VIEW_TYPE_SCORE) circleExpand else circleShrink
                        }
                        flAttendance.updateLayoutParams {
                            height =
                                if (position == VIEW_TYPE_ATTENDANCE) circleExpand else circleShrink
                        }
                        flAchievement.updateLayoutParams {
                            height =
                                if (position == VIEW_TYPE_ACHIEVEMENT) circleExpand else circleShrink
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        with(binding) {
            imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }
            imvScore.setOnClickListener {
                Toast.makeText(
                    context,
                    "Rata-rata nilai akhir",
                    Toast.LENGTH_SHORT
                ).show()
            }
            imvAttendance.setOnClickListener {
                Toast.makeText(
                    context,
                    "Persentase kehadiran siswa",
                    Toast.LENGTH_SHORT
                ).show()
            }
            imvAchievement.setOnClickListener {
                Toast.makeText(
                    context,
                    "Jumlah pencapaian",
                    Toast.LENGTH_SHORT
                ).show()
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

        retrieveArgs()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs() {
        val args: PrPaymentFragmentArgs by navArgs()
        viewModel.setStudent(args.studentId)

        binding.btnGraphic.setOnClickListener {
            view?.findNavController()?.navigate(
                PrProgressFragmentDirections.actionPrProgressFragmentToPrProgressGraphicFragment(
                    args.studentId
                )
            )
        }
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int =
            PrProgressViewModel.tabCount

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                VIEW_TYPE_SCORE -> PrProgressScoreFragment(viewModel)
                VIEW_TYPE_ATTENDANCE -> PrProgressAttendanceFragment(viewModel)
                else -> PrProgressAchievementFragment(viewModel)
            }
        }
    }
}