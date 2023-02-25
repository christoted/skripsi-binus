package com.example.project_skripsi.module.teacher.resource.view

import android.view.LayoutInflater
import com.example.project_skripsi.databinding.ViewButtonOutlineBinding
import com.example.project_skripsi.utils.generic.GenericAdapter
import com.example.project_skripsi.utils.generic.ItemClickListener

class TcMeetingNumberViewHolder(
    private val dataSet: List<Int>,
    private val listener: ItemClickListener
) {

    fun getAdapter(): GenericAdapter<Int> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ViewButtonOutlineBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item, viewBinding, _ ->
            val view = viewBinding as ViewButtonOutlineBinding
            with(view) {
                btnOutline.text = item.toString()
                root.setOnClickListener { listener.onItemClick(item.toString()) }
            }
        }
        return adapter
    }


}