package com.example.project_skripsi.module.teacher.study_class.resource

import com.example.project_skripsi.utils.generic.GenericAdapter
import android.view.LayoutInflater
import androidx.core.content.res.ResourcesCompat
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.utils.app.App
import androidx.core.graphics.drawable.DrawableCompat

import android.graphics.drawable.Drawable
import com.example.project_skripsi.core.model.firestore.Resource
import com.example.project_skripsi.databinding.ItemStSubjectResourceBinding
import com.example.project_skripsi.databinding.ItemTcStudyClassStudentBinding


class ResourceViewHolder(
    private val dataSet : List<Resource>,
) {

    fun getAdapter(): GenericAdapter<Resource> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemStSubjectResourceBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item, viewBinding ->
            val view = viewBinding as ItemStSubjectResourceBinding
            with(view) {
                tvNumber.text = ("${dataSet.indexOf(item) + 1}.")
                tvName.text = item.title
                tvType.text = item.type
            }
        }
        return adapter
    }

}