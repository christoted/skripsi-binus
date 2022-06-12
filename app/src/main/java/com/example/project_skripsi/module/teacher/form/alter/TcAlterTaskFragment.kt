package com.example.project_skripsi.module.teacher.form.alter

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.Question
import com.example.project_skripsi.databinding.FragmentTcAlterTaskBinding
import com.example.project_skripsi.module.teacher._sharing.AssignmentViewHolder
import com.example.project_skripsi.module.teacher._sharing.ResourceViewHolder
import com.example.project_skripsi.utils.Constant
import com.example.project_skripsi.utils.helper.DateHelper
import com.example.project_skripsi.utils.helper.ValidationHelper.Companion.isStringEmpty
import com.example.project_skripsi.utils.helper.ValidationHelper.Companion.isStringInteger
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat


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

        with(binding) {

            btnMidExam.setOnClickListener { viewModel.taskType = Constant.TASK_TYPE_MID_EXAM }
            btnFinalExam.setOnClickListener { viewModel.taskType = Constant.TASK_TYPE_FINAL_EXAM }
            btnAssignment.setOnClickListener { viewModel.taskType = Constant.TASK_TYPE_ASSIGNMENT }

            btnStartDate.setOnClickListener{
                activity?.supportFragmentManager?.let { sfm ->
                    val datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Pilih tanggal mulai")
                        .build()
                    datePicker.addOnPositiveButtonClickListener {
                        viewModel.updateStartDate(DateHelper.updateDate(
                            viewModel.startDate.value ?: DateHelper.getCurrentTime(), it
                        ))
                    }
                    datePicker.show(sfm, "Tag")
                }
            }

            btnStartTime.setOnClickListener{
                activity?.supportFragmentManager?.let { sfm ->
                    val timePicker = MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setTitleText("Pilih jam mulai")
                        .build()
                    timePicker.addOnPositiveButtonClickListener {
                        val newHour: Int = timePicker.hour
                        val newMinute: Int = timePicker.minute
                        viewModel.updateStartDate(DateHelper.updateTime(
                            viewModel.startDate.value ?: DateHelper.getCurrentTime(), newHour, newMinute
                        ))
                    }
                    timePicker.show(sfm, "Tag")
                }
            }

            btnEndDate.setOnClickListener{
                activity?.supportFragmentManager?.let { sfm ->
                    val datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Pilih tanggal selesai")
                        .build()
                    datePicker.addOnPositiveButtonClickListener {
                        viewModel.updateEndDate(
                            DateHelper.updateDate(
                                viewModel.endDate.value ?: DateHelper.getCurrentTime(), it
                            )
                        )
                    }
                    datePicker.show(sfm, "Tag")
                }
            }

            btnEndTime.setOnClickListener{
                activity?.supportFragmentManager?.let { sfm ->
                    val timePicker = MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setTitleText("Pilih jam selesai")
                        .build()
                    timePicker.addOnPositiveButtonClickListener {
                        val newHour: Int = timePicker.hour
                        val newMinute: Int = timePicker.minute
                        viewModel.updateEndDate(
                            DateHelper.updateTime(
                                viewModel.endDate.value ?: DateHelper.getCurrentTime(), newHour, newMinute
                            )
                        )
                    }
                    timePicker.show(sfm, "Tag")
                }
            }

            btnPreqResource.setOnClickListener{ showBottomSheet(TcAlterTaskViewModel.QUERY_RESOURCE) }
            btnPreqAssignment.setOnClickListener{ showBottomSheet(TcAlterTaskViewModel.QUERY_ASSIGNMENT) }
            btnQuestion.setOnClickListener { showBottomSheetForm() }

            viewModel.oldTaskForm.observe(viewLifecycleOwner, {
                tvFormType.text = ("Formulir Draf")
                edtTitle.setText(it.title)
                tvQuestionCount.text = ("jumlah soal ${it.questions?.size}")
                tvResourceCount.text = ("terpilih ${it.prerequisiteResources?.size}")
                tvAssignmentCount.text = ("terpilih ${it.prerequisiteTaskForms?.size}")
                when (it.type) {
                    Constant.TASK_TYPE_MID_EXAM -> toggleButton.check(R.id.btn_mid_exam)
                    Constant.TASK_TYPE_FINAL_EXAM -> toggleButton.check(R.id.btn_final_exam)
                    Constant.TASK_TYPE_ASSIGNMENT -> toggleButton.check(R.id.btn_assignment)
                }
            })

            viewModel.startDate.observe(viewLifecycleOwner, {
                btnStartDate.text = DateHelper.getFormattedDateTime(DateHelper.DMY, it)
                btnStartTime.text = DateHelper.getFormattedDateTime(DateHelper.hm, it)
            })

            viewModel.endDate.observe(viewLifecycleOwner, {
                btnEndDate.text = DateHelper.getFormattedDateTime(DateHelper.DMY, it)
                btnEndTime.text = DateHelper.getFormattedDateTime(DateHelper.hm, it)
            })

            viewModel.draftSaved.observe(viewLifecycleOwner, {
                if (it.hasBeenHandled) view?.findNavController()?.popBackStack()
                it.getContentIfNotHandled()?.let { event ->
                    if (event.first) {
                        if (event.second) {
                            view?.findNavController()?.navigate(
                                TcAlterTaskFragmentDirections.actionTcAlterTaskFragmentToTcAlterTaskFinalizationFragment(
                                    viewModel.subjectGroup.subjectName,
                                    viewModel.subjectGroup.gradeLevel,
                                    viewModel.formType,
                                    viewModel.savedTaskFormId
                                )
                            )
                        } else {
                            Toast.makeText(context, "Draf berhasil disimpan", Toast.LENGTH_SHORT).show()
                            view?.findNavController()?.popBackStack()
                        }
                    }
                }

                btnSaveDraf.isEnabled = true
                btnFinalize.isEnabled = true
            })

            btnSaveDraf.setOnClickListener {
                if (validateInput(false)) {
                    viewModel.submitForm(edtTitle.text.toString(), false)
                    btnSaveDraf.isEnabled = false
                    btnFinalize.isEnabled = false
                }
            }

            btnFinalize.setOnClickListener {
                if (validateInput(true)) {
                    viewModel.submitForm(edtTitle.text.toString(), true)
                    btnSaveDraf.isEnabled = false
                    btnFinalize.isEnabled = false
                }
            }
        }

        binding.imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }

        retrieveArgs()

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveArgs() {
        val args: TcAlterTaskFragmentArgs by navArgs()
        with(binding) {
            tvSubjectGroup.text = ("${args.subjectName} - ${args.gradeLevel}")
            if (args.formType == TcAlterTaskViewModel.TYPE_ASSIGNMENT) {
                btnMidExam.visibility = View.GONE
                btnFinalExam.visibility = View.GONE
                toggleButton.check(R.id.btn_assignment)
                viewModel.taskType = Constant.TASK_TYPE_ASSIGNMENT
            } else {
                btnAssignment.visibility = View.GONE
                toggleButton.check(R.id.btn_mid_exam)
                viewModel.taskType = Constant.TASK_TYPE_MID_EXAM
                binding.llPreqResource.visibility = View.GONE
                binding.dvPreqResource.visibility = View.GONE
                binding.llPreqAssignment.visibility = View.GONE
                binding.dvPreqAssignment.visibility = View.GONE
            }
        }
        viewModel.initData(args.subjectName, args.gradeLevel, args.formType, args.taskFormId)
    }

    @SuppressLint("InflateParams")
    private fun showBottomSheet(queryType : Int) {
        val dialog = BottomSheetDialog(context!!)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_tc_alter_task_general, null)

        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        val btnConfirm = view.findViewById<Button>(R.id.btn_confirm)
        val btnCancel = view.findViewById<Button>(R.id.btn_cancel)

        val rvItem = view.findViewById<RecyclerView>(R.id.rv_item)
        rvItem.layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        rvItem.addItemDecoration(dividerItemDecoration)

        when(queryType) {
            TcAlterTaskViewModel.QUERY_RESOURCE -> {
                viewModel.resourceList.observe(viewLifecycleOwner, {
                    val adapter = ResourceViewHolder(it, viewModel.selectedResource)
                    tvTitle.text = ("Daftar Materi")
                    rvItem.adapter = adapter.getAdapter()
                    btnConfirm.setOnClickListener {
                        viewModel.selectedResource = adapter.getResult()
                        binding.tvResourceCount.text = ("terpilih ${adapter.getResult().size}")
                        dialog.dismiss()
                    }
                    viewModel.resourceList.removeObservers(viewLifecycleOwner)
                })
                viewModel.loadResource()
            }
            TcAlterTaskViewModel.QUERY_ASSIGNMENT -> {
                viewModel.assignmentList.observe(viewLifecycleOwner, {
                    val adapter = AssignmentViewHolder(it, viewModel.selectedAssignment)
                    tvTitle.text = ("Daftar Tugas")
                    rvItem.adapter = adapter.getAdapter()
                    btnConfirm.setOnClickListener {
                        viewModel.selectedAssignment = adapter.getResult()
                        binding.tvAssignmentCount.text = ("terpilih ${adapter.getResult().size}")
                        dialog.dismiss()
                    }
                    viewModel.assignmentList.removeObservers(viewLifecycleOwner)
                })
                viewModel.loadAssignment()
            }
        }

        btnCancel.setOnClickListener { dialog.dismiss() }

        dialog.setContentView(view)
        dialog.show()
    }

    @SuppressLint("InflateParams")
    private fun showBottomSheetForm() {
        val dialog = BottomSheetDialog(context!!)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_tc_alter_task_form, null)

        val btnConfirm = view.findViewById<Button>(R.id.btn_confirm)
        val btnCancel = view.findViewById<Button>(R.id.btn_cancel)
        val btnAdd = view.findViewById<Button>(R.id.btn_add)

        val rvItem = view.findViewById<RecyclerView>(R.id.rv_item)
        rvItem.layoutManager = LinearLayoutManager(context)

        viewModel.questionList.observe(viewLifecycleOwner, {
            val adapter = TcFormAdapter(it)
            rvItem.adapter = adapter
            btnConfirm.setOnClickListener {
                var isOK = true
                var totalScore = 0
                adapter.questions.mapIndexed { index, question ->
                    val childView = rvItem.getChildAt(index)
                    when (question.type) {
                        Constant.TASK_FORM_MC -> validateQuestionMC(childView)
                        Constant.TASK_FORM_ESSAY -> validateQuestionEssay(childView)
                        else -> null
                    }.let { newQuestion ->
                        if (newQuestion == null) {
                            isOK = false
                            return@mapIndexed
                        } else {
                            totalScore += childView.findViewById<EditText>(R.id.edt_score_weight).text.toString().toInt()
                            adapter.questions[index] = newQuestion
                        }
                    }
                }
                if (isOK && totalScore != 100) {
                    Toast.makeText(context, "Bobot total harus 100, total saat ini $totalScore", Toast.LENGTH_SHORT).show()
                    isOK = false
                }

                if (isOK) {
                    viewModel.updateQuestions(adapter.questions)
                    binding.tvQuestionCount.text = ("jumlah soal ${adapter.questions.size}")
                    dialog.dismiss()
                }
            }

            btnAdd.setOnClickListener { showChoiceDialog(adapter) }
            viewModel.questionList.removeObservers(viewLifecycleOwner)
        })

        btnCancel.setOnClickListener { dialog.dismiss() }

        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { bottomSheet ->
                val behaviour = BottomSheetBehavior.from(bottomSheet)
                val layoutParams = bottomSheet.layoutParams
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                bottomSheet.layoutParams = layoutParams
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        dialog.show()

    }

    @SuppressLint("InflateParams")
    private fun showChoiceDialog(adapter : TcFormAdapter) {
        val dialog = Dialog(context!!)
        val view = layoutInflater.inflate(R.layout.dialog_tc_form_type, null)

        val btnMc = view.findViewById<Button>(R.id.btn_mc)
        val btnEssay = view.findViewById<Button>(R.id.btn_essay)

        btnMc.setOnClickListener {
            adapter.addQuestion(Constant.TASK_FORM_MC)
            dialog.dismiss()
        }

        btnEssay.setOnClickListener {
            adapter.addQuestion(Constant.TASK_FORM_ESSAY)
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.setCancelable(true)

        dialog.show()
    }

    private fun validateInput(continueToFinalization: Boolean) : Boolean {
        with(binding) {
            if (isStringEmpty(context!!, edtTitle.text.toString(), "Judul")) return false
        }

        if (continueToFinalization) {
            if (viewModel.startDate.value!! > viewModel.endDate.value!!) {
                Toast.makeText(context, "Waktu mulai harus mendahului waktu selesai",Toast.LENGTH_SHORT).show()
                return false
            }

            if (viewModel.startDate.value!! < DateHelper.getCurrentTime()) {
                Toast.makeText(context, "Waktu sekarang harus mendahului waktu selesai",Toast.LENGTH_SHORT).show()
                return false
            }

            if (DateHelper.getMinute(viewModel.startDate.value, viewModel.endDate.value) < 5) {
                Toast.makeText(context, "Durasi minimal 5 menit",Toast.LENGTH_SHORT).show()
                return false
            }

            if ((viewModel.questionList.value?.size ?: 0) == 0) {
                Toast.makeText(context, "Jumlah soal harus minimal 1",Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    private fun validateQuestionEssay(view : View) : Question?{
        val title = view.findViewById<EditText>(R.id.edt_title).text.toString()
        if (isStringEmpty(context!!, title, "Judul soal")) return null

        val scoreWeight = view.findViewById<EditText>(R.id.edt_score_weight).text.toString()
        if (isStringEmpty(context!!, scoreWeight, "Bobot soal")) return null
        if (isStringInteger(context!!, scoreWeight, "Bobot soal")) return null
        val scoreWeightInt = scoreWeight.toInt()

        return Question(
            title,
            Constant.TASK_FORM_ESSAY,
            scoreWeightInt,
            emptyList(),
            ""
        )
    }

    private fun validateQuestionMC(view : View) : Question?{
        val title = view.findViewById<EditText>(R.id.edt_title).text.toString()
        if (isStringEmpty(context!!, title, "Judul soal")) return null

        val scoreWeight = view.findViewById<EditText>(R.id.edt_score_weight).text.toString()
        if (isStringEmpty(context!!, scoreWeight, "Bobot soal")) return null
        if (isStringInteger(context!!, scoreWeight, "Bobot soal")) return null
        val scoreWeightInt = scoreWeight.toInt()

        val answerKey = view.findViewById<EditText>(R.id.edt_answer_key).text.toString()
        if (isStringEmpty(context!!, answerKey, "Jawaban")) return null
        if (isStringInteger(context!!, answerKey, "Jawaban")) return null
        val answerKeyInt = answerKey.toInt()
        if (answerKeyInt < 1 || answerKeyInt > 5) {
            Toast.makeText(context, "Jawaban harus di antara 1 - 5", Toast.LENGTH_SHORT).show()
            return null
        }

        val choice1 = view.findViewById<EditText>(R.id.edt_choice_1).text.toString()
        if (isStringEmpty(context!!, choice1, "Pilihan 1")) return null

        val choice2 = view.findViewById<EditText>(R.id.edt_choice_2).text.toString()
        if (isStringEmpty(context!!, choice2, "Pilihan 2")) return null

        val choice3 = view.findViewById<EditText>(R.id.edt_choice_3).text.toString()
        if (isStringEmpty(context!!, choice3, "Pilihan 3")) return null

        val choice4 = view.findViewById<EditText>(R.id.edt_choice_4).text.toString()
        if (isStringEmpty(context!!, choice4, "Pilihan 4")) return null

        val choice5 = view.findViewById<EditText>(R.id.edt_choice_5).text.toString()
        if (isStringEmpty(context!!, choice5, "Pilihan 5")) return null

        return Question(
            title,
            Constant.TASK_FORM_MC,
            scoreWeightInt,
            listOf(choice1, choice2, choice3, choice4, choice5),
            answerKey
        )
    }
}