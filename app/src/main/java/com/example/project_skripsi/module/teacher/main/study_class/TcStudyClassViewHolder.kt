package com.example.project_skripsi.module.teacher.main.study_class

import com.example.project_skripsi.utils.generic.GenericAdapter
import android.view.LayoutInflater
import com.example.project_skripsi.core.model.firestore.Resource
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.databinding.ItemTcStudyClassBinding
import com.example.project_skripsi.databinding.ViewCheckboxBinding

class TcStudyClassViewHolder(
    private val dataSet : List<StudyClass>,
    private val subjectName: String,
    private val listener: ClassClickListener) {

    fun getAdapter(): GenericAdapter<StudyClass> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemTcStudyClassBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item, viewBinding, _ ->
            val view = viewBinding as ItemTcStudyClassBinding
            with(view) {
                tvClassName.text = item.name
                tvStudentCount.text = (item.students?.size ?: 0).toString()
                item.id?.let { classId ->
                    root.setOnClickListener {
                        listener.onItemClick(classId, subjectName)
                    }
                }
            }
        }
        return adapter
    }
}