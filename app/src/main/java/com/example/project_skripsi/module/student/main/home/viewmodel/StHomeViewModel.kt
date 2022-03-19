package com.example.project_skripsi.module.student.main.home.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// Dummy Class

open class HomeSectionData { }

data class HomeItemJadwalKelas(val className: String): HomeSectionData()
data class HomeItemUjian(val examSubject: String) : HomeSectionData()
data class HomeItemTugas(val assignmentSubject: String) : HomeSectionData()
data class HomeItemPembayaran(val paymentName: String) : HomeSectionData()
data class HomeItemPengumuman(val announcementName: String): HomeSectionData()


data class HomeMainSection(val sectionName: String, val sectionItem: List<HomeSectionData>)

// Use the abstract class

class StHomeViewModel : ViewModel() {

    private val _profileName = MutableLiveData<String>()
    val profileName : LiveData<String> = _profileName

    private val _profileClass = MutableLiveData<String>()
    val profileClass : LiveData<String> = _profileClass

    private val _sectionDatas = MutableLiveData<List<HomeMainSection>>()
    val sectionDatas: LiveData<List<HomeMainSection>> = _sectionDatas



//    private val _profilePicture = MutableLiveData<String>()
//    val profilePicture : LiveData<String> = _profilePicture

    init {
        _profileName.value = "Luis Anthonie Alkins (21)"
        _profileClass.value = "XII - IPA - 1"
        Handler(Looper.getMainLooper()).postDelayed({
            initData()
        }, 1000)
    }

    private fun initData() {
        val listHomeSectionData = arrayListOf<HomeSectionData>()

        listHomeSectionData.add(HomeItemJadwalKelas(className = "Biologi"))
        listHomeSectionData.add(HomeItemUjian(examSubject = "Fisika"))
        listHomeSectionData.add(HomeItemTugas(assignmentSubject = "B Indo"))
        listHomeSectionData.add(HomeItemPembayaran(paymentName = "Pembayaran bulan 1"))
        listHomeSectionData.add(HomeItemPembayaran(paymentName = "Pembayaran bulan 1"))

        val listExam = arrayListOf<String>()
        listExam.add("Biology")
        listExam.add("Matematika")
        listExam.add("Kimia")
        listExam.add("Olahraga")

        val listAssignment = arrayListOf<String>()
        listAssignment.add("Fisika")
        listAssignment.add("Geologi")

        val listPayment = arrayListOf<String>()
        listPayment.add("Rp2.00.000,00")
        listPayment.add("Rp2.500.000,00")

        val listPengumuman = arrayListOf<String>()
        listPengumuman.add("Sekolah tatap muka")
        listPengumuman.add("Sekolah tatap wajah")

        val listDatas = arrayListOf<HomeMainSection>()
        listDatas.add(HomeMainSection("Jadwal Kelas", sectionItem = listHomeSectionData))
//        listDatas.add(HomeMainSection("Ujian", sectionItem = listExam))
//        listDatas.add(HomeMainSection("Tugas", sectionItem = listAssignment))
//        listDatas.add(HomeMainSection("Pembayaran", sectionItem = listPayment))
//        listDatas.add(HomeMainSection("Pengumuman", sectionItem = listPengumuman))
        _sectionDatas.value = listDatas

    }

}