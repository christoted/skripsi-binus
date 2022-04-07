package com.example.project_skripsi.module.teacher.form.alter_task

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.project_skripsi.databinding.FragmentTcAlterTaskBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R

import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.project_skripsi.module.teacher._sharing.AssignmentViewHolder
import com.example.project_skripsi.module.teacher._sharing.ClassViewHolder
import com.example.project_skripsi.module.teacher._sharing.ResourceViewHolder

import android.widget.TextView
import android.widget.Toast
import com.example.project_skripsi.utils.Constant
import com.example.project_skripsi.utils.helper.DateHelper
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat


class TcAlterTaskFragment : Fragment() {

    private lateinit var viewModel: TcAlterTaskViewModel
    private var _binding: FragmentTcAlterTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[TcAlterTaskViewModel::class.java]
        _binding = FragmentTcAlterTaskBinding.inflate(inflater, container, false)

        with(binding) {

            btnMidExam.setOnClickListener { viewModel.taskType = Constant.TASK_TYPE_MID_EXAM }
            btnFinalExam.setOnClickListener { viewModel.taskType = Constant.TASK_TYPE_FINAL_EXAM }
            btnAssignment.setOnClickListener { viewModel.taskType = Constant.TASK_TYPE_ASSIGNMENT }

            btnStartDate.setOnClickListener{
                activity?.supportFragmentManager?.let { sfm ->
                    val datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Pilih tanggal mulai")
                        .build()
                    datePicker.addOnPositiveButtonClickListener {
                        viewModel.updateStartDate(DateHelper.updateDate(
                            viewModel.startDate.value ?: DateHelper.getCurrentDate(), it
                        ))
                    }
                    datePicker.show(sfm, "Tag")
                }
            }

            btnStartTime.setOnClickListener{
                activity?.supportFragmentManager?.let { sfm ->
                    val timePicker = MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setTitleText("Pilih jam mulai")
                        .build()
                    timePicker.addOnPositiveButtonClickListener {
                        val newHour: Int = timePicker.hour
                        val newMinute: Int = timePicker.minute
                        viewModel.updateStartDate(DateHelper.updateTime(
                            viewModel.startDate.value ?: DateHelper.getCurrentDate(), newHour, newMinute
                        ))
                    }
                    timePicker.show(sfm, "Tag")
                }
            }

            btnEndDate.setOnClickListener{
                activity?.supportFragmentManager?.let { sfm ->
                    val datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Pilih tanggal selesai")
                        .build()
                    datePicker.addOnPositiveButtonClickListener {
                        viewModel.updateEndDate(DateHelper.updateDate(
                            viewModel.endDate.value ?: DateHelper.getCurrentDate(), it
                        ))
                    }
                    datePicker.show(sfm, "Tag")
                }
            }

            btnEndTime.setOnClickListener{
                activity?.supportFragmentManager?.let { sfm ->
                    val timePicker = MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setTitleText("Pilih jam selesai")
                        .build()
                    timePicker.addOnPositiveButtonClickListener {
                        val newHour: Int = timePicker.hour
                        val newMinute: Int = timePicker.minute
                        viewModel.updateEndDate(DateHelper.updateTime(
                            viewModel.endDate.value ?: DateHelper.getCurrentDate(), newHour, newMinute
                        ))
                    }
                    timePicker.show(sfm, "Tag")
                }
            }

            btnClass.setOnClickListener{ showBottomSheet(TcAlterTaskViewModel.QUERY_CLASS) }
            btnPreqResource.setOnClickListener{ showBottomSheet(TcAlterTaskViewModel.QUERY_RESOURCE) }
            btnPreqAssignment.setOnClickListener{ showBottomSheet(TcAlterTaskViewModel.QUERY_ASSIGNMENT) }

            btnCreate.setOnClickListener{
                if (validateInput()) {
                    viewModel.submitForm(edtTitle.text.toString())
                    Toast.makeText(context, "Form sudah terancang", Toast.LENGTH_SHORT).show()
                }
            }

            viewModel.startDate.observe(viewLifecycleOwner, {
                btnStartDate.text = DateHelper.getFormattedDateTime(DateHelper.DMY, it)
                btnStartTime.text = DateHelper.getFormattedDateTime(DateHelper.hm, it)
            })

            viewModel.endDate.observe(viewLifecycleOwner, {
                btnEndDate.text = DateHelper.getFormattedDateTime(DateHelper.DMY, it)
                btnEndTime.text = DateHelper.getFormattedDateTime(DateHelper.hm, it)
            })
        }

        retrieveArgs()

        return binding.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs() {
        val args: TcAlterTaskFragmentArgs by navArgs()
        with(binding) {
            tvSubjectGroup.text = ("${args.subjectName} - ${args.gradeLevel}")
            if (args.formType == TcAlterTaskViewModel.TYPE_ASSIGNMENT) {
                btnMidExam.visibility = View.GONE
                btnFinalExam.visibility = View.GONE
            } else {
                btnAssignment.visibility = View.GONE
            }
        }

        viewModel.initData(args.subjectName, args.gradeLevel, args.formType)
    }


    @SuppressLint("InflateParams")
    private fun showBottomSheet(queryType : Int) {
        val dialog = BottomSheetDialog(context!!)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_tc_alter_task, null)

        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        val btnClose = view.findViewById<Button>(R.id.btn_confirm)

        val rvItem = view.findViewById<RecyclerView>(R.id.rv_item)
        rvItem.layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        rvItem.addItemDecoration(dividerItemDecoration)

        dialog.setContentView(view)
        dialog.show()

        when(queryType) {
            TcAlterTaskViewModel.QUERY_CLASS -> {
                viewModel.classList.observe(viewLifecycleOwner, {
                    val adapter = ClassViewHolder(it, viewModel.selectedClass)
                    tvTitle.text = ("Daftar Kelas")
                    rvItem.adapter = adapter.getAdapter()
                    btnClose.setOnClickListener {
                        viewModel.selectedClass = adapter.getResult()
                        binding.tvClassCount.text = ("terpilih ${adapter.getResult().size}")
                        dialog.dismiss()
                    }
                    viewModel.classList.removeObservers(viewLifecycleOwner)
                })
                viewModel.loadClass()
            }
            TcAlterTaskViewModel.QUERY_RESOURCE -> {
                viewModel.resourceList.observe(viewLifecycleOwner, {
                    val adapter = ResourceViewHolder(it, viewModel.selectedResource)
                    tvTitle.text = ("Daftar Materi")
                    rvItem.adapter = adapter.getAdapter()
                    btnClose.setOnClickListener {
                        viewModel.selectedResource = adapter.getResult()
                        binding.tvResourceCount.text = ("terpilih ${adapter.getResult().size}")
                        dialog.dismiss()
                    }
                    viewModel.resourceList.removeObservers(viewLifecycleOwner)
                })
                viewModel.loadResource()
            }
            TcAlterTaskViewModel.QUERY_ASSIGNMENT -> {
                viewModel.assignmentList.observe(viewLifecycleOwner, {
                    val adapter = AssignmentViewHolder(it, viewModel.selectedAssignment)
                    tvTitle.text = ("Daftar Tugas")
                    rvItem.adapter = adapter.getAdapter()
                    btnClose.setOnClickListener {
                        viewModel.selectedAssignment = adapter.getResult()
                        binding.tvAssignmentCount.text = ("terpilih ${adapter.getResult().size}")
                        dialog.dismiss()
                    }
                    viewModel.assignmentList.removeObservers(viewLifecycleOwner)
                })
                viewModel.loadAssignment()
            }
        }
    }

    private fun validateInput() : Boolean {
        with(binding) {
            if (edtTitle.text.toString().isEmpty()) {
                Toast.makeText(context, "Judul harus diisi",Toast.LENGTH_SHORT).show()
                return false
            }

            if (viewModel.taskType.isEmpty()) {
                Toast.makeText(context, "Tipe ujian harus dipilih",Toast.LENGTH_SHORT).show()
                return false
            }

            if (viewModel.startDate.value!! > viewModel.endDate.value!!) {
                Toast.makeText(context, "Tanggal mulai harus mendahului tanggal selesai",Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }
}