package com.example.project_skripsi.module.student.main.progress.graphic

import android.view.LayoutInflater
import com.example.project_skripsi.databinding.ItemStSubjectFilterBinding
import com.example.project_skripsi.utils.generic.GenericAdapter
import com.example.project_skripsi.utils.generic.ItemClickListener

class StSubjectFilterViewHolder(
    private val dataSet: List<String>,
    private val listener: ItemClickListener
) {

    fun getAdapter(): GenericAdapter<String> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemStSubjectFilterBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item, viewBinding, _ ->
            val view = viewBinding as ItemStSubjectFilterBinding
            with(view) {
                tvSubjectName.text = item
                llParent.setOnClickListener { listener.onItemClick(item) }
            }
        }
        return adapter
    }


}