package com.example.project_skripsi.module.teacher.study_class.teaching

import android.graphics.drawable.Drawable
import com.example.project_skripsi.utils.generic.GenericAdapter
import android.view.LayoutInflater
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.databinding.ItemTcStudyClassStudentBinding
import com.example.project_skripsi.utils.app.App


class TeachingStudentViewHolder(
    private val viewModel : TcStudyClassTeachingViewModel,
    private val dataSet : List<Student>
) {

    fun getAdapter(): GenericAdapter<Student> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemTcStudyClassStudentBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item, viewBinding->
            val view = viewBinding as ItemTcStudyClassStudentBinding
            with(view) {
                tvAbsentNumber.text = "1."
                tvName.text = item.name
                tvAbsent.text = viewModel.getAttendanceAbsent(item).toString()

                tvStatusTitle.text = ("Status tugas terakhir")
                val status : Pair<String, Int> = viewModel.getLastAssignmentStatus(item)
                tvStatus.text = status.first

                var drawable: Drawable? = tvStatusIndicator.background
                drawable = DrawableCompat.wrap(drawable!!)
                DrawableCompat.setTint(drawable, ResourcesCompat.getColor(App.resourses!!, status.second, null))
                tvStatusIndicator.background = drawable

            }
        }
        return adapter
    }

}