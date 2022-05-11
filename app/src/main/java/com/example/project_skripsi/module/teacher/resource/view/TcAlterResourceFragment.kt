package com.example.project_skripsi.module.teacher.resource.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.util.Log
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.FragmentTcAlterResourceBinding
import com.example.project_skripsi.module.teacher._sharing.ClassViewHolder
import com.example.project_skripsi.module.teacher._sharing.ResourceViewHolder
import com.google.android.material.bottomsheet.BottomSheetDialog

class TcAlterResourceFragment : Fragment() {

    private var _binding: FragmentTcAlterResourceBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TcAlterResourceViewModel

    private var materialType = ""

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
        binding.toggleButton.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when(checkedId) {
                    binding.btnSlide.id -> {
                        viewModel.materialType = "Slide"

                    }
                    binding.btnRecording.id -> {
                        viewModel.materialType = "Recording"
                    }
                }
            }
        }
        binding.btnSlide.setOnClickListener {
            materialType = "Slide"
        }
        binding.btnRecording.setOnClickListener {
            materialType = "Recording"
        }
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
                toggleButton.isSelectionRequired = true
            }

            if (viewModel.isValid) {
                viewModel.submitResource(binding.edtTitle.text.toString(), viewModel.materialType, binding.edtLink.text.toString())
            }
        }
        viewModel.status.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(context, "Materi baru berhasil dibuat", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack()
            } else {
                Toast.makeText(context, "Materi berhasil diubah", Toast.LENGTH_SHORT).show()
            }
        }

        // Button
        with(binding) {
            btnClass.setOnClickListener {
                showBottomSheet(TcAlterResourceViewModel.QUERY_CLASS)
            }
            btnRequirementResource.setOnClickListener {
                showBottomSheet(TcAlterResourceViewModel.QUERY_RESOURCE)
            }
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
            binding.btnUpload.text = ("Update Resource")
        }
        viewModel.initData(args.subjectName, args.gradeLevel)
        setData()
    }

    private fun setData() {
        viewModel.singleResource.observe(viewLifecycleOwner) {
            Log.d("333", "setData: $it")
            with(binding) {
                edtTitle.setText(it.title)
                when(it.type) {
                    "slide", "Slide" -> toggleButton.check(R.id.btn_slide)
                    "recording", "Recording" -> toggleButton.check(R.id.btn_recording)
                    else -> Log.d("333", "setData: masuk bawah")
                }
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
            TcAlterResourceViewModel.QUERY_CLASS -> {
                viewModel.classList.observe(viewLifecycleOwner) {
                    val viewHolder = ClassViewHolder(it, viewModel.selectedClass)
                    tvTitle.text = ("Daftar Kelas")
                    rvItem.adapter = viewHolder.getAdapter()
                    btnClose.setOnClickListener {
                        viewModel.selectedClass = viewHolder.getResult()
                        binding.tvClassChoosen.text =
                            "Terpilih " + viewHolder.getResult().size + " Kelas"
                        dialog.dismiss()
                    }
                }
                viewModel.loadClass()
            }
            TcAlterResourceViewModel.QUERY_RESOURCE -> {
                viewModel.resourceList.observe(viewLifecycleOwner) {
                    val viewHolder = ResourceViewHolder(it, viewModel.selectedResource)
                    tvTitle.text = ("Daftar Materi")
                    rvItem.adapter = viewHolder.getAdapter()
                    btnClose.setOnClickListener {
                        viewModel.selectedResource = viewHolder.getResult()
                        binding.tvRequirementResource.text =
                            "Terpilih " + viewHolder.getResult().size + " Materi"

                        dialog.dismiss()
                    }
                }

                viewModel.loadResource()
            }
        }


    }
}