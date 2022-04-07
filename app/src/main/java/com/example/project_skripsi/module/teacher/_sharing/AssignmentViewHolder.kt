package com.example.project_skripsi.module.teacher._sharing

import com.example.project_skripsi.utils.generic.GenericAdapter
import android.view.LayoutInflater
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.databinding.StandardCheckboxBinding

class AssignmentViewHolder(private val dataSet : List<TaskForm>, private val checked: List<TaskForm>) {

    var itemChecked = BooleanArray(dataSet.size)

    fun getAdapter(): GenericAdapter<TaskForm> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            StandardCheckboxBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item,viewBinding,holder->
            val view = viewBinding as StandardCheckboxBinding
            with(view.itemCheckbox) {
                text = item.title
                isChecked = checked.contains(item)
                setOnCheckedChangeListener { _, b ->
                    val pos = holder.absoluteAdapterPosition
                    itemChecked[pos] = b
                }
            }
        }
        return adapter
    }

    fun getResult() : List<TaskForm> {
        val result = mutableListOf<TaskForm>()
        itemChecked.mapIndexed{ i,b -> if (b) result.add(dataSet[i]) }
        return result
    }

}