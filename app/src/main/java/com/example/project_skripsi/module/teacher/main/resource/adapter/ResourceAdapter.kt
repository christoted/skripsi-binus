package com.example.project_skripsi.module.teacher.main.resource.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.core.model.firestore.Resource
import com.example.project_skripsi.databinding.ItemTcResourceBinding
import com.example.project_skripsi.module.teacher.main.resource.TcResourceFragmentDirections
import com.example.project_skripsi.module.teacher.main.resource.viewmodel.TcResourceViewModel
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
                tvResourceTitle.text = resource.title
                root.setOnClickListener {
                    val action = TcResourceFragmentDirections.actionTcResourceFragmentToTcAlterResourceFragment(resource.subjectName ?: "", resource.gradeLevel ?: 0, resource.id)
                    it.findNavController().navigate(action)
                }
            }
        }
    }
}