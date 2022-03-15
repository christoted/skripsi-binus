package com.example.project_skripsi.module.student.main.payment

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StPaymentViewModel : ViewModel() {

    private val _upcomingPayment = MutableLiveData<List<String>>()
    val upcomingPayment : LiveData<List<String>> = _upcomingPayment

    private val _unpaidPayment = MutableLiveData<List<String>>()
    val unpaidPayment : LiveData<List<String>> = _unpaidPayment

    private val _paidPayment = MutableLiveData<List<String>>()
    val paidPayment : LiveData<List<String>> = _paidPayment

    companion object {
        const val tabCount = 3
        val tabHeader = arrayOf("Mendatang", "Jatuh Tempo", "Terbayar")
    }

    init {
        Handler(Looper.getMainLooper()).postDelayed({
            _upcomingPayment.value = listOf("Uang sekolah", "Uang guru")
            _unpaidPayment.value = listOf("Uang berenang", "Uang main")
            _paidPayment.value = listOf("Uang libur", "Uang ortu")
        }, 1000)
    }


}