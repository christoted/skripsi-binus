package com.example.project_skripsi.module.student.main.home.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.databinding.StItemHomeMainSectionBinding
import com.example.project_skripsi.databinding.StItemHomeSectionItemBinding
import com.example.project_skripsi.databinding.StItemHomeSectionPembayaranBinding
import com.example.project_skripsi.databinding.StItemHomeSectionPengumumanBinding
import com.example.project_skripsi.module.student.main.home.viewmodel.HomeMainSection
import com.example.project_skripsi.module.student.main.score.view.adapter.StScoreContentAdapter
import com.example.project_skripsi.utils.Constant

class StHomeRecyclerViewChildAdapter(val item: HomeMainSection): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        when(item.sectionName) {
            Constant.SECTION_PEMBAYARAN -> {
                val itemPembayaranHomeSection = StItemHomeSectionPembayaranBinding.inflate(LayoutInflater.from(parent.context), parent, false )
                return StHomeRecyclerViewChildPembayaranViewHolder(itemPembayaranHomeSection)
            }

            Constant.SECTION_PENGUMUMAN -> {
                val itemPengumumanHomeSection = StItemHomeSectionPengumumanBinding.inflate(LayoutInflater.from(parent.context), parent, false )
                return StHomeRecyclerViewChildPengumumanViewHolder(itemPengumumanHomeSection)
            }
            else -> {
                // Jadwal Kelas, Ujian, Tugas
                val itemHomeSection = StItemHomeSectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return StHomeRecyclerViewChildViewHolder(itemHomeSection)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val singleData = item.sectionItem[position]
        when(item.sectionName) {
            Constant.SECTION_PEMBAYARAN -> {
                (holder as StHomeRecyclerViewChildAdapter.StHomeRecyclerViewChildPembayaranViewHolder).bind(singleData)
            }

            Constant.SECTION_PENGUMUMAN -> {
                (holder as StHomeRecyclerViewChildAdapter.StHomeRecyclerViewChildPengumumanViewHolder).bind(singleData)
            }

            else -> {
                (holder as StHomeRecyclerViewChildAdapter.StHomeRecyclerViewChildViewHolder).bind(singleData)
            }
        }


    }

    override fun getItemCount(): Int {
       return item.sectionItem.size
    }

    inner class StHomeRecyclerViewChildViewHolder(private val binding: StItemHomeSectionItemBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                Log.d("Test Child", absoluteAdapterPosition.toString())
            }
        }
        fun bind(singleItem: String) {
            with(binding) {
                title.text = singleItem
                when(item.sectionName) {
                    Constant.SECTION_JADWAL_KELAS -> {
                        btnMateri.setOnClickListener {
                            Log.d("JADWAL KELAS", "bind: Materi")
                        }
                        btnKelas.setOnClickListener {
                            Log.d("JADWAL KELAS", "bind: Kelas")
                        }
                    }

                    Constant.SECTION_UJIAN -> {
                        btnMateri.isVisible = false
                        btnKelas.text = "ujian"
                        btnKelas.setOnClickListener {
                            Log.d("Section Ujian", "bind: ujian")
                        }

                    }
                    Constant.SECTION_TUGAS -> {
                        btnMateri.isVisible = false
                        btnKelas.text = "tugas"
                        btnKelas.setOnClickListener {
                            Log.d("Section Tugas", "bind: tugas")
                        }

                    }
                }

            }
        }
    }

    inner class StHomeRecyclerViewChildPembayaranViewHolder(private val binding: StItemHomeSectionPembayaranBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                Log.d("Test Child", absoluteAdapterPosition.toString())
            }
        }
        fun bind(singleItem: String) {
            with(binding) {

            }
        }
    }

    inner class StHomeRecyclerViewChildPengumumanViewHolder(private val binding: StItemHomeSectionPengumumanBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                Log.d("Test Child", absoluteAdapterPosition.toString())
            }
        }
        fun bind(singleItem: String) {
            with(binding) {

            }
        }
    }

}