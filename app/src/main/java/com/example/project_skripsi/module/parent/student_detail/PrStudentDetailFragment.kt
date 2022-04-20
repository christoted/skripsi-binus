package com.example.project_skripsi.module.parent.student_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.project_skripsi.databinding.FragmentPrStudentDetailBinding

class PrStudentDetailFragment : Fragment() {

    private lateinit var viewModel: PrStudentDetailViewModel
    private var _binding: FragmentPrStudentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[PrStudentDetailViewModel::class.java]
        _binding = FragmentPrStudentDetailBinding.inflate(inflater, container, false)

        viewModel.student.observe(viewLifecycleOwner, { student ->
            student.id?.let { id ->
                with(binding) {
                    btnPayment.setOnClickListener {
                        view?.findNavController()?.navigate(
                            PrStudentDetailFragmentDirections.actionPrStudentDetailFragmentToPrPaymentFragment(id)) }

                    btnCalendar.setOnClickListener {
                        view?.findNavController()?.navigate(
                            PrStudentDetailFragmentDirections.actionPrStudentDetailFragmentToPrCalendarFragment()) }

                    btnProgress.setOnClickListener {
                        view?.findNavController()?.navigate(
                            PrStudentDetailFragmentDirections.actionPrStudentDetailFragmentToPrProgressFragment()) }

                    btnExam.setOnClickListener {
                        view?.findNavController()?.navigate(
                            PrStudentDetailFragmentDirections.actionPrStudentDetailFragmentToPrExamFragment()) }

                    btnAssignment.setOnClickListener {
                        view?.findNavController()?.navigate(
                            PrStudentDetailFragmentDirections.actionPrStudentDetailFragmentToPrAssignmentFragment()) }

                    btnAnnouncement.setOnClickListener {
                        view?.findNavController()?.navigate(
                            PrStudentDetailFragmentDirections.actionPrStudentDetailFragmentToPrAnnouncementFragment()) }
                }
            }
        })

        retrieveArgs()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs() {
        val args: PrStudentDetailFragmentArgs by navArgs()
        viewModel.setStudent(args.studentId)
    }
}