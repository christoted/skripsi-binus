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
        val listHomeSectionDataClassSchedule = arrayListOf<HomeSectionData>()
        listHomeSectionDataClassSchedule.add(HomeItemJadwalKelas(className = "Biologi"))
        listHomeSectionDataClassSchedule.add(HomeItemJadwalKelas(className = "Bahasa Inggris"))

        val listHomeSectionDataExam = arrayListOf<HomeSectionData>()
        listHomeSectionDataExam.add(HomeItemUjian(examSubject = "Matematika"))

        val listHomeSectionDataAssignment = arrayListOf<HomeSectionData>()
        listHomeSectionDataAssignment.add(HomeItemTugas(assignmentSubject = "Tugas 1"))

        val listHomeSectionDataPembayaran = arrayListOf<HomeSectionData>()
        listHomeSectionDataPembayaran.add(HomeItemPembayaran(paymentName = "Biaya Sekolah"))
        listHomeSectionDataPembayaran.add(HomeItemPembayaran(paymentName = "Biaya Semester"))

        val listHomeSectionDataPengumuman = arrayListOf<HomeSectionData>()
        listHomeSectionDataPengumuman.add(HomeItemPengumuman(announcementName = "Libur"))
        listHomeSectionDataPengumuman.add(HomeItemPengumuman(announcementName = "Lebaran"))

        //////
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
        listDatas.add(HomeMainSection("Jadwal Kelas", sectionItem = listHomeSectionDataClassSchedule))
        listDatas.add(HomeMainSection("Ujian", sectionItem = listHomeSectionDataExam))
        listDatas.add(HomeMainSection("Tugas", sectionItem = listHomeSectionDataAssignment))
        listDatas.add(HomeMainSection("Pembayaran", sectionItem = listHomeSectionDataPembayaran))
        listDatas.add(HomeMainSection("Pengumuman", sectionItem = listHomeSectionDataPengumuman))

        _sectionDatas.value = listDatas
    }

}