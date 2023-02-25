package com.example.project_skripsi.module.common.zoom

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.project_skripsi.R
import com.example.project_skripsi.utils.service.alarm.AlarmService
import us.zoom.sdk.MeetingActivity

class MyMeetingActivity : MeetingActivity() {

    private lateinit var viewModel: MyMeetingViewModel

    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[MyMeetingViewModel::class.java]

        AlarmService.inst.cancelAlarm(applicationContext, MeetingHandler.inst.meetingId, true)

        viewModel.timeLeft.observe(this) {
            if (it == MyMeetingViewModel.readinessDelay) {
                dialog = AlertDialog.Builder(this)
                    .setView(R.layout.dialog_readiness)
                    .setPositiveButton("Ya") { dialog, _ ->
                        viewModel.addReadinessCheck(applicationContext)
                        Toast.makeText(applicationContext, "Absensi direkam", Toast.LENGTH_SHORT)
                            .show()
                        dialog.dismiss()
                    }
                    .setCancelable(false)
                    .show()
            }
            dialog?.let { dlg ->
                dlg.findViewById<TextView>(R.id.tv_timer)?.text = ("${it}s")
                if (it == 0) dlg.dismiss()
            }
        }

//        viewModel.dummyInit(applicationContext)
//        Toast.makeText(applicationContext, "MeetingActivity onCreate", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.recordTime(applicationContext)
        viewModel.getVerdict(applicationContext)
//        Toast.makeText(applicationContext, "MeetingActivity onDestroy", Toast.LENGTH_SHORT).show()
    }
}