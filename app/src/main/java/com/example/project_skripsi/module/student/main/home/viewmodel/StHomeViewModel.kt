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
    private val _listHomeSectionDataExamId = MutableLiveData<List<String>>()
    private val _listHomeSectionDataExam = MutableLiveData<List<TaskForm>>()
    private val _listHomeSectionDataAssignmentId = MutableLiveData<List<String>>()
    private val _listHomeSectionDataAssignment = MutableLiveData<List<TaskForm>>()
    private val _listPaymentSectionDataPayment = MutableLiveData<List<Payment>>()
    private val _listPaymentSectionDataAnnouncement = MutableLiveData<List<Announcement>>()

    private val listHomeSectionDataClassSchedule = arrayListOf<HomeSectionData>()
    private val listHomeSectionDataPayment = arrayListOf<HomeSectionData>()
    private val listHomeSectionDataAnnouncement = arrayListOf<HomeSectionData>()
    private var listHomeSectionDataExam = arrayListOf<TaskForm>()
    private var listHomeSectionDataAssignment = arrayListOf<TaskForm>()

    private var tempListHomeSectionDataAssignment = arrayListOf<TaskForm>()
    private var tempHomeSectionDataExam = arrayListOf<TaskForm>()

    init {
        _profileName.value = "Luis Anthonie Alkins (21)"
        _profileClass.value = "XII - IPA - 1"
        loadCurrentStudent(AuthRepository.instance.getCurrentUser().uid)
        loadAnnouncements()
        initData()
    }

    private fun initData() {
        val listDatas = arrayListOf<HomeMainSection>()
        listDatas.add(HomeMainSection("Jadwal Kelas", sectionItem = emptyList()))
        listDatas.add(HomeMainSection("Ujian", sectionItem = emptyList()))
        listDatas.add(HomeMainSection("Tugas", sectionItem = emptyList()))
        listDatas.add(HomeMainSection("Pembayaran", sectionItem = emptyList()))
        listDatas.add(HomeMainSection("Pengumuman", sectionItem = emptyList()))

        _listHomeSectionDataClassSchedule.observeOnce {
          //  listHomeSectionDataClassSchedule.addAll(it)
            listDatas[0] = HomeMainSection("Jadwal Kelas", sectionItem = it)
            _sectionDatas.postValue(listDatas)
        }

        _listPaymentSectionDataPayment.observeOnce {
        //    listHomeSectionDataPayment.addAll(it)
            listDatas[3] =  HomeMainSection("Pembayaran", sectionItem = it)
            _sectionDatas.postValue(listDatas)
        }

        _listPaymentSectionDataAnnouncement.observeOnce {
       //     listHomeSectionDataAnnouncement.addAll(it)
            listDatas[4] = HomeMainSection("Pengumuman", sectionItem = it)
            _sectionDatas.postValue(listDatas)
        }

     //   listHomeSectionDataAssignment = arrayListOf()
        _listHomeSectionDataAssignment.observeOnce {
       //     tempListHomeSectionDataAssignment.addAll(it)
            listDatas[2] = (HomeMainSection("Tugas", sectionItem = it))
            _sectionDatas.postValue(listDatas)
        }

     //   listHomeSectionDataExam = arrayListOf()
        _listHomeSectionDataExam.observeOnce {
      //      tempHomeSectionDataExam.addAll(it)
            listDatas[1] = (HomeMainSection("Ujian", sectionItem = it))
            _sectionDatas.postValue(listDatas)
        }



        /*

            listDatas.add(HomeMainSection("Jadwal Kelas", sectionItem = listHomeSectionDataClassSchedule))
        listDatas.add(HomeMainSection("Ujian", sectionItem = tempHomeSectionDataExam))
        listDatas.add(HomeMainSection("Tugas", sectionItem = tempListHomeSectionDataAssignment))
        listDatas.add(HomeMainSection("Pembayaran", sectionItem = listHomeSectionDataPayment))
        listDatas.add(HomeMainSection("Pengumuman", sectionItem = listHomeSectionDataAnnouncement))
         */

        _sectionDatas.postValue(listDatas)
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
                    it.map { subject ->
                        // TODO: Take the class assignments
                        subject.classExams?.map {
                            loadTaskForms(it)
                        }
                        // TODO: Take the class exams
                        subject.classAssignments?.map {
                            loadTaskForms(it)
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
                _listHomeSectionDataAssignment.postValue(listHomeSectionDataAssignment)
                _listHomeSectionDataExam.postValue(listHomeSectionDataExam)
            }
        }
    }

}