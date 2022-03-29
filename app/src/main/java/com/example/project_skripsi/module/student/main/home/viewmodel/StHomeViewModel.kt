package com.example.project_skripsi.module.student.main.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.model.local.TaskFormStatus
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

    private val _sectionData = MutableLiveData<List<HomeMainSection>>()
    val sectionData: LiveData<List<HomeMainSection>> = _sectionData

    private val _listHomeSectionDataClassSchedule = MutableLiveData<List<Subject>>()
    private val _listHomeSectionDataExam = MutableLiveData<List<TaskForm>>()
    private val _listHomeSectionDataAssignment = MutableLiveData<List<TaskForm>>()
    private val _listPaymentSectionDataPayment = MutableLiveData<List<Payment>>()
    private val _listPaymentSectionDataAnnouncement = MutableLiveData<List<Announcement>>()

    private var listHomeSectionDataExam = arrayListOf<TaskForm>()
    private var listHomeSectionDataAssignment = arrayListOf<TaskForm>()


    init {
        _profileName.value = "Luis Anthonie Alkins (21)"
        _profileClass.value = "XII - IPA - 1"
        loadCurrentStudent(AuthRepository.instance.getCurrentUser().uid)
        loadAnnouncements()
        initData()
    }

    private fun initData() {
        val listData = arrayListOf<HomeMainSection>()
        listData.add(HomeMainSection("Jadwal Kelas", sectionItem = emptyList()))
        listData.add(HomeMainSection("Ujian", sectionItem = emptyList()))
        listData.add(HomeMainSection("Tugas", sectionItem = emptyList()))
        listData.add(HomeMainSection("Pembayaran", sectionItem = emptyList()))
        listData.add(HomeMainSection("Pengumuman", sectionItem = emptyList()))

        _listHomeSectionDataClassSchedule.observeOnce {
            listData[0] = HomeMainSection("Jadwal Kelas", sectionItem = it)
            _sectionData.postValue(listData)
        }

        _listHomeSectionDataExam.observeOnce {
            listData[1] = (HomeMainSection("Ujian", sectionItem = it))
            _sectionData.postValue(listData)
        }

        _listHomeSectionDataAssignment.observeOnce {
            listData[2] = (HomeMainSection("Tugas", sectionItem = it))
            _sectionData.postValue(listData)
        }

        _listPaymentSectionDataPayment.observeOnce {
            listData[3] =  HomeMainSection("Pembayaran", sectionItem = it)
            _sectionData.postValue(listData)
        }

        _listPaymentSectionDataAnnouncement.observeOnce {
            listData[4] = HomeMainSection("Pengumuman", sectionItem = it)
            _sectionData.postValue(listData)
        }

        _sectionData.postValue(listData)
    }

    private fun loadCurrentStudent(uid: String) {
        FireRepository.instance.getStudent(uid).let {
            response ->
            response.first.observeOnce {
                student ->
                Log.d("Data Student", "${student}")
                // TODO: Take the Class id
                student.studyClass?.let { loadStudyClass(it) }
                // TODO: Load the Payment
                student.payments?.let { _listPaymentSectionDataPayment.postValue(it) }
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
                        // TODO: Take the class exams
                        subject.classExams?.let { exams ->
                            loadTaskForms(exams, _listHomeSectionDataExam)
                        }
                        // TODO: Take the class assignments
                        subject.classAssignments?.let { assignments ->
                            loadTaskForms(assignments, _listHomeSectionDataAssignment)
                        }
                    }
                }
            }
        }
    }

    private fun loadAnnouncements() {
        FireRepository.instance.getAnnouncements().let {
            response ->
            response.first.observeOnce {
                // TODO: Load Announcement
                _listPaymentSectionDataAnnouncement.postValue(it)
            }
        }
    }

    private fun loadTaskForms(uids: List<String>, _taskFormList: MutableLiveData<List<TaskForm>>) {
        val taskFormList = ArrayList<TaskForm>()
        uids.map { uid ->
            FireRepository.instance.getTaskForm(uid).let { response ->
                response.first.observeForever { taskForm ->
                    taskFormList.add(taskForm)
                    if (taskFormList.size == uids.size)
                        _taskFormList.postValue(taskFormList.toList())
                }
            }
        }
    }


}