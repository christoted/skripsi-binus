package com.example.project_skripsi.module.teacher.form.assessment

import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.*
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.local.AssignedQuestion
import com.example.project_skripsi.databinding.DialogStViewImageBinding
import com.example.project_skripsi.databinding.ItemTcAssessmentTaskFormEssayBinding
import com.example.project_skripsi.databinding.ItemTcAssessmentTaskFormMcBinding
import com.example.project_skripsi.module.common.view_image.ViewImageViewHolder
import com.example.project_skripsi.utils.Constant
import com.example.project_skripsi.utils.app.App

class TcAssessmentFormAdapter(
    val questionList: List<AssignedQuestion>,
    val allowAssessment: Boolean
    ) :
    Adapter<ViewHolder>() {

    private val imageList: List<MutableList<String>> = List(questionList.size) { mutableListOf() }

    init {
        questionList.mapIndexed { index, assignedQuestion ->
            assignedQuestion.answer?.images?.let { imageList[index].addAll(it) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            0 -> MultipleChoiceViewHolder(
                ItemTcAssessmentTaskFormMcBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                )
            )
            else -> EssayViewHolder(
                ItemTcAssessmentTaskFormEssayBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                )
            )
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (questionList[position].type) {
            Constant.TASK_FORM_MC ->
                (holder as MultipleChoiceViewHolder).bind(questionList[position], position)
            Constant.TASK_FORM_ESSAY ->
                (holder as EssayViewHolder).bind(questionList[position], position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return TcAssessmentTaskFormViewModel.TASK_FORM_TYPE[questionList[position].type]!!
    }

    override fun getItemCount() = questionList.size

    inner class MultipleChoiceViewHolder(private val binding: ItemTcAssessmentTaskFormMcBinding) :
        ViewHolder(binding.root) {
        fun bind(item: AssignedQuestion, position: Int) {
            with(binding) {
                tvNumber.text = ("${position + 1}.")
                tvChoice1.text = item.choices?.getOrNull(0)
                tvChoice2.text = item.choices?.getOrNull(1)
                tvChoice3.text = item.choices?.getOrNull(2)
                tvChoice4.text = item.choices?.getOrNull(3)
                tvChoice5.text = item.choices?.getOrNull(4)

                when (item.answerKey) {
                    "1" -> imvChoice1
                    "2" -> imvChoice2
                    "3" -> imvChoice3
                    "4" -> imvChoice4
                    "5" -> imvChoice5
                    else -> null
                }?.setImageResource(R.drawable.ic_indicator_answer_key)

                tvTitle.text = item.title

                when (item.answer?.answerText) {
                    "1" -> llChoice1
                    "2" -> llChoice2
                    "3" -> llChoice3
                    "4" -> llChoice4
                    "5" -> llChoice5
                    else -> null
                }?.setBackgroundColor(
                    ResourcesCompat.getColor(
                        App.resourses!!,
                        if (item.answerKey == item.answer?.answerText) R.color.answer_correct
                        else R.color.answer_incorrect,
                        null
                    )
                )

                tvScoreWeight.text = ("Nilai (0/${item.scoreWeight}): ")
                tvScore.text =
                    if (item.answerKey == item.answer?.answerText) item.scoreWeight.toString() else "0"

                item.answer?.images?.takeIf { it.isNotEmpty() }?.let { list ->
                    tvImageCount.text = list.size.toString()
                    btnViewImage.setOnClickListener { showImagesDialog(position, root.context) }
                } ?: kotlin.run {
                    llImages.visibility = GONE
                }

                if (!allowAssessment) {
                    dvScore.visibility = View.GONE
                    llScore.visibility = View.GONE
                }
            }
        }
    }

    inner class EssayViewHolder(private val binding: ItemTcAssessmentTaskFormEssayBinding) :
        ViewHolder(binding.root) {
        fun bind(item: AssignedQuestion, position: Int) {
            with(binding) {
                tvNumber.text = ("${position + 1}.")
                tvTitle.text = item.title
                item.answer?.answerText?.let {
                    if (it.isEmpty()) {
                        tvAnswer.text = ("jawaban tidak diisi")
                        tvAnswer.setTypeface(null, Typeface.ITALIC)
                        tvAnswer.setTextColor(
                            ResourcesCompat.getColor(
                                App.resourses!!,
                                R.color.empty_answer,
                                null
                            )
                        )
                    } else {
                        tvAnswer.text = it
                    }
                }
                tvScoreWeight.text = ("Nilai (0 - ${item.scoreWeight}): ")
                item.answer?.score.let { edtScore.setText(it.toString()) }
                btnSetZero.text = ("SET 0")
                btnSetZero.setOnClickListener { edtScore.setText("0") }
                btnSetMax.text = ("SET ${item.scoreWeight}")
                btnSetMax.setOnClickListener { edtScore.setText(item.scoreWeight.toString()) }

                item.answer?.images?.takeIf { it.isNotEmpty() }?.let { list ->
                    tvImageCount.text = list.size.toString()
                    btnViewImage.setOnClickListener { showImagesDialog(position, root.context) }
                } ?: kotlin.run {
                    llImages.visibility = GONE
                }

                if (!allowAssessment) {
                    dvScore.visibility = View.GONE
                    llScore.visibility = View.GONE
                }
            }
        }
    }

    private fun showImagesDialog(questionNumber: Int, context: Context) {
        val dialog = Dialog(context)

        val inflater = LayoutInflater.from(context)
        val sBinding = DialogStViewImageBinding.inflate(inflater)

        with(sBinding) {

            tvTitle.text = ("Foto Soal - ${questionNumber + 1}")

            rvContainer.layoutManager = LinearLayoutManager(context)
            rvContainer.adapter = ViewImageViewHolder(imageList[questionNumber]).getAdapter()

            btnClose.setOnClickListener { dialog.dismiss() }

            dialog.setContentView(root)
        }

        dialog.show()
    }
}
