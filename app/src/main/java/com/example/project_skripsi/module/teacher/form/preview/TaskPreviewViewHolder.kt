package com.example.project_skripsi.module.teacher.form.preview

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.databinding.ItemTcPreviewTaskBinding
import com.example.project_skripsi.databinding.ItemTcStudyClassTaskDetailBinding
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.generic.GenericAdapter
import com.example.project_skripsi.utils.generic.ItemClickListener

class TaskPreviewViewHolder(
    private val dataSet: List<String>,
) {

    fun getAdapter(): GenericAdapter<String> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemTcPreviewTaskBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item, viewBinding, _ ->
            val view = viewBinding as ItemTcPreviewTaskBinding
            with(view) {
                tvName.text = item
            }
        }
        return adapter
    }

}