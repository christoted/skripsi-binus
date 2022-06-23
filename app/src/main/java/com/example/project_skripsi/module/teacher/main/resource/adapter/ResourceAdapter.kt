package com.example.project_skripsi.module.teacher.main.resource.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.core.model.firestore.Resource
import com.example.project_skripsi.databinding.ItemTcResourceBinding
import com.example.project_skripsi.module.teacher.main.resource.TcResourceFragmentDirections

class ResourceAdapter(private val listResource: List<Resource>): RecyclerView.Adapter<ResourceAdapter.ResourceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
       val itemBinding = ItemTcResourceBinding.inflate(LayoutInflater.from(parent.context), parent, false )
        return ResourceViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        if (listResource.isNotEmpty()) {
            val item = listResource[position]
            holder.bind(item)
        }
    }

    override fun getItemCount(): Int {
       return listResource.size
    }

    inner class ResourceViewHolder(private val binding: ItemTcResourceBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(resource: Resource) {
            with(binding) {
                tvName.text = resource.title
                tvMeetingNumber.text = ("Pert. ${resource.meetingNumber}")
                root.setOnClickListener {
                    val action = TcResourceFragmentDirections.actionTcResourceFragmentToTcAlterResourceFragment(resource.subjectName ?: "", resource.gradeLevel ?: 0, resource.id)
                    it.findNavController().navigate(action)
                }
            }
        }
    }
}