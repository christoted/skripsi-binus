package com.example.project_skripsi.module.teacher._sharing

import com.example.project_skripsi.utils.generic.GenericAdapter
import android.view.LayoutInflater
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.databinding.StandardCheckboxBinding

class ClassViewHolder(private val dataSet : List<StudyClass>, private val checked: List<StudyClass>) {

    var itemChecked = BooleanArray(dataSet.size)

    fun getAdapter(): GenericAdapter<StudyClass> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            StandardCheckboxBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item,viewBinding,holder->
            val view = viewBinding as StandardCheckboxBinding
            with(view.itemCheckbox) {
                text = item.name
                isChecked = checked.contains(item)
                setOnCheckedChangeListener { _, b ->
                    val pos = holder.absoluteAdapterPosition
                    itemChecked[pos] = b
                }
            }
        }
        return adapter
    }

    fun getResult() : List<StudyClass> {
        val result = mutableListOf<StudyClass>()
        itemChecked.mapIndexed{ i,b -> if (b) result.add(dataSet[i]) }
        return result
    }

}