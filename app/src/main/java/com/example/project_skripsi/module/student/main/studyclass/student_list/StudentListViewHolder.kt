package com.example.project_skripsi.module.student.main.studyclass.student_list

import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.databinding.ItemStStudyClassStudentBinding
import com.example.project_skripsi.utils.generic.GenericAdapter

class StudentListViewHolder(private val dataSet : List<Student>) {

    fun getAdapter(): GenericAdapter<Student> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemStStudyClassStudentBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item,viewBinding,_ ->
            val view = viewBinding as ItemStStudyClassStudentBinding
            with(view) {
                tvName.text = item.name
                tvAttendanceNumber.text = item.attendanceNumber.toString()
                item.profile?.let { imageUrl ->
                    Glide.with(root.context)
                        .load(imageUrl)
                        .into(ivProfilePicture)
                }
            }
        }
        return adapter
    }

}