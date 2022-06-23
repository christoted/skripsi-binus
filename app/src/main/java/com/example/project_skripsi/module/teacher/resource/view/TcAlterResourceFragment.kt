package com.example.project_skripsi.module.teacher.resource.view

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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.BottomSheetTcChooseMeetingBinding
import com.example.project_skripsi.databinding.FragmentTcAlterResourceBinding
import com.example.project_skripsi.module.teacher._sharing.ClassViewHolder
import com.example.project_skripsi.module.teacher._sharing.ResourceViewHolder
import com.example.project_skripsi.module.teacher.resource.view.TcAlterResourceViewModel.Companion.QUERY_CLASS
import com.example.project_skripsi.module.teacher.resource.view.TcAlterResourceViewModel.Companion.QUERY_RESOURCE
import com.example.project_skripsi.module.teacher.resource.view.TcAlterResourceViewModel.Companion.mapOfMeetingLink
import com.example.project_skripsi.utils.generic.ItemClickListener
import com.google.android.material.bottomsheet.BottomSheetDialog

class TcAlterResourceFragment : Fragment(), ItemClickListener {

    private var _binding: FragmentTcAlterResourceBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TcAlterResourceViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTcAlterResourceBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[TcAlterResourceViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrieveArgs()

        submit()
    }

    private fun submit() {
        binding.btnUpload.setOnClickListener {
            with(binding) {
                if (edtTitle.text!!.isEmpty()) {
                    Toast.makeText(context, "Judul materi harus diisi", Toast.LENGTH_SHORT).show()
                    viewModel.isValid = false
                }

                if (edtLink.text!!.isEmpty()) {
                    Toast.makeText(context, "Link materi harus diisi", Toast.LENGTH_SHORT).show()
                    viewModel.isValid = false
                }
            }

            if (viewModel.isValid) {
                binding.btnUpload.isEnabled = false
                val meetingNumber = binding.btnMeetingNumber.text.toString().toInt()
                viewModel.submitResource(binding.edtTitle.text.toString(), meetingNumber, binding.edtLink.text.toString())
            }
        }
        viewModel.status.observe(viewLifecycleOwner) {
            if (it) {
                if (viewModel.isFirstTimeCreated) {
                    Toast.makeText(context, "Materi baru berhasil dibuat", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Materi berhasil diubah", Toast.LENGTH_SHORT).show()
                }
                view?.findNavController()?.popBackStack()
            } else {
                if (viewModel.isFirstTimeCreated) {
                    Toast.makeText(context, "Materi gagal dibuat", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Materi gagal diubah", Toast.LENGTH_SHORT).show()
                }
            }
            binding.btnUpload.isEnabled = true
        }

        // Button
        with(binding) {
            btnMeetingNumber.setOnClickListener { showBottomSheetMeeting() }
            btnClass.setOnClickListener { showBottomSheet(QUERY_CLASS) }
            btnRequirementResource.setOnClickListener { showBottomSheet(QUERY_RESOURCE) }
            imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs() {
        val args: TcAlterResourceFragmentArgs by navArgs()
        with(binding) {
            tvSubjectGroup.text = ("${args.subjectName} - ${args.gradeLevel}")
        }
        args.documentId?.let {
            viewModel.resourceDocumentId = it
            viewModel.getAlterResourceData()
        }
        viewModel.initData(args.subjectName, args.gradeLevel)
        setData()
    }

    private fun setData() {
        viewModel.singleResource.observe(viewLifecycleOwner) {
            with(binding) {
                edtTitle.setText(it.title)
                btnMeetingNumber.text = it.meetingNumber?.toString() ?: "0"
                edtLink.setText(it.link)
                tvClassChoosen.text = ("${it.assignedClasses?.size} Terpilih")
                tvRequirementResource.text = ("${it.prerequisites?.size} Terpilih")
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun showBottomSheet(queryType : Int) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_tc_alter_task_general, null)

        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        val btnClose = view.findViewById<Button>(R.id.btn_confirm)

        val rvItem = view.findViewById<RecyclerView>(R.id.rv_item)
        rvItem.layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        rvItem.addItemDecoration(dividerItemDecoration)

        dialog.setContentView(view)
        dialog.show()

        when(queryType) {
            QUERY_CLASS -> {
                viewModel.classList.observe(viewLifecycleOwner) {
                    val viewHolder = ClassViewHolder(it, viewModel.selectedClass)
                    tvTitle.text = ("Daftar Kelas")
                    rvItem.adapter = viewHolder.getAdapter()
                    btnClose.setOnClickListener {
                        viewModel.selectedClass = viewHolder.getResult()
                        binding.tvClassChoosen.text =
                            ("Terpilih " + viewHolder.getResult().size + " Kelas")
                        dialog.dismiss()
                    }
                }
                viewModel.loadClass()
            }
            QUERY_RESOURCE -> {
                viewModel.resourceList.observe(viewLifecycleOwner) {
                    val viewHolder = ResourceViewHolder(it, viewModel.selectedResource)
                    tvTitle.text = ("Daftar Materi")
                    rvItem.adapter = viewHolder.getAdapter()
                    btnClose.setOnClickListener {
                        viewModel.selectedResource = viewHolder.getResult()
                        binding.tvRequirementResource.text =
                            ("Terpilih " + viewHolder.getResult().size + " Materi")
                        dialog.dismiss()
                    }
                }
                viewModel.loadResource()
            }
        }
    }

    var dialog : BottomSheetDialog? = null

    @SuppressLint("InflateParams")
    private fun showBottomSheetMeeting() {
        dialog = BottomSheetDialog(requireContext())
        val sBinding = BottomSheetTcChooseMeetingBinding.inflate(LayoutInflater.from(context))

        dialog?.let {
            with(sBinding) {
                rvItem.layoutManager = GridLayoutManager(context, 4)
                rvItem.adapter = TcMeetingNumberViewHolder(TcAlterResourceViewModel.meetingNumbers, this@TcAlterResourceFragment).getAdapter()
                it.setContentView(sBinding.root)
            }
            it.show()
        }

    }

    override fun onItemClick(itemId: String) {
        dialog?.dismiss()
        binding.btnMeetingNumber.text = itemId
        mapOfMeetingLink[itemId]?.let { binding.edtLink.setText(it) }
    }
}