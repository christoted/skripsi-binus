package com.example.project_skripsi.module.student.subject_detail.resource

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.core.model.firestore.Resource
import com.example.project_skripsi.databinding.ItemStSubjectResourceBinding
import com.example.project_skripsi.utils.generic.LinkClickListener

class StSubjectResourceAdapter(
    private val examList: List<Resource>,
    private val listener: LinkClickListener,
) : RecyclerView.Adapter<StSubjectResourceAdapter.ResourceViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ResourceViewHolder =
        ResourceViewHolder(
            ItemStSubjectResourceBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false
            )
        )

    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        holder.bind(examList[position])
    }

    override fun getItemCount() = examList.size

    inner class ResourceViewHolder(private val binding: ItemStSubjectResourceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Resource) {
            with(binding) {
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
    }
}