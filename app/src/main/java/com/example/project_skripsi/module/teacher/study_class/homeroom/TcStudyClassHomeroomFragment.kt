package com.example.project_skripsi.module.teacher.study_class.homeroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.FragmentTcStudyClassHomeroomBinding
import com.example.project_skripsi.utils.generic.ItemClickListener

class TcStudyClassHomeroomFragment : Fragment(), ItemClickListener {

    private lateinit var viewModel : TcStudyClassHomeroomViewModel
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

        viewModel.classChief.observe(viewLifecycleOwner, {
            with(binding) {
                tvChiefName.text = it.name
                it.phoneNumber?.let { imvChiefPhone.setImageResource(R.drawable.whatsapp) }
            }
        })

        binding.rvItem.layoutManager = LinearLayoutManager(context)
        viewModel.studentList.observe(viewLifecycleOwner, {
            binding.rvItem.adapter = HomeroomStudentViewHolder(viewModel, it, this).getAdapter()
        })

        binding.imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs(){
//        val args:  by navArgs()
        val args = "eMsulnik6kEpW0ESKI9V"
        viewModel.setClass(args)
    }

    override fun onItemClick(itemId: String) {
        view?.findNavController()?.navigate(TcStudyClassHomeroomFragmentDirections
            .actionTcStudyClassHomeroomFragmentToTcStudentFragment(itemId))
    }


}