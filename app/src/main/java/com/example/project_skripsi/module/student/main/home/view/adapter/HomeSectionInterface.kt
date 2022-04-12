package com.example.project_skripsi.module.student.main.home.view.adapter

interface ItemListener {
    fun onTaskFormItemClicked(taskFormId: String, subjectName: String)
    fun onClassItemClicked(Position: Int)
    fun onMaterialItemClicked(Position: Int)
}