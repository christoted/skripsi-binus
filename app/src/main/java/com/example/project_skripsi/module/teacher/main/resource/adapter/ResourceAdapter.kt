package com.example.project_skripsi.module.teacher.main.resource.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.core.model.firestore.Resource
import com.example.project_skripsi.databinding.ItemTcResourceBinding
import com.example.project_skripsi.module.teacher.main.resource.viewmodel.TcResourceViewModel
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class ResourceAdapter(private val viewModel: TcResourceViewModel): RecyclerView.Adapter<ResourceAdapter.ResourceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
       val itemBinding = ItemTcResourceBinding.inflate(LayoutInflater.from(parent.context), parent, false )
        return ResourceViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        viewModel.resources.value?.let {
            val item = it[position]
            holder.bind(item)
        }
    }

    override fun getItemCount(): Int {
       return viewModel.resources.value?.size ?: 0
    }

    inner class ResourceViewHolder(private val binding: ItemTcResourceBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(resource: Resource) {
            with(binding) {
                tvResourceTitle.text = resource.title
            }
        }
    }
}