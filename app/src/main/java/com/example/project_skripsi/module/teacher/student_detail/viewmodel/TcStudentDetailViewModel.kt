package com.example.project_skripsi.module.teacher.student_detail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.Payment
import com.example.project_skripsi.core.model.local.TcStudentDetailPaymentSection
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.helper.DateHelper

class TcStudentDetailViewModel: ViewModel() {

    private val _listPaymentSection: MutableLiveData<List<TcStudentDetailPaymentSection>> = MutableLiveData()
    val listPaymentSection: LiveData<List<TcStudentDetailPaymentSection>> = _listPaymentSection
    companion object {
        const val tabCount = 3
        val tabHeader = arrayOf("Nilai", "Absensi", "Pembayaran")
    }
    var studentUID = ""
    // Get Payments
    fun getPayments() {
        val paymentSection: MutableList<TcStudentDetailPaymentSection> = mutableListOf()
        val payments: MutableList<Payment> = mutableListOf()
        val _payments: MutableLiveData<List<Payment>> = MutableLiveData()
        FireRepository.instance.getStudent(studentUID).first.observeOnce { student ->
            student.payments?.let {

                payments.addAll(it)
            }
            _payments.postValue(payments)
        }
        paymentSection.add(TcStudentDetailPaymentSection(title = "Jatuh Tempo", payments = emptyList()))
        paymentSection.add(TcStudentDetailPaymentSection(title = "Mendatang", payments = emptyList()))
        _payments.observeOnce {
            Log.d("987 ", "getPayments: palign bawah" + it)
            paymentSection[0].payments = it.filter {
                it.paymentDeadline!! < DateHelper.getCurrentDate()
            }
            paymentSection[1].payments = it.filter {
                it.paymentDeadline!! > DateHelper.getCurrentDate()
            }
            _listPaymentSection.postValue(paymentSection)
        }
    }
    // Get Attendances
    private fun getAttendances() {

    }
}