package com.example.project_skripsi.module.student.main.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.databinding.StItemHomeMainSectionBinding
import com.example.project_skripsi.databinding.StItemHomeSectionItemBinding
import com.example.project_skripsi.module.student.main.home.viewmodel.HomeMainSection

class StHomeRecyclerViewChildAdapter(val item: HomeMainSection): RecyclerView.Adapter<StHomeRecyclerViewChildAdapter.StHomeRecyclerViewChildViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StHomeRecyclerViewChildViewHolder {
        val itemHomeSection = StItemHomeSectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StHomeRecyclerViewChildViewHolder(itemHomeSection)
    }

    override fun onBindViewHolder(holder: StHomeRecyclerViewChildViewHolder, position: Int) {
        val singleData = item.sectionItem[position]
        holder.bind(singleData)
    }

    override fun getItemCount(): Int {
       return item.sectionItem.size
    }

    inner class StHomeRecyclerViewChildViewHolder(private val binding: StItemHomeSectionItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(singleItem: String) {
            with(binding) {
                title.text = singleItem
                
            }
        }
    }


}