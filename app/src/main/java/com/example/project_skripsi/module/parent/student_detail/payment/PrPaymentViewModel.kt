package com.example.project_skripsi.module.parent.student_detail.payment

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.Payment
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.helper.DateHelper

class PrPaymentViewModel : ViewModel() {

    private val _totalCharge = MutableLiveData<Int>()
    val totalCharge : LiveData<Int> = _totalCharge

    private val _totalPaid = MutableLiveData<Int>()
    val totalPaid : LiveData<Int> = _totalPaid

    private val _accountNumber = MutableLiveData<String>()
    val accountNumber : LiveData<String> = _accountNumber

    private val _upcomingPayment = MutableLiveData<List<Payment>>()
    val upcomingPayment : LiveData<List<Payment>> = _upcomingPayment

    private val _unpaidPayment = MutableLiveData<List<Payment>>()
    val unpaidPayment : LiveData<List<Payment>> = _unpaidPayment

    private val _paidPayment = MutableLiveData<List<Payment>>()
    val paidPayment : LiveData<List<Payment>> = _paidPayment

    companion object {
        const val tabCount = 3
        val tabHeader = arrayOf("Mendatang", "Jatuh Tempo", "Terbayar")
    }

    fun setStudent(studentId: String) {
        loadStudent(studentId)
    }

    private fun loadStudent(uid: String) {
        FireRepository.instance.getItem<Student>(uid).first.observeOnce { student ->
            var totalCharge = 0
            var totalPaid = 0
            val upcomingPayment = mutableListOf<Payment>()
            val unpaidPayment = mutableListOf<Payment>()
            val paidPayment = mutableListOf<Payment>()
            student.payments?.map { payment ->
                if (payment.paymentDate == null) {
                    if (payment.paymentDeadline!! > DateHelper.getCurrentDate()) upcomingPayment.add(payment)
                    else unpaidPayment.add(payment)
                    totalCharge += (payment.nominal ?: 0)
                } else {
                    paidPayment.add(payment)
                    totalPaid += (payment.nominal?:0)
                }
                _accountNumber.postValue(payment.accountNumber?: "null" )
            }
            _totalCharge.postValue(totalCharge)
            _totalPaid.postValue(totalPaid)
            _upcomingPayment.postValue(upcomingPayment)
            _unpaidPayment.postValue(unpaidPayment)
            _paidPayment.postValue(paidPayment)
        }
    }


}