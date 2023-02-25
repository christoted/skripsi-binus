package com.example.project_skripsi.module.teacher.study_class.homeroom

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.FragmentTcStudyClassHomeroomBinding
import com.example.project_skripsi.utils.generic.ItemClickListener
import com.example.project_skripsi.utils.helper.UIHelper

class TcStudyClassHomeroomFragment : Fragment(), ItemClickListener {

    private lateinit var viewModel: TcStudyClassHomeroomViewModel
    private var _binding: FragmentTcStudyClassHomeroomBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[TcStudyClassHomeroomViewModel::class.java]
        _binding = FragmentTcStudyClassHomeroomBinding.inflate(inflater, container, false)

        retrieveArgs()

        viewModel.studyClass.observe(viewLifecycleOwner, {
            binding.tvClassName.text = it.name
        })

        viewModel.classChief.observe(viewLifecycleOwner) { student ->
            with(binding) {
                tvChiefName.text = student.name
                student.phoneNumber?.let {
                    imvChiefPhone.setImageResource(R.drawable.whatsapp)
                    imvChiefPhone.setOnClickListener {
                        student?.phoneNumber?.let {
                            goToWhatsApp(it)
                        }
                    }
                }

            }
        }

        binding.rvItem.layoutManager = LinearLayoutManager(context)
        viewModel.studentList.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                binding.llParent.addView(
                    UIHelper.getEmptyList("Tidak ada siswa", inflater, binding.llParent)
                )
            } else {
                binding.rvItem.adapter = HomeroomStudentViewHolder(viewModel, it, this).getAdapter()
            }
        })

        binding.imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun goToWhatsApp(phoneNumber: String) {
        val url = "https://api.whatsapp.com/send/?phone=${phoneNumber}"
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun retrieveArgs() {
        val args: TcStudyClassHomeroomFragmentArgs by navArgs()
        viewModel.setClass(args.studyClassId)
    }

    override fun onItemClick(itemId: String) {
        view?.findNavController()?.navigate(
            TcStudyClassHomeroomFragmentDirections
                .actionTcStudyClassHomeroomFragmentToTcStudentFragment(itemId)
        )
    }


}