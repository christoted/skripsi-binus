package com.example.project_skripsi.module.teacher.form.assessment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView.*
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.local.AssignedQuestion
import com.example.project_skripsi.databinding.ItemTcAssessmentTaskFormEssayBinding
import com.example.project_skripsi.databinding.ItemTcAssessmentTaskFormMcBinding
import com.example.project_skripsi.utils.Constant
import com.example.project_skripsi.utils.app.App

class TcAssessmentFormAdapter(val questionList: List<AssignedQuestion>) :
    Adapter<ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            0 -> MultipleChoiceViewHolder(ItemTcAssessmentTaskFormMcBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false))
            else -> EssayViewHolder(ItemTcAssessmentTaskFormEssayBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false))
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

    class MultipleChoiceViewHolder ( private val binding : ItemTcAssessmentTaskFormMcBinding) : ViewHolder(binding.root) {
        fun bind(item: AssignedQuestion, position: Int) {
            with(binding) {
                tvNumber.text = ("${position+1}.")
                tvChoice1.text = item.choices?.getOrNull(0)
                tvChoice2.text = item.choices?.getOrNull(1)
                tvChoice3.text = item.choices?.getOrNull(2)
                tvChoice4.text = item.choices?.getOrNull(3)
                tvChoice5.text = item.choices?.getOrNull(4)

                when(item.answerKey){
                    "1" -> imvChoice1
                    "2" -> imvChoice2
                    "3" -> imvChoice3
                    "4" -> imvChoice4
                    else -> imvChoice5
                }.setImageResource(R.drawable.ic_indicator_answer_key)

                tvTitle.text = item.title

                when(item.answer?.text) {
                    "1" -> llChoice1
                    "2" -> llChoice2
                    "3" -> llChoice3
                    "4" -> llChoice4
                    else -> llChoice5
                }.setBackgroundColor(ResourcesCompat.getColor(App.resourses!!,
                    if (item.scoreWeight == item.answer?.score) R.color.answer_correct
                    else R.color.answer_incorrect,
                    null
                ))
                tvScore.text = if (item.scoreWeight == item.answer?.score) item.scoreWeight.toString() else "0"
            }
        }
    }

    class EssayViewHolder ( private val binding : ItemTcAssessmentTaskFormEssayBinding) : ViewHolder(binding.root) {
        fun bind(item: AssignedQuestion, position: Int) {
            with(binding) {
                tvNumber.text = ("${position+1}.")
                tvTitle.text = item.title
                item.answer?.text?.let { tvAnswer.text = it.toString() }
                tvScoreWeight.text = ("Nilai (0 - ${item.scoreWeight}): ")
                item.answer?.score.let { edtScore.setText(it.toString()) }
                btnSetZero.text = ("SET 0")
                btnSetZero.setOnClickListener { edtScore.setText("0") }
                btnSetMax.text = ("SET ${item.scoreWeight}")
                btnSetMax.setOnClickListener { edtScore.setText(item.scoreWeight.toString()) }
            }
        }
    }
}
