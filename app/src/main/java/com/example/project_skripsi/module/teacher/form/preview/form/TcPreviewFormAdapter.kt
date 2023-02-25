package com.example.project_skripsi.module.teacher.form.preview.form

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.project_skripsi.core.model.firestore.Question
import com.example.project_skripsi.databinding.ItemStTaskFormEssayBinding
import com.example.project_skripsi.databinding.ItemStTaskFormMcBinding
import com.example.project_skripsi.utils.Constant

class TcPreviewFormAdapter(private val questionList: List<Question>) :
    Adapter<ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            0 -> MultipleChoiceViewHolder(
                ItemStTaskFormMcBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                )
            )
            else -> EssayViewHolder(
                ItemStTaskFormEssayBinding.inflate(
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
        return TcPreviewTaskFormViewModel.TASK_FORM_TYPE[questionList[position].type]!!
    }

    override fun getItemCount() = questionList.size

    class MultipleChoiceViewHolder(private val binding: ItemStTaskFormMcBinding) :
        ViewHolder(binding.root) {
        fun bind(item: Question, position: Int) {
            with(binding) {
                tvNumber.text = ("${position + 1}.")
                choice1.text = item.choices?.get(0) ?: "null"
                choice2.text = item.choices?.get(1) ?: "null"
                choice3.text = item.choices?.get(2) ?: "null"
                choice4.text = item.choices?.get(3) ?: "null"
                choice5.text = item.choices?.get(4) ?: "null"
                tvTitle.text = item.title
                tvScoreWeight.text = ("Bobot : ${item.scoreWeight}")
            }
        }
    }

    class EssayViewHolder(private val binding: ItemStTaskFormEssayBinding) :
        ViewHolder(binding.root) {
        fun bind(item: Question, position: Int) {
            with(binding) {
                tvNumber.text = ("${position + 1}.")
                tvTitle.text = item.title
                tvScoreWeight.text = ("Bobot : ${item.scoreWeight}")
            }
        }
    }
}
