package com.example.project_skripsi.module.student.main.home.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.databinding.StItemHomeMainSectionBinding
import com.example.project_skripsi.module.student.main.home.viewmodel.HomeMainSection
import com.example.project_skripsi.module.student.main.home.viewmodel.StHomeViewModel
import java.util.ArrayList

class StHomeRecyclerViewMainAdapter(val viewModel: StHomeViewModel, val listener: ItemListener): RecyclerView.Adapter<StHomeRecyclerViewMainAdapter.StHomeMainSectionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StHomeMainSectionViewHolder {
       val itemHomeMainSection = StItemHomeMainSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StHomeMainSectionViewHolder(itemHomeMainSection)
    }

    override fun onBindViewHolder(holder: StHomeMainSectionViewHolder, position: Int) {
        viewModel.sectionDatas.value?.let {
            val singleItemMainSection = it[position]
            // Declare the child adapter
            val childAdapter = StHomeRecyclerViewChildAdapter(singleItemMainSection, listener)
            holder.bind(singleItemMainSection, childAdapter)
        }
    }

    override fun getItemCount(): Int {

       viewModel.sectionDatas.value?.let {
         return it.size
       } ?: run {
           return 0
       }
    }


    inner class StHomeMainSectionViewHolder(private val binding: StItemHomeMainSectionBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                Log.d("Test", absoluteAdapterPosition.toString())
                with(binding) {
//                    sectionItemsRecyclerView.isVisible = !sectionItemsRecyclerView.isVisible
                }
            }
        }

        fun bind(singleHomeMainSectionItem: HomeMainSection, adapter: StHomeRecyclerViewChildAdapter) {
            with(binding) {
                sectionTitle.text = singleHomeMainSectionItem.sectionName
                with(binding.sectionItemsRecyclerView) {
                    sectionItemsRecyclerView.layoutManager = LinearLayoutManager(context)
                    sectionItemsRecyclerView.adapter = adapter
                }
            }
        }
    }
}