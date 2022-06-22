package com.example.project_skripsi.module.common.zoom

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.Constant
import com.example.project_skripsi.utils.helper.DateHelper
import com.example.project_skripsi.utils.service.storage.StorageSP
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.max
import kotlin.math.min

class MyMeetingViewModel : ViewModel()  {

    companion object {
//        const val readinessDelay = 2 * 60
//        const val meetingTime = 40

        const val interval = 12

        const val readinessDelay = 15 // test
        const val meetingTime = 3 // test
    }

    private val _timeLeft = MutableLiveData<Int>()
    val timeLeft : LiveData<Int> = _timeLeft

    var startTime : Date = DateHelper.getCurrentTime()

    init {
        if (MeetingHandler.inst.isStudent) generateReadinessDialog(3)
    }

//    fun dummyInit(context: Context) {
//        StorageSP.setInt(
//            context,
//            getReadinessId(MeetingHandler.inst.studentEntity?.id, MeetingHandler.inst.meetingId),
//            0
//        )
//
//        StorageSP.setInt(
//            context,
//            getTimeId(MeetingHandler.inst.studentEntity?.id, MeetingHandler.inst.meetingId),
//            0
//        )
//    }

    private fun generateReadinessDialog(counter: Int) {
        if (counter == 0) return
        viewModelScope.launch {
            val triggerAt = 6 + (0..6).random()
//            delay(triggerAt * 60 * 1000L)
            delay(triggerAt * 60 * 50L) // test

            startTimerCountdown()

            val sleep = interval - triggerAt
//            delay(sleep * 60 * 1000L)
            delay(sleep * 60 * 50L) // test
            generateReadinessDialog(counter-1)
        }
    }

    private fun startTimerCountdown() {
        viewModelScope.launch {
            var localTime = readinessDelay
            while (localTime >= 0) {
                _timeLeft.postValue(localTime)
                localTime -= 1
                delay(1000)
            }
        }
    }

    // format readiness
    // studentID-meetID-readiness
    // studentID-meetID-time

    fun addReadinessCheck(context: Context) {
        val id = getReadinessId(MeetingHandler.inst.studentEntity?.id, MeetingHandler.inst.meetingId)
        StorageSP.setInt(context, id, min(3, StorageSP.getInt(context, id, 0) + 1))
    }

    fun recordTime(context: Context) {
        val minute = DateHelper.getMinute(startTime, DateHelper.getCurrentTime()).toInt()

        val id = getTimeId(MeetingHandler.inst.studentEntity?.id, MeetingHandler.inst.meetingId)
        StorageSP.setInt(context, id, StorageSP.getInt(context, id, 0) + minute)
    }

    fun getVerdict(context: Context) {
        val readinessCheck = StorageSP.getInt(
            context,
            getReadinessId(MeetingHandler.inst.studentEntity?.id, MeetingHandler.inst.meetingId),
            0
        )

        val time = StorageSP.getInt(
            context,
            getTimeId(MeetingHandler.inst.studentEntity?.id, MeetingHandler.inst.meetingId),
            0
        )

        // verdict result
        // 3 checks with 60% time 24 menit
        // 2 checks with 75% time 30 menit
        // 1 checks with 100% time 40 menit
        val verdict = ((readinessCheck == 3 && verdictTimePassed(time, 60)) ||
                (readinessCheck == 2 && verdictTimePassed(time, 75))  ||
                (readinessCheck == 1 && verdictTimePassed(time, 100)))

//        Toast.makeText(context, "Verdict :$verdict",Toast.LENGTH_SHORT).show()
        MeetingHandler.inst.studentEntity?.let { student ->
            student.attendedMeetings?.firstOrNull { it.id == MeetingHandler.inst.meetingId }
                ?.status = if (verdict) Constant.ATTENDANCE_ATTEND else Constant.ATTENDANCE_ALPHA
            FireRepository.inst.alterItems(listOf(student))
        }
        MeetingHandler.inst.endMeeting()
    }

    private fun verdictTimePassed(time: Int, percent: Int) = time >= meetingTime * percent / 100

    private fun getReadinessId(studentId: String?, meetId: String?) = "${studentId}-${meetId}-readiness"

    private fun getTimeId(studentId: String?, meetId: String?) = "${studentId}-${meetId}-time"
}