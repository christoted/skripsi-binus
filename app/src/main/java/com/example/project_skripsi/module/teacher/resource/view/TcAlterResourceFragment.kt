package com.example.project_skripsi.module.teacher.resource.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.project_skripsi.databinding.FragmentTcAlterResourceBinding
import com.example.project_skripsi.module.teacher.main.resource.adapter.ResourceAdapter

class TcAlterResourceFragment : Fragment() {

    private var _binding: FragmentTcAlterResourceBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TcAlterResourceViewModel

    var materialType = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTcAlterResourceBinding.inflate(inflater, container, false)
        //binding.tvTest.text = this.toString().split("{")[0]
        viewModel = ViewModelProvider(this)[TcAlterResourceViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrieveArgs()

        submit()
    }

    private fun submit() {
        binding.toggleButton.addOnButtonCheckedListener { group, checkedId, isChecked ->
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
                    Toast.makeText(context, "Title must be filled", Toast.LENGTH_SHORT).show()
                    viewModel.isValid = false
                }

                if (edtLink.text!!.isEmpty()) {
                    Toast.makeText(context, "Link must be filled", Toast.LENGTH_SHORT).show()
                    viewModel.isValid = false
                }
                toggleButton.isSelectionRequired = true
            }

            if (viewModel.isValid) {
                viewModel.submitResource(binding.edtTitle.text.toString(), viewModel.materialType, binding.edtLink.text.toString())
            }

        }
        viewModel.status.observe(viewLifecycleOwner, {
            if (it) {
                Toast.makeText(context, "Success to Create a Task", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack()
            } else {
                Toast.makeText(context, "Failed to Create a Task", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs() {
        val args: TcAlterResourceFragmentArgs by navArgs()
        with(binding) {
            tvClass.text = ("${args.subjectName} - ${args.gradeLevel}")
        }
        viewModel.initData(args.subjectName, args.gradeLevel)
    }
}