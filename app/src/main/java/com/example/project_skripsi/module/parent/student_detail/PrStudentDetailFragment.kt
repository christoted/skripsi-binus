package com.example.project_skripsi.module.parent.student_detail

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
import com.example.project_skripsi.databinding.FragmentPrStudentDetailBinding
import com.example.project_skripsi.module.parent.student_detail.viewholder.PrSubjectViewHolder

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
                    ivPayment.setOnClickListener {
                        view?.findNavController()?.navigate(
                            PrStudentDetailFragmentDirections.actionPrStudentDetailFragmentToPrPaymentFragment(id)) }

                    ivCalendar.setOnClickListener {
                        view?.findNavController()?.navigate(
                            PrStudentDetailFragmentDirections.actionPrStudentDetailFragmentToPrCalendarFragment(id)) }

                    ivProgress.setOnClickListener {
                        view?.findNavController()?.navigate(
                            PrStudentDetailFragmentDirections.actionPrStudentDetailFragmentToPrProgressFragment(id)) }

                    ivExam.setOnClickListener {
                        view?.findNavController()?.navigate(
                            PrStudentDetailFragmentDirections.actionPrStudentDetailFragmentToPrExamFragment(id)) }

                    ivAssignment.setOnClickListener {
                        view?.findNavController()?.navigate(
                            PrStudentDetailFragmentDirections.actionPrStudentDetailFragmentToPrAssignmentFragment(id)) }

                    ivAnnouncement.setOnClickListener {
                        view?.findNavController()?.navigate(
                            PrStudentDetailFragmentDirections.actionPrStudentDetailFragmentToPrAnnouncementFragment()) }
                }
            }
        })

        viewModel.school.observe(viewLifecycleOwner, { binding.tvSchoolName.text = it.name })
        viewModel.studyClass.observe(viewLifecycleOwner, { binding.tvClassName.text = it.name })
        viewModel.homeroomTeacher.observe(viewLifecycleOwner, {
            with(binding) {
                tvTeacherName.text = it.name
                it.phoneNumber?.let { imvTeacherPhone.setImageResource(R.drawable.whatsapp) }
            }
        })

        binding.rvContainer.layoutManager = LinearLayoutManager(context)
        viewModel.subjectList.observe(viewLifecycleOwner, {
            binding.rvContainer.adapter = PrSubjectViewHolder(it).getAdapter()
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