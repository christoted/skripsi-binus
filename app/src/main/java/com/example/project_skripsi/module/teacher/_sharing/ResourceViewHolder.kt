package com.example.project_skripsi.module.teacher._sharing

import com.example.project_skripsi.utils.generic.GenericAdapter
import android.view.LayoutInflater
import com.example.project_skripsi.core.model.firestore.Resource
import com.example.project_skripsi.databinding.StandardCheckboxBinding

class ResourceViewHolder(private val dataSet : List<Resource>, private val checked: List<Resource>) {

    var itemChecked = BooleanArray(dataSet.size)

    fun getAdapter(): GenericAdapter<Resource> {
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

    fun getResult() : List<Resource> {
        val result = mutableListOf<Resource>()
        itemChecked.mapIndexed{ i,b -> if (b) result.add(dataSet[i]) }
        return result
    }

}