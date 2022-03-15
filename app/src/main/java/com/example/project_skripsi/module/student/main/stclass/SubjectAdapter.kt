package com.example.project_skripsi.module.student.main.stclass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView.*
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.ItemStClassSubjectBinding


class SubjectAdapter(private val subjectList: List<String>) :
    Adapter<SubjectAdapter.SubjectViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SubjectViewHolder =
        SubjectViewHolder(ItemStClassSubjectBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false))

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        holder.bind(subjectList[position])
    }

    override fun getItemCount() = subjectList.size

    class SubjectViewHolder ( private val binding : ItemStClassSubjectBinding) : ViewHolder(binding.root) {
        fun bind(subjectName: String) {
            with(binding) {
                this.textviewSubjectName.text = subjectName
            }

            binding.root.setOnClickListener { view ->
                val toStSubjectActivity = StClassFragmentDirections.actionNavigationClassToStSubjectActivity()
                toStSubjectActivity.subjectName = subjectName
                view.findNavController().navigate(toStSubjectActivity)
            }
        }
    }

}
