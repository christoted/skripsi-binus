package com.example.project_skripsi.module.student.main.home.view.adapter

interface ItemListener {
    fun onExamItemClicked(Position : Int)
    fun onAssignmentItemClicked(Position: Int)
    fun onClassItemClicked(Position: Int)
    fun onMaterialItemClicked(Position: Int)
}