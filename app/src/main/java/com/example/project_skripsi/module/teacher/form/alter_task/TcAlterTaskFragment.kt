package com.example.project_skripsi.module.teacher.form.alter_task

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.databinding.FragmentTcAlterTaskBinding

import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.ItemStTaskBinding
import com.example.project_skripsi.databinding.StandardRecyclerViewBinding

import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.recyclerview.widget.DividerItemDecoration








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


//        binding.rvClass.layoutManager = GridLayoutManager(context, 3)
//        binding.rvClass.adapter = ClassViewHolder(
//            listOf(
//                StudyClass(name = "XII-IPA-1"),
//                StudyClass(name = "XII-IPA-2"),
//                StudyClass(name = "XII-IPA-3"),
//                StudyClass(name = "XII-IPA-4"),
//                StudyClass(name = "XII-IPA-5"),
//            )
//        ).getAdapter()

        binding.btnClass.setOnClickListener{ showBottomSheet(TcAlterTaskViewModel.QUERY_RESOURCE) }
        binding.btnPreqResource.setOnClickListener{ showBottomSheet(TcAlterTaskViewModel.QUERY_RESOURCE) }
        binding.btnPreqTaskForm.setOnClickListener{ showBottomSheet(TcAlterTaskViewModel.QUERY_TASK_FORM) }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()

        when(queryType) {
            TcAlterTaskViewModel.QUERY_CLASS -> {
                viewModel.loadClass()
                viewModel.classList.observe(viewLifecycleOwner, {
                    val adapter = ClassViewHolder(it, viewModel.selectedClass)
                    rvItem.adapter = adapter.getAdapter()
                    btnClose.setOnClickListener {
                        viewModel.selectedClass = adapter.getResult()
                        dialog.dismiss()
                    }
                })
            }
            TcAlterTaskViewModel.QUERY_RESOURCE -> {

            }
            TcAlterTaskViewModel.QUERY_TASK_FORM -> {

            }
        }
    }
}