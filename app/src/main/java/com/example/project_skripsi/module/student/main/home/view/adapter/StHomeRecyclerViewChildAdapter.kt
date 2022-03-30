package com.example.project_skripsi.module.student.main.home.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.model.local.HomeMainSection
import com.example.project_skripsi.core.model.local.HomeSectionData
import com.example.project_skripsi.databinding.*
import com.example.project_skripsi.module.student.main.home.viewmodel.*
import com.example.project_skripsi.utils.Constant

class StHomeRecyclerViewChildAdapter(val item: HomeMainSection, val listener: ItemListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        when(item.sectionName) {
            Constant.SECTION_PEMBAYARAN -> {
                val itemPembayaranHomeSection = ItemStHomeSectionPembayaranBinding.inflate(LayoutInflater.from(parent.context), parent, false )
                return StHomeRecyclerViewChildPembayaranViewHolder(itemPembayaranHomeSection)
            }

            Constant.SECTION_PENGUMUMAN -> {
                val itemPengumumanHomeSection = ItemStHomeSectionPengumumanBinding.inflate(LayoutInflater.from(parent.context), parent, false )
                return StHomeRecyclerViewChildPengumumanViewHolder(itemPengumumanHomeSection)
            }

            Constant.SECTION_UJIAN -> {
                val itemUjianHomeSection = ItemStHomeSectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false )
                return StHomeRecyclerViewChildExamViewHolder(itemUjianHomeSection)
            }

            Constant.SECTION_TUGAS -> {
                val itemPengumumanHomeSection = ItemStHomeSectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false )
                return StHomeRecyclerViewChildAssignmentViewHolder(itemPengumumanHomeSection)
            }

            else -> {
                // Jadwal Kelas, Ujian, Tugas
                val itemHomeSection = ItemStHomeSectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

            Constant.SECTION_JADWAL_KELAS -> {
                (holder as StHomeRecyclerViewChildAdapter.StHomeRecyclerViewChildViewHolder).bind(singleData)
            }

            Constant.SECTION_UJIAN -> {
                (holder as StHomeRecyclerViewChildAdapter.StHomeRecyclerViewChildExamViewHolder).bind(singleData)
            }

            Constant.SECTION_TUGAS -> {
               (holder as StHomeRecyclerViewChildAdapter.StHomeRecyclerViewChildAssignmentViewHolder).bind(singleData)
            }
        }
    }

    override fun getItemCount(): Int {
       return item.sectionItem.size
    }

    inner class StHomeRecyclerViewChildViewHolder(private val binding: ItemStHomeSectionItemBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                Log.d("Test Child", absoluteAdapterPosition.toString())

            }
        }
        fun bind(singleItem: HomeSectionData) {
            with(binding) {
                val data = singleItem as Subject
                title.text = data.subjectName
                btnMateri.setOnClickListener {
                    Log.d("JADWAL KELAS", "bind: Materi")
                    listener.onMaterialItemClicked(absoluteAdapterPosition)
                }
                btnKelas.setOnClickListener {
                    Log.d("JADWAL KELAS", "bind: Kelas")
                    listener.onClassItemClicked(absoluteAdapterPosition)
                }
            }
        }
    }

    inner class StHomeRecyclerViewChildExamViewHolder(private val binding: ItemStHomeSectionItemBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                Log.d("Exam", absoluteAdapterPosition.toString())
            }
        }
        fun bind(singleItem: HomeSectionData) {
            with(binding) {
                val data = singleItem as TaskForm
                title.text = data.subjectName
                btnKelas.text = "Ujian"
                btnMateri.isVisible = false
                singleItem.id?.let { id ->
                    btnKelas.setOnClickListener { listener.onTaskFormItemClicked(id) }
                }
            }
        }
    }

    inner class StHomeRecyclerViewChildAssignmentViewHolder(private val binding: ItemStHomeSectionItemBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                Log.d("Assignment", absoluteAdapterPosition.toString())
            }
        }
        fun bind(singleItem: HomeSectionData) {
            with(binding) {
                val data = singleItem as TaskForm
                title.text = data.subjectName
                btnKelas.text = "Tugas"
                btnMateri.isVisible = false
                singleItem.id?.let { id ->
                    btnKelas.setOnClickListener { listener.onTaskFormItemClicked(id) }
                }
            }
        }
    }

    inner class StHomeRecyclerViewChildPembayaranViewHolder(private val binding: ItemStHomeSectionPembayaranBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                Log.d("Test Child", absoluteAdapterPosition.toString())
            }
        }
        fun bind(singleItem: HomeSectionData) {
            with(binding) {
                val data = singleItem as Payment
                jumlahTagihan.text = data.title
            }
        }
    }

    inner class StHomeRecyclerViewChildPengumumanViewHolder(private val binding: ItemStHomeSectionPengumumanBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                Log.d("Test Child", absoluteAdapterPosition.toString())
            }
        }
        fun bind(singleItem: HomeSectionData) {
            with(binding) {
                val data = singleItem as Announcement
                judul.text = data.title
            }
        }
    }

}