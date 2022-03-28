package com.example.project_skripsi.module.student.main.home.viewmodel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

// Dummy Class
data class HomeItemJadwalKelas(val className: String): HomeSectionData()
data class HomeItemUjian(val examSubject: String) : HomeSectionData()
data class HomeItemTugas(val assignmentSubject: String) : HomeSectionData()
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

    private val _listHomeSectionDataClassSchedule = MutableLiveData<List<Subject>>()
    private val listHomeSectionDataExam = arrayListOf<TaskForm>()
    private val listHomeSectionDataAssignment = arrayListOf<TaskForm>()
    private val _listPaymentSectionDataPayment = MutableLiveData<List<Payment>>()
    private val _listPaymentSectionDataAnnouncement = MutableLiveData<List<Announcement>>()

    private val listHomeSectionDataClassSchedule = arrayListOf<HomeSectionData>()
    private val listHomeSectionDataPayment = arrayListOf<HomeSectionData>()
    private val listHomeSectionDataAnnouncement = arrayListOf<HomeSectionData>()
    init {
        _profileName.value = "Luis Anthonie Alkins (21)"
        _profileClass.value = "XII - IPA - 1"
        loadCurrentStudent(AuthRepository.instance.getCurrentUser().uid)
        loadAnnouncements()

        Handler(Looper.getMainLooper()).postDelayed({
            initData()
        }, 1000)
    }

    private fun initData() {
        val listDatas = arrayListOf<HomeMainSection>()

        _listHomeSectionDataClassSchedule.observeOnce {
            listHomeSectionDataClassSchedule.addAll(it)
        }

        val listHomeSectionDataExam = arrayListOf<HomeSectionData>()
        listHomeSectionDataExam.add(HomeItemUjian(examSubject = "Matematika"))

        val listHomeSectionDataAssignment = arrayListOf<HomeSectionData>()
        listHomeSectionDataAssignment.add(HomeItemTugas(assignmentSubject = "Tugas 1"))


        _listPaymentSectionDataPayment.observeOnce {
            listHomeSectionDataPayment.addAll(it)
        }

        _listPaymentSectionDataAnnouncement.observeOnce {
            listHomeSectionDataAnnouncement.addAll(it)
        }

        listDatas.add(HomeMainSection("Jadwal Kelas", sectionItem = listHomeSectionDataClassSchedule))
        listDatas.add(HomeMainSection("Ujian", sectionItem = listHomeSectionDataExam))
        listDatas.add(HomeMainSection("Tugas", sectionItem = listHomeSectionDataAssignment))
        listDatas.add(HomeMainSection("Pembayaran", sectionItem = listHomeSectionDataPayment))
        listDatas.add(HomeMainSection("Pengumuman", sectionItem = listHomeSectionDataAnnouncement))

        _sectionDatas.value = listDatas
    }

    private fun loadCurrentStudent(uid: String) {
        FireRepository.instance.getStudent(uid).let {
            response ->
            response.first.observeOnce {
                student ->
                Log.d("Data Student", "${student}")
                student.studyClass?.let {
                    // TODO: Take the Class id
                    loadStudyClass(it)
                }
                // TODO: Load the Payment
                student.payments?.let {
                    _listPaymentSectionDataPayment.postValue(it)
                }
            }
        }
    }

    private fun loadStudyClass(uid: String) {

        FireRepository.instance.getStudyClass(uid).let {
            response ->
            response.first.observeOnce { studyClass ->

                studyClass.subjects?.let {
                    _listHomeSectionDataClassSchedule.postValue(it)
                    it.forEach { subject ->

                        // TODO: Take the class assignments
                        subject.classExams?.let {
                            it.forEach { uid ->
                                loadTaskForms(uid)
                            }
                        }
                        // TODO: Take the class exams
                        subject.classAssignments?.let {
                            it.forEach { uid ->
                                loadTaskForms(uid)
                            }
                        }

                    }
                }
            }
        }
    }

    private fun loadAnnouncements() {
        FireRepository.instance.getAnnouncement().let {
            response ->
            response.first.observeOnce {
                // TODO: Load Announcement
                _listPaymentSectionDataAnnouncement.postValue(it)
            }
        }
    }

    private fun loadTaskForms(uid: String) {
        FireRepository.instance.getTaskForm(uid).let {
            response ->
            response.first.observeOnce {
                it.type?.let { type ->
                    if (type == "tugas") {
                        listHomeSectionDataAssignment.add(it)
                    } else {
                        listHomeSectionDataExam.add(it)
                    }
                }
            }
        }
    }

}