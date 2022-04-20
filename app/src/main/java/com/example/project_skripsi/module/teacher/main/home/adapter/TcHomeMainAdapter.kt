package com.example.project_skripsi.module.teacher.main.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.core.model.local.HomeMainSection
import com.example.project_skripsi.databinding.ItemStHomeMainSectionBinding
import com.example.project_skripsi.databinding.ItemTcHomeMainSectionBinding
import com.example.project_skripsi.module.student.main.home.view.adapter.ItemListener
import com.example.project_skripsi.module.student.main.home.view.adapter.StHomeRecyclerViewChildAdapter
import com.example.project_skripsi.module.student.main.home.view.adapter.StHomeRecyclerViewMainAdapter
import com.example.project_skripsi.module.teacher._sharing.agenda.TcAgendaItemListener

class TcHomeMainAdapter(val listHomeSectionData: List<HomeMainSection>, val listener: TcAgendaItemListener):
    RecyclerView.Adapter<TcHomeMainAdapter.TcHomeMainSectionViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TcHomeMainSectionViewHolder {
        return  TcHomeMainSectionViewHolder(
            ItemTcHomeMainSectionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TcHomeMainSectionViewHolder, position: Int) {
        listHomeSectionData.let {
            val singleItemMainSection = it[position]
            // Declare the child adapter
            val childAdapter = TcHomeChildAdapter(singleItemMainSection, listener)
            holder.bind(singleItemMainSection, childAdapter)
        }
    }

    override fun getItemCount(): Int {
        return listHomeSectionData.size ?: 0
    }


    inner class TcHomeMainSectionViewHolder(private val binding: ItemTcHomeMainSectionBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(singleHomeMainSectionItem: HomeMainSection, adapter: TcHomeChildAdapter) {
            with(binding) {
                sectionTitle.text = singleHomeMainSectionItem.sectionName
                sectionTitleCount.text = singleHomeMainSectionItem.sectionItem.count().toString()
                with(binding.sectionItemsRecyclerView) {
                    sectionItemsRecyclerView.layoutManager = LinearLayoutManager(context)
                    sectionItemsRecyclerView.adapter = adapter
                }
            }
        }
    }
}