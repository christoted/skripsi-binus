package com.example.project_skripsi.module.student.task.form

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.*
import com.example.project_skripsi.core.model.local.AssignedQuestion
import com.example.project_skripsi.databinding.ItemStTaskFormEssayBinding
import com.example.project_skripsi.databinding.ItemStTaskFormMcBinding
import com.example.project_skripsi.utils.Constant
import com.example.project_skripsi.utils.Constant.Companion.TASK_FORM_ESSAY
import com.example.project_skripsi.utils.Constant.Companion.TASK_FORM_MC

class StFormAdapter(val questionList: List<AssignedQuestion>) :
    Adapter<ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            0 -> MultipleChoiceViewHolder(ItemStTaskFormMcBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false))
            else -> EssayViewHolder(ItemStTaskFormEssayBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false))
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (questionList[position].type) {
            TASK_FORM_MC ->
                (holder as MultipleChoiceViewHolder).bind(questionList[position], position)
            TASK_FORM_ESSAY ->
                (holder as EssayViewHolder).bind(questionList[position], position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return StTaskFormViewModel.TASK_FORM_TYPE[questionList[position].type]!!
    }

    override fun getItemCount() = questionList.size

    class MultipleChoiceViewHolder ( private val binding : ItemStTaskFormMcBinding) : ViewHolder(binding.root) {
        fun bind(item: AssignedQuestion, position: Int) {
            with(binding) {
                tvNumber.text = ("${position+1}.")
                choice1.text = item.choices?.getOrNull(0)
                choice2.text = item.choices?.getOrNull(1)
                choice3.text = item.choices?.getOrNull(2)
                choice4.text = item.choices?.getOrNull(3)
                choice5.text = item.choices?.getOrNull(4)
                tvTitle.text = item.title
                item.answer?.answerText?.let {
                    when (it.toInt()) {
                        1 -> choice1.isChecked = true
                        2 -> choice2.isChecked = true
                        3 -> choice3.isChecked = true
                        4 -> choice4.isChecked = true
                        5 -> choice5.isChecked = true
                    }
                }
                tvScoreWeight.text = ("Bobot : ${item.scoreWeight}")
            }
        }
    }

    class EssayViewHolder ( private val binding : ItemStTaskFormEssayBinding) : ViewHolder(binding.root) {
        fun bind(item: AssignedQuestion, position: Int) {
            with(binding) {
                tvNumber.text = ("${position+1}.")
                tvTitle.text = item.title
                item.answer?.answerText?.let { edtAnswer.setText(it) }
                tvScoreWeight.text = ("Bobot : ${item.scoreWeight}")
            }
        }
    }
}
