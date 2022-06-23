package com.example.project_skripsi.module.parent.student_detail

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
import com.bumptech.glide.Glide
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.FragmentPrStudentDetailBinding
import com.example.project_skripsi.databinding.ViewEmptyItemBinding
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

            binding.tvProfileName.text = ("${student.name} (${student.attendanceNumber})")

            student.profile?.let { imageUrl ->
                Glide
                    .with(requireContext())
                    .load(imageUrl)
                    .into(binding.ivProfilePicture)
            }

            student.id?.let { id ->
                with(binding) {
                    ivPayment.setOnClickListener {
                        view?.findNavController()?.navigate(
                            PrStudentDetailFragmentDirections.actionPrStudentDetailFragmentToPrPaymentFragment(
                                id
                            )
                        )
                    }

                    ivCalendar.setOnClickListener {
                        view?.findNavController()?.navigate(
                            PrStudentDetailFragmentDirections.actionPrStudentDetailFragmentToPrCalendarFragment(
                                id
                            )
                        )
                    }

                    ivProgress.setOnClickListener {
                        view?.findNavController()?.navigate(
                            PrStudentDetailFragmentDirections.actionPrStudentDetailFragmentToPrProgressFragment(
                                id
                            )
                        )
                    }

                    ivExam.setOnClickListener {
                        view?.findNavController()?.navigate(
                            PrStudentDetailFragmentDirections.actionPrStudentDetailFragmentToPrExamFragment(
                                id
                            )
                        )
                    }

                    ivAssignment.setOnClickListener {
                        view?.findNavController()?.navigate(
                            PrStudentDetailFragmentDirections.actionPrStudentDetailFragmentToPrAssignmentFragment(
                                id
                            )
                        )
                    }

                    ivAnnouncement.setOnClickListener {
                        view?.findNavController()?.navigate(
                            PrStudentDetailFragmentDirections.actionPrStudentDetailFragmentToPrAnnouncementFragment()
                        )
                    }
                }
            }
        })

        viewModel.school.observe(viewLifecycleOwner, { binding.tvSchoolName.text = it.name })
        viewModel.studyClass.observe(viewLifecycleOwner, { binding.tvClassName.text = it.name })
        viewModel.homeroomTeacher.observe(viewLifecycleOwner) { teacher ->
            with(binding) {
                tvTeacherName.text = teacher.name
                teacher.phoneNumber?.let {
                    imvTeacherPhone.setImageResource(R.drawable.whatsapp)
                    imvTeacherPhone.setOnClickListener {
                        teacher.phoneNumber?.let {
                            goToWhatsApp(it)
                        }
                    }
                }

            }
        }

        binding.rvContainer.layoutManager = LinearLayoutManager(context)
        viewModel.subjectList.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                val emptyView = ViewEmptyItemBinding.inflate(inflater, binding.llParent, false)
                emptyView.tvEmpty.text = ("Tidak ada mata pelajaran")
                binding.llParent.addView(emptyView.root)
            } else {
                binding.rvContainer.adapter = PrSubjectViewHolder(it).getAdapter()
            }
        })

        retrieveArgs()

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
        val args: PrStudentDetailFragmentArgs by navArgs()
        viewModel.setStudent(args.studentId)
    }
}