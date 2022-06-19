package com.example.project_skripsi.module.teacher.study_class.resource

import com.example.project_skripsi.utils.generic.GenericAdapter
import android.view.LayoutInflater

import android.widget.Toast
import com.example.project_skripsi.core.model.firestore.Resource
import com.example.project_skripsi.databinding.ItemStSubjectResourceBinding
import com.example.project_skripsi.utils.generic.LinkClickListener


class ResourceViewHolder(
    private val dataSet : List<Resource>,
    private val listener : LinkClickListener
) {

    fun getAdapter(): GenericAdapter<Resource> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemStSubjectResourceBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item, viewBinding,_ ->
            val view = viewBinding as ItemStSubjectResourceBinding
            with(view) {
                tvName.text = item.title
                tvMeetingNumber.text = ("Pert. ${item.meetingNumber}")
                root.setOnClickListener {
                    if (item.link.isNullOrEmpty()) {
                        Toast.makeText(root.context, "Tidak ada link materi", Toast.LENGTH_SHORT).show()
                    } else {
                        listener.onResourceItemClicked(item)
                    }
                }
            }
        }
        return adapter
    }

}