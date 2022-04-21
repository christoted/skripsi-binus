package com.example.project_skripsi.module.teacher._sharing

import com.example.project_skripsi.utils.generic.GenericAdapter
import android.view.LayoutInflater
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.databinding.ViewCheckboxBinding

class ClassViewHolder(private val dataSet : List<StudyClass>, private val checked: List<String>) {

    private var itemsChecked = BooleanArray(dataSet.size)

    fun getAdapter(): GenericAdapter<StudyClass> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ViewCheckboxBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item,viewBinding,holder->
            val view = viewBinding as ViewCheckboxBinding
            val itemChecked = checked.contains(item.id)
            with(view.itemCheckbox) {
                text = item.name
                isChecked = itemChecked
                setOnCheckedChangeListener { _, b ->
                    val pos = holder.absoluteAdapterPosition
                    itemsChecked[pos] = b
                }
            }
            itemsChecked[holder.absoluteAdapterPosition] = itemChecked
        }
        return adapter
    }

    fun getResult() : List<String> {
        val result = mutableListOf<String>()
        itemsChecked.mapIndexed{ i, b -> if (b) dataSet[i].id?.let { result.add(it) } }
        return result
    }

}