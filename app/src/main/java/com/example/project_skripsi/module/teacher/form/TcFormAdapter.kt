package com.example.project_skripsi.module.teacher.form

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.*
import com.example.project_skripsi.core.model.firestore.Question
import com.example.project_skripsi.databinding.ItemTcAlterTaskFormEssayBinding
import com.example.project_skripsi.databinding.ItemTcAlterTaskFormMcBinding
import com.example.project_skripsi.utils.Constant

class TcFormAdapter(questionList: List<Question>) :
    Adapter<ViewHolder>() {

    val questions = mutableListOf<Question>()
    init { questions.addAll(questionList) }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            0 ->
                MultipleChoiceViewHolder(
                    ItemTcAlterTaskFormMcBinding.inflate(
                        LayoutInflater.from(viewGroup.context), viewGroup, false)
                )
            else ->
                EssayViewHolder(
                    ItemTcAlterTaskFormEssayBinding.inflate(
                        LayoutInflater.from(viewGroup.context), viewGroup, false)
                )
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (questions[position].type) {
            Constant.TASK_FORM_MC ->
                (holder as MultipleChoiceViewHolder).bind(questions[position], position)
            Constant.TASK_FORM_ESSAY ->
                (holder as EssayViewHolder).bind(questions[position], position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return TcAlterTaskViewModel.TASK_FORM_TYPE[questions[position].type]!!
    }

    override fun getItemCount() = questions.size

    @SuppressLint("NotifyDataSetChanged")
    fun addQuestion(questionType : String) {
        questions.add(Question(type = questionType))
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteQuestion(position: Int) {
        questions.removeAt(position)
        notifyDataSetChanged()
    }

    inner class MultipleChoiceViewHolder ( private val binding : ItemTcAlterTaskFormMcBinding) : ViewHolder(binding.root) {
        fun bind(item: Question, position: Int) {
            with(binding) {
                tvNumber.text = ("Soal ${position+1}")
                item.title?.let { edtTitle.setText(it) }
                edtScoreWeight.setText(item.scoreWeight?.toString()?:"0")

                item.choices?.getOrNull(0)?.let { edtChoice1.setText(it)}
                item.choices?.getOrNull(1)?.let { edtChoice2.setText(it)}
                item.choices?.getOrNull(2)?.let { edtChoice3.setText(it)}
                item.choices?.getOrNull(3)?.let { edtChoice4.setText(it)}
                item.choices?.getOrNull(4)?.let { edtChoice5.setText(it)}
                edtAnswerKey.setText(item.answerKey?.toString()?:"1")

                imvDelete.setOnClickListener{ deleteQuestion(position) }
            }
        }
    }

    inner class EssayViewHolder ( private val binding : ItemTcAlterTaskFormEssayBinding) : ViewHolder(binding.root) {
        fun bind(item: Question, position: Int) {
            with(binding) {
                tvNumber.text = ("Soal ${position+1}")
                item.title?.let { edtTitle.setText(it) }
                edtScoreWeight.setText(item.scoreWeight?.toString()?:"0")

                imvDelete.setOnClickListener{ deleteQuestion(position) }
            }
        }
    }
}
