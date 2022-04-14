package com.example.project_skripsi.module.student.task.form

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.*
import com.example.project_skripsi.core.model.firestore.Question
import com.example.project_skripsi.core.model.local.AssignedQuestion
import com.example.project_skripsi.databinding.ItemStTaskFormEssayBinding
import com.example.project_skripsi.databinding.ItemStTaskFormMcBinding
import com.example.project_skripsi.utils.Constant

class StFormAdapter(private val questionList: List<AssignedQuestion>) :
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
            Constant.TASK_FORM_MC ->
                (holder as MultipleChoiceViewHolder).bind(questionList[position], position)
            Constant.TASK_FORM_ESSAY ->
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
                choice1.text = item.choices?.get(0) ?: "null"
                choice2.text = item.choices?.get(1) ?: "null"
                choice3.text = item.choices?.get(2) ?: "null"
                choice4.text = item.choices?.get(3) ?: "null"
                choice5.text = item.choices?.get(4) ?: "null"
                tvTitle.text = item.title
                item.answer?.let {
                    when (it.toString().toInt()) {
                        0 -> choice1.isChecked = true
                        1 -> choice2.isChecked = true
                        2 -> choice3.isChecked = true
                        3 -> choice4.isChecked = true
                        4 -> choice5.isChecked = true
                    }
                }

            }
        }
    }

    class EssayViewHolder ( private val binding : ItemStTaskFormEssayBinding) : ViewHolder(binding.root) {
        fun bind(item: AssignedQuestion, position: Int) {
            with(binding) {
                tvNumber.text = ("${position+1}.")
                tvTitle.text = item.title
                item.answer?.let { edtAnswer.setText(it.toString()) }
            }
        }
    }
}
