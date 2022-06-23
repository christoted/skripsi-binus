package com.example.project_skripsi.module.parent.home.viewholder.student

import android.view.LayoutInflater
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.databinding.ItemPrHomeStudentBinding
import com.example.project_skripsi.utils.generic.GenericAdapter
import com.example.project_skripsi.utils.generic.ItemClickListener

class StudentViewHolder(
    private val dataSet: List<Student>,
    private val listener: ItemClickListener
) {

    fun getAdapter(): GenericAdapter<Student> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemPrHomeStudentBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item, viewBinding, _ ->
            val view = viewBinding as ItemPrHomeStudentBinding
            with(view) {
                tvStudentName.text = item.name
                ivStudentProfile.setOnClickListener { item.id?.let { id -> listener.onItemClick(id) } }
            }
        }
        return adapter
    }

}