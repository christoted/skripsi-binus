package com.example.project_skripsi.module.parent.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.model.firestore.Announcement
import com.example.project_skripsi.core.model.firestore.Parent
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.model.local.HomeMainSection
import com.example.project_skripsi.core.model.local.ParentAgendaMeeting
import com.example.project_skripsi.core.model.local.ParentAgendaPayment
import com.example.project_skripsi.core.model.local.ParentAgendaTaskForm
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.Constant
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.helper.DateHelper.Companion.convertDateToCalendarDay
import com.example.project_skripsi.utils.helper.DateHelper.Companion.getCurrentDate
import kotlin.math.min

class PrHomeViewModel : ViewModel() {

    companion object {
        private const val contentPerPage = 3
    }

    private val _studentList = MutableLiveData<List<Student>>()
    val studentList: LiveData<List<Student>> = _studentList


    private val _sectionData = MutableLiveData<List<HomeMainSection>>()
    val sectionData: LiveData<List<HomeMainSection>> = _sectionData

    private val _listHomeSectionDataClassMeeting = MutableLiveData<List<ParentAgendaMeeting>>()
    private val _listHomeSectionDataExam = MutableLiveData<List<ParentAgendaTaskForm>>()
    private val _listHomeSectionDataAssignment = MutableLiveData<List<ParentAgendaTaskForm>>()
    private val _listPaymentSectionDataPayment = MutableLiveData<List<ParentAgendaPayment>>()
    private val _listPaymentSectionDataAnnouncement = MutableLiveData<List<Announcement>>()

    init {

        val listData = arrayListOf<HomeMainSection>()
        listData.add(HomeMainSection(Constant.SECTION_MEETING, sectionItem = emptyList()))
        listData.add(HomeMainSection(Constant.SECTION_EXAM, sectionItem = emptyList()))
        listData.add(HomeMainSection(Constant.SECTION_ASSIGNMENT, sectionItem = emptyList()))
        listData.add(HomeMainSection(Constant.SECTION_PAYMENT, sectionItem = emptyList()))
        listData.add(HomeMainSection(Constant.SECTION_ANNOUNCEMENT, sectionItem = emptyList()))

        _listHomeSectionDataClassMeeting.observeOnce {
            listData[0] = HomeMainSection(Constant.SECTION_MEETING, sectionItem = it)
            _sectionData.postValue(listData)
        }

        _listHomeSectionDataExam.observeOnce {
            listData[1] = (HomeMainSection(Constant.SECTION_EXAM, sectionItem = it))
            _sectionData.postValue(listData)
        }

        _listHomeSectionDataAssignment.observeOnce {
            listData[2] = (HomeMainSection(Constant.SECTION_ASSIGNMENT, sectionItem = it))
            _sectionData.postValue(listData)
        }

        _listPaymentSectionDataPayment.observeOnce {
            listData[3] =  HomeMainSection(Constant.SECTION_PAYMENT, sectionItem = it)
            _sectionData.postValue(listData)
        }

        _listPaymentSectionDataAnnouncement.observeOnce {
            listData[4] = HomeMainSection(Constant.SECTION_ANNOUNCEMENT, sectionItem = it)
            _sectionData.postValue(listData)
        }

        loadParent(AuthRepository.inst.getCurrentUser().uid)
        loadAnnouncements()
    }



    private fun loadParent(uid: String) {
        FireRepository.inst.getItem<Parent>(uid).first.observeOnce { parent ->
            parent.children?.let { loadStudent(it) }
        }
    }

    private fun loadStudent(uids: List<String>) {
        FireRepository.inst.getItems<Student>(uids).first.observeOnce { list ->
            _studentList.postValue(list.sortedByDescending { it.age })

            val meetingList = mutableListOf<ParentAgendaMeeting>()
            val examList = mutableListOf<ParentAgendaTaskForm>()
            val assignmentList = mutableListOf<ParentAgendaTaskForm>()
            val paymentList = mutableListOf<ParentAgendaPayment>()

            list.map { student ->
                paymentList.addAll(
                    student.payments?.map { ParentAgendaPayment(student.name, it) } ?: emptyList()
                )
                meetingList.addAll(
                    student.attendedMeetings?.map { ParentAgendaMeeting(student.name, it) } ?: emptyList()
                )
                examList.addAll(
                    student.assignedExams?.map { ParentAgendaTaskForm(student.name, it) } ?: emptyList()
                )
                assignmentList.addAll(
                    student.assignedAssignments?.map { ParentAgendaTaskForm(student.name, it) } ?: emptyList()
                )
            }
            _listHomeSectionDataClassMeeting.postValue(
                meetingList
                    .filter { convertDateToCalendarDay(it.attendedMeeting?.startTime) == getCurrentDate() }
                    .sortedBy { it.attendedMeeting?.startTime }
            )
            _listHomeSectionDataExam.postValue(
                examList
                    .filter { convertDateToCalendarDay(it.assignedTaskForm?.startTime) == getCurrentDate() }
                    .sortedBy { it.assignedTaskForm?.startTime }
            )
            _listHomeSectionDataAssignment.postValue(
                assignmentList
                    .filter { convertDateToCalendarDay(it.assignedTaskForm?.startTime) == getCurrentDate() }
                    .sortedBy { it.assignedTaskForm?.startTime }
            )
            _listPaymentSectionDataPayment.postValue(
                paymentList
                    .filter { convertDateToCalendarDay(it.payment?.paymentDeadline) == getCurrentDate() }
                    .sortedBy { it.payment?.paymentDeadline }
            )
        }
    }

    private fun loadAnnouncements() {
        FireRepository.inst.getAllItems<Announcement>().first.observeOnce { list ->
            _listPaymentSectionDataAnnouncement.postValue(
                list.filter { convertDateToCalendarDay(it.date) == getCurrentDate() }
                    .sortedBy { it.date }
            )
        }
    }

    fun getStudents(page: Int): List<Student> {
        val startIdx = page * contentPerPage
        val endIdx = min(startIdx + contentPerPage, studentList.value?.size?:0)
        return studentList.value?.subList(startIdx, endIdx)?: emptyList()
    }

    fun getStudentPageCount() : Int{
        val subjects = studentList.value?.size ?: 0
        return (subjects + contentPerPage - 1) / contentPerPage
    }

}