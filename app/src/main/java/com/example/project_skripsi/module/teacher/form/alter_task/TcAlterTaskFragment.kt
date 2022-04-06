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
import com.google.android.material.bottomsheet.BottomSheetBehavior

import android.view.MotionEvent




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

            btnClass.setOnClickListener{ showBottomSheet(TcAlterTaskViewModel.QUERY_CLASS) }
            btnPreqResource.setOnClickListener{ showBottomSheet(TcAlterTaskViewModel.QUERY_RESOURCE) }
            btnPreqAssignment.setOnClickListener{ showBottomSheet(TcAlterTaskViewModel.QUERY_ASSIGNMENT) }

            btnCreate.setOnClickListener{viewModel.submitForm()}
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
                    rvItem.adapter = adapter.getAdapter()
                    btnClose.setOnClickListener {
                        viewModel.selectedClass = adapter.getResult()
                        dialog.dismiss()
                    }
                    viewModel.classList.removeObservers(viewLifecycleOwner)
                })
                viewModel.loadClass()
            }
            TcAlterTaskViewModel.QUERY_RESOURCE -> {
                viewModel.resourceList.observe(viewLifecycleOwner, {
                    val adapter = ResourceViewHolder(it, viewModel.selectedResource)
                    rvItem.adapter = adapter.getAdapter()
                    btnClose.setOnClickListener {
                        viewModel.selectedResource = adapter.getResult()
                        dialog.dismiss()
                    }
                    viewModel.resourceList.removeObservers(viewLifecycleOwner)
                })
                viewModel.loadResource()
            }
            TcAlterTaskViewModel.QUERY_ASSIGNMENT -> {
                viewModel.assignmentList.observe(viewLifecycleOwner, {
                    val adapter = AssignmentViewHolder(it, viewModel.selectedAssignment)
                    rvItem.adapter = adapter.getAdapter()
                    btnClose.setOnClickListener {
                        viewModel.selectedAssignment = adapter.getResult()
                        dialog.dismiss()
                    }
                    viewModel.assignmentList.removeObservers(viewLifecycleOwner)
                })
                viewModel.loadAssignment()
            }
        }
    }
}