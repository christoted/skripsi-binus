package com.example.project_skripsi.module.student.main.studyclass.student_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.bumptech.glide.Glide
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.Announcement
import com.example.project_skripsi.core.model.firestore.Payment
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.model.local.HomeSectionData
import com.example.project_skripsi.databinding.*
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.generic.GenericAdapter
import com.example.project_skripsi.utils.generic.ItemClickListener
import com.example.project_skripsi.utils.helper.CurrencyHelper
import com.example.project_skripsi.utils.helper.DateHelper

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