package com.example.project_skripsi.utils.generic

import com.example.project_skripsi.core.model.firestore.Resource

interface LinkClickListener {
    fun onResourceItemClicked(resource: Resource)
}