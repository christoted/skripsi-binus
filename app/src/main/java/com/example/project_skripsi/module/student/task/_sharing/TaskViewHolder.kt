package com.example.project_skripsi.module.student.task._sharing

import com.example.project_skripsi.utils.generic.GenericAdapter
import android.view.LayoutInflater
import androidx.navigation.Navigation
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.ItemStTaskBinding

class TaskViewHolder(private val taskType : Int, private val dataSet : List<String>) {

    companion object{
        const val TYPE_EXAM = 1
        const val TYPE_ASSIGNMENT = 2
        val TASK_NAVIGATION = mapOf(
            TYPE_EXAM to R.id.action_stTaskExamFragment_to_stTaskFormFragment,
            TYPE_ASSIGNMENT to R.id.action_stTaskAssignmentFragment_to_stTaskFormFragment
        )
    }


    fun getAdapter(): GenericAdapter<String> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemStTaskBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item,viewBinding->
            val view = viewBinding as ItemStTaskBinding
            view.textClass.text = item
            view.root.setOnClickListener {
                Navigation
                    .findNavController(view.root)
                    .navigate(TASK_NAVIGATION[taskType]!!)
            }
        }
        return adapter
    }

}