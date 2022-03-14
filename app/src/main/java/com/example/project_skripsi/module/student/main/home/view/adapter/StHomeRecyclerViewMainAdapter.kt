package com.example.project_skripsi.module.student.main.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.databinding.StItemHomeMainSectionBinding
import com.example.project_skripsi.module.student.main.home.viewmodel.HomeMainSection
import com.example.project_skripsi.module.student.main.home.viewmodel.StHomeViewModel
import java.util.ArrayList


class StHomeRecyclerViewMainAdapter(val viewModel: StHomeViewModel): RecyclerView.Adapter<StHomeRecyclerViewMainAdapter.StHomeMainSectionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StHomeMainSectionViewHolder {
       val itemHomeMainSection = StItemHomeMainSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StHomeMainSectionViewHolder(itemHomeMainSection)
    }

    override fun onBindViewHolder(holder: StHomeMainSectionViewHolder, position: Int) {
        viewModel.sectionDatas.value?.let {
            val singleItemMainSection = it[position]
            holder.bind(singleItemMainSection)
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

            }
        }

        fun bind(singleHomeMainSectionItem: HomeMainSection) {
            with(binding) {
                sectionTitle.text = singleHomeMainSectionItem.sectionName
            }
        }
    }
}