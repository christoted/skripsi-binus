package com.example.project_skripsi.utils.helper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.project_skripsi.databinding.ViewEmptyItemBinding
import com.example.project_skripsi.databinding.ViewEmptyListBinding

class UIHelper {

    companion object {

        fun getEmptyList(message: String, inflater: LayoutInflater, parent: ViewGroup) : View {
            val emptyView = ViewEmptyListBinding.inflate(inflater, parent, false)
            emptyView.tvEmpty.text = message
            return emptyView.root
        }

        fun getEmptyItem(message: String, inflater: LayoutInflater, parent: ViewGroup) : View {
            val emptyView = ViewEmptyItemBinding.inflate(inflater, parent, false)
            emptyView.tvEmpty.text = message
            return emptyView.root
        }

    }

}