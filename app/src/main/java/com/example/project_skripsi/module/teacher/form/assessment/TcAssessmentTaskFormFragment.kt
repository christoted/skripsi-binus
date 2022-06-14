package com.example.project_skripsi.module.teacher.form.assessment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.Answer
import com.example.project_skripsi.databinding.FragmentTcAssessmentTaskFormBinding
import com.example.project_skripsi.utils.Constant
import com.example.project_skripsi.utils.helper.ValidationHelper

class TcAssessmentTaskFormFragment : Fragment() {

    private lateinit var viewModel: TcAssessmentTaskFormViewModel
    private var _binding: FragmentTcAssessmentTaskFormBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[TcAssessmentTaskFormViewModel::class.java]
        _binding = FragmentTcAssessmentTaskFormBinding.inflate(inflater, container, false)

        retrieveArgs()

        binding.rvQuestion.layoutManager = LinearLayoutManager(context)
        binding.rvQuestion.isNestedScrollingEnabled = true
        viewModel.questionList.observe(viewLifecycleOwner, { questions ->
            val adapter = TcAssessmentFormAdapter(questions)
            binding.rvQuestion.adapter = adapter

            binding.btnConfirm.setOnClickListener {
                var isOK = true
                questions.mapIndexed { index, assignedQuestion ->
                    if (assignedQuestion.type == Constant.TASK_FORM_ESSAY) {
                        with(binding.rvQuestion.getChildAt(index)) {
                            val score = findViewById<TextView>(R.id.edt_score).text.toString()
                            if (ValidationHelper.isStringEmpty(context, score, "Nilai - ${index+1}")) {
                                isOK = false
                                return@mapIndexed
                            }
                            if (ValidationHelper.isStringInteger(context, score, "Nilai - ${index+1}")) {
                                isOK = false
                                return@mapIndexed
                            }
                            val scoreInt = score.toInt()
                            if (scoreInt < 0 || scoreInt > (assignedQuestion.scoreWeight ?: 0)) {
                                Toast.makeText(
                                    context,
                                    "Nilai harus 0 - ${assignedQuestion.scoreWeight}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@mapIndexed
                            }
                            adapter.questionList[index].answer = Answer(
                                assignedQuestion.answer?.answerText,
                                scoreInt,
                                adapter.questionList[index].answer?.images
                            )
                        }
                    } else {
                        with(binding.rvQuestion.getChildAt(index)) {
                            val score = findViewById<TextView>(R.id.tv_score).text.toString().toInt()
                            adapter.questionList[index].answer = Answer(
                                assignedQuestion.answer?.answerText,
                                score,
                                adapter.questionList[index].answer?.images
                            )
                        }
                    }
                }
                if (isOK) {
                    viewModel.assignedAnswers = adapter.questionList.map { it.answer!! }
                    viewModel.submitResult()
                    binding.btnConfirm.isEnabled = false
                }
            }
        })

        viewModel.student.observe(viewLifecycleOwner, {
            with(binding) {
                tvAttendanceNumber.text = it.attendanceNumber.toString()
                tvName.text = it.name
            }
        })

        viewModel.assignedTaskForm.observe(viewLifecycleOwner, {
            if (it.isChecked == true) {
                with(binding) {
                    tvScoreHeader.visibility = View.VISIBLE
                    tvScore.visibility = View.VISIBLE
                    tvScore.text = it.score.toString()
                }
            }
        })

        viewModel.assessmentCompleted.observe(viewLifecycleOwner, {
            if (it) {
                Toast.makeText(context,"Penilaian sukses", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs(){
        val args: TcAssessmentTaskFormFragmentArgs by navArgs()
        viewModel.setAssignedTaskForm(args.studentId, args.taskFormId)
    }


}