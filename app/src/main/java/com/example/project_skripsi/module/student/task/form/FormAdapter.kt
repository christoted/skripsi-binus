package com.example.project_skripsi.module.student.task.form

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.*
import com.example.project_skripsi.databinding.ItemStTaskFormEssayBinding
import com.example.project_skripsi.databinding.ItemStTaskFormMcBinding

class FormAdapter(private val questionList: List<Int>) :
    Adapter<ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            0 -> MultipleChoiceViewHolder(ItemStTaskFormMcBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false))
            else -> EssayViewHolder(ItemStTaskFormEssayBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false))
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (questionList[position]) {
            0 -> (holder as MultipleChoiceViewHolder).bind(position)
            else -> (holder as EssayViewHolder).bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return questionList[position]
    }

    override fun getItemCount() = questionList.size

    class MultipleChoiceViewHolder ( private val binding : ItemStTaskFormMcBinding) : ViewHolder(binding.root) {
        fun bind(question: Int) {
            with(binding) {
                tvNumber.text = ("${question+1}.")
//                this.textviewSubjectName.text = subjectName
            }
        }
    }

    class EssayViewHolder ( private val binding : ItemStTaskFormEssayBinding) : ViewHolder(binding.root) {
        fun bind(question: Int) {
            with(binding) {
                tvNumber.text = ("${question+1}.")
//                this.textviewSubjectName.text = subjectName
            }
        }
    }
}
