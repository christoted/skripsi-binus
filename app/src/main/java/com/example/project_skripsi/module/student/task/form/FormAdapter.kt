package com.example.project_skripsi.module.student.task.form

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.example.project_skripsi.databinding.ItemStClassSubjectBinding
import com.example.project_skripsi.databinding.ItemStTaskFormEssayBinding
import com.example.project_skripsi.databinding.ItemStTaskFormMcBinding

class FormAdapter(private val questionList: List<Int>) :
    Adapter<ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        MultipleChoiceViewHolder(ItemStTaskFormMcBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false))
//        when (viewType) {
//            0 -> MultipleChoiceViewHolder(ItemStTaskFormMcBinding.inflate(
//                LayoutInflater.from(viewGroup.context), viewGroup, false))
//            else -> EssayViewHolder(ItemStTaskFormEssayBinding.inflate(
//                LayoutInflater.from(viewGroup.context), viewGroup, false))
//        }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as MultipleChoiceViewHolder).bind(questionList[position])
//        when (questionList[position]) {
//            0 -> (holder as MultipleChoiceViewHolder).bind(questionList[position])
//            else -> (holder as EssayViewHolder).bind(questionList[position])
//        }
    }

    override fun getItemCount() = questionList.size

    class MultipleChoiceViewHolder ( private val binding : ItemStTaskFormMcBinding) : ViewHolder(binding.root) {
        fun bind(question: Int) {
            with(binding) {
//                this.textviewSubjectName.text = subjectName
            }


        }
    }

    class EssayViewHolder ( private val binding : ItemStTaskFormEssayBinding) : ViewHolder(binding.root) {
        fun bind(question: Int) {
            with(binding) {
//                this.textviewSubjectName.text = subjectName
            }

        }
    }
}
