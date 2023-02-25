package com.example.project_skripsi.core.model.local

data class TaskFormTimer(
    val forceSubmit: Boolean,
    val hour: Long,
    val minute: Long,
    val second: Long
)