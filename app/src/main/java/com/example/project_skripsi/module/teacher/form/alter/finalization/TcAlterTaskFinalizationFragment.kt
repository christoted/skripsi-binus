package com.example.project_skripsi.module.teacher.form.alter.finalization

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.FragmentTcAlterTaskFinalizationBinding
import com.example.project_skripsi.module.teacher._sharing.ClassViewHolder
import com.example.project_skripsi.module.teacher.form.alter.TcAlterTaskViewModel
import com.example.project_skripsi.module.teacher.form.alter.finalization.TcAlterTaskFinalizationViewModel.Companion.mapFormType
import com.example.project_skripsi.utils.helper.DateHelper
import com.google.android.material.bottomsheet.BottomSheetDialog


class TcAlterTaskFinalizationFragment : Fragment() {

    private lateinit var viewModel: TcAlterTaskFinalizationViewModel
    private var _binding: FragmentTcAlterTaskFinalizationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[TcAlterTaskFinalizationViewModel::class.java]
        _binding = FragmentTcAlterTaskFinalizationBinding.inflate(inflater, container, false)

        with(binding) {
            btnClass.setOnClickListener { showBottomSheet() }

            viewModel.oldTaskForm.observe(viewLifecycleOwner, {
                with(binding) {
                    tvTitle.text = it.title
                    tvStartTime.text =
                        ("${DateHelper.getFormattedDateTimeWithWeekDay(it.startTime)}, ${
                            DateHelper.getFormattedDateTime(
                                DateHelper.hm,
                                it.startTime
                            )
                        }")
                    tvEndTime.text = ("${DateHelper.getFormattedDateTimeWithWeekDay(it.endTime)}, ${
                        DateHelper.getFormattedDateTime(
                            DateHelper.hm,
                            it.endTime
                        )
                    }")
                    val duration = DateHelper.getDuration(it.startTime, it.endTime)
                    tvDuration.text = ("${duration.first} ${duration.second}")
                    tvFormType.text = mapFormType[it.type]
                    tvPreqResource.text = (it.prerequisiteResources?.size ?: 0).toString()
                    tvPreqAssignment.text = (it.prerequisiteTaskForms?.size ?: 0).toString()
                    tvQuestionCount.text = (it.questions?.size ?: 0).toString()
                }
            })

            viewModel.finalizationCompleted.observe(viewLifecycleOwner, {
                if (it) {
                    Toast.makeText(context, "Formulir telah difinalisasi", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().popBackStack()
                }
                btnConfirm.isEnabled = true
            })

            btnConfirm.setOnClickListener {
                if (validateInput()) {
                    viewModel.submitForm()
                    btnConfirm.isEnabled = false
                }
            }
        }

        binding.imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }

        retrieveArgs()
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs() {
        val args: TcAlterTaskFinalizationFragmentArgs by navArgs()
        binding.tvSubjectGroup.text = ("${args.subjectName} - ${args.gradeLevel}")
        viewModel.initData(args.subjectName, args.gradeLevel, args.formType, args.taskFormId)
        if (args.formType == TcAlterTaskViewModel.TYPE_EXAM) {
            binding.trPreqResource.visibility = View.GONE
            binding.trPreqAssignment.visibility = View.GONE
        }
    }


    @SuppressLint("InflateParams")
    private fun showBottomSheet() {
        val dialog = BottomSheetDialog(context!!)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_tc_alter_task_general, null)

        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        val btnConfirm = view.findViewById<Button>(R.id.btn_confirm)
        val btnCancel = view.findViewById<Button>(R.id.btn_cancel)

        val rvItem = view.findViewById<RecyclerView>(R.id.rv_item)
        rvItem.layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        rvItem.addItemDecoration(dividerItemDecoration)

        viewModel.classList.observe(viewLifecycleOwner, {
            val adapter = ClassViewHolder(it, viewModel.selectedClass)
            tvTitle.text = ("Daftar Kelas")
            rvItem.adapter = adapter.getAdapter()
            btnConfirm.setOnClickListener {
                viewModel.selectedClass = adapter.getResult()
                binding.tvClassCount.text = ("terpilih ${adapter.getResult().size}")
                dialog.dismiss()
            }
            viewModel.classList.removeObservers(viewLifecycleOwner)
        })
        viewModel.loadClass()

        btnCancel.setOnClickListener { dialog.dismiss() }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun validateInput(): Boolean {
        if (viewModel.selectedClass.isEmpty()) {
            Toast.makeText(context, "Jumlah kelas harus minimal 1", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}