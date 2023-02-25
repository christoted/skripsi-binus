package com.example.project_skripsi.module.student.main.home.view.adapter

import com.example.project_skripsi.core.model.firestore.ClassMeeting

interface ItemListener {
    fun onTaskFormItemClicked(taskFormId: String, subjectName: String)
    fun onClassItemClicked(classMeeting: ClassMeeting)
    fun onResourceItemClicked(resourceId: String)
}