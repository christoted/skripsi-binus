package com.example.project_skripsi.module.parent.student_detail.viewholder

import android.view.LayoutInflater
import androidx.core.content.res.ResourcesCompat
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.Payment
import com.example.project_skripsi.core.model.firestore.Subject
import com.example.project_skripsi.core.model.local.ParentSubject
import com.example.project_skripsi.databinding.ItemPrSubjectBinding
import com.example.project_skripsi.databinding.ItemStPaymentVariantBinding
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.generic.GenericAdapter
import com.example.project_skripsi.utils.helper.CurrencyHelper
import com.example.project_skripsi.utils.helper.DateHelper

class PrSubjectViewHolder(private val dataSet : List<ParentSubject>) {


    fun getAdapter(): GenericAdapter<ParentSubject> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemPrSubjectBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item,viewBinding,_ ->
            val view = viewBinding as ItemPrSubjectBinding
            with(view) {
                view.tvSubjectName.text = item.subjectName
                view.tvTeacherName.text = item.teacherName
                item.teacherPhoneNumber?.let { imvTeacherPhone.setImageResource(R.drawable.whatsapp) }

                view.tvSummaryAttendance.text = ("${item.attendance} / ${item.meetingTotal}")
                view.tvSummaryExam.text = ("${item.exam} / ${item.examTotal}")
                view.tvSummaryAssignment.text = ("${item.assignment} / ${item.assignmentTotal}")
            }
        }
        return adapter
    }

}