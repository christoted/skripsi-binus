package com.example.project_skripsi.module.teacher.main.task

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.DialogTcTaskTypeBinding
import com.example.project_skripsi.databinding.FragmentTcTaskBinding
import com.example.project_skripsi.databinding.ViewRecyclerViewBinding
import com.example.project_skripsi.module.teacher.form.alter.TcAlterTaskViewModel
import com.example.project_skripsi.module.teacher.form.alter.TcFormAdapter
import com.example.project_skripsi.module.teacher.study_class.task.TaskViewHolder
import com.example.project_skripsi.module.teacher.study_class.task.TcStudyClassTaskViewModel
import com.example.project_skripsi.utils.Constant
import com.example.project_skripsi.utils.generic.ItemClickListener
import com.google.android.material.chip.Chip

class TcTaskFragment : Fragment(), ItemClickListener {

    private lateinit var viewModel : TcTaskViewModel
    private var _binding: FragmentTcTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[TcTaskViewModel::class.java]
        _binding = FragmentTcTaskBinding.inflate(inflater, container, false)
        viewModel.subjectGroupList.observe(viewLifecycleOwner, {
            binding.cgSubjectGroup.removeAllViews()
            binding.cgSubjectGroupBackup.removeAllViews()
            var hasItem = false
            var roundRobin = 0
            it.map { subjectGroup ->
                val chip = inflater.inflate(R.layout.item_tc_chip, binding.cgSubjectGroup, false) as Chip
                chip.id = View.generateViewId()
                chip.text = ("${subjectGroup.gradeLevel}-${subjectGroup.subjectName}")

                val isTop = viewModel.isChipPositionTop(roundRobin)
                chip.setOnCheckedChangeListener { _, b ->
                    if (b) viewModel.selectSubjectGroup(subjectGroup)
                    if (isTop) binding.cgSubjectGroupBackup.clearCheck()
                    else binding.cgSubjectGroup.clearCheck()
                }
                if (isTop) binding.cgSubjectGroup.addView(chip)
                else binding.cgSubjectGroupBackup.addView(chip)
                roundRobin++

                if (!hasItem) {
                    chip.isChecked = true
                    viewModel.selectSubjectGroup(subjectGroup)
                    hasItem = true
                }
            }
        })

        binding.vpContainer.adapter = ScreenSlidePagerAdapter()
        binding.tabLayout.setupWithViewPager(binding.vpContainer)

        binding.btnAdd.setOnClickListener {
            showChoiceDialog()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showChoiceDialog() {
        val dialog = Dialog(context!!)

        val inflater = LayoutInflater.from(context)
        val sBinding = DialogTcTaskTypeBinding.inflate(inflater)

        with(sBinding) {
            viewModel.currentSubjectGroup?.let {
                tvSubjectGroup.text = ("Grup: ${it.gradeLevel} - ${it.subjectName}")
            }

            btnAddExam.setOnClickListener {
                viewModel.currentSubjectGroup?.let {
                    view?.findNavController()?.navigate(
                        TcTaskFragmentDirections.actionTcTaskFragmentToTcAlterTaskFragment(
                            it.subjectName, it.gradeLevel, TcAlterTaskViewModel.TYPE_EXAM, null
                        )
                    )
                }
                dialog.dismiss()
            }

            btnAddAssignment.setOnClickListener {
                viewModel.currentSubjectGroup?.let {
                    view?.findNavController()?.navigate(
                        TcTaskFragmentDirections.actionTcTaskFragmentToTcAlterTaskFragment(
                            it.subjectName,
                            it.gradeLevel,
                            TcAlterTaskViewModel.TYPE_ASSIGNMENT,
                            null
                        )
                    )
                }
                dialog.dismiss()
            }

            dialog.setContentView(root)
        }

        dialog.show()
    }


    override fun onItemClick(itemId: String) {
        viewModel.currentSubjectGroup?.let { subjectGroup ->
            viewModel.getTaskFormType(itemId)?.let { type ->
                view?.findNavController()?.navigate(
                    TcTaskFragmentDirections.actionTcTaskFragmentToTcAlterTaskFragment(
                        subjectGroup.subjectName, subjectGroup.gradeLevel, type, itemId
                    )
                )
            }
        }
    }

    private inner class ScreenSlidePagerAdapter : PagerAdapter(){

        lateinit var layoutInflater: LayoutInflater

        override fun getCount(): Int =
            TcTaskViewModel.tabCount

        override fun isViewFromObject(view: View, `object`: Any): Boolean =
            view == `object`

        override fun getPageTitle(position: Int): CharSequence =
            TcTaskViewModel.tabHeader[position]


        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = LayoutInflater.from(context)
            val bindingRV = ViewRecyclerViewBinding.inflate(layoutInflater, container, false)

            bindingRV.rvContainer.layoutManager = LinearLayoutManager(context)
            when(position) {
                TcTaskViewModel.TAB_EXAM -> {
                    viewModel.examList.observe(viewLifecycleOwner, {
                        bindingRV.rvContainer.adapter = TaskViewHolder(it, this@TcTaskFragment).getAdapter()
                    })
                }
                TcTaskViewModel.TAB_ASSIGNMENT -> {
                    viewModel.assignmentList.observe(viewLifecycleOwner, {
                        bindingRV.rvContainer.adapter = TaskViewHolder(it, this@TcTaskFragment).getAdapter()
                    })
                }
            }

            container.addView(bindingRV.root, 0)
            return bindingRV.root
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}