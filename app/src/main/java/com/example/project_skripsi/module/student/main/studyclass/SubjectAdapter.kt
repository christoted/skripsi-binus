package com.example.project_skripsi.module.student.main.studyclass

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView.*
import com.example.project_skripsi.core.model.firestore.Subject
import com.example.project_skripsi.databinding.ItemStClassSubjectBinding

class SubjectAdapter(private val subjectList: List<Subject>) :
    Adapter<SubjectAdapter.SubjectViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SubjectViewHolder =
        SubjectViewHolder(ItemStClassSubjectBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false))

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        holder.bind(subjectList[position])
    }

    override fun getItemCount() = subjectList.size

    class SubjectViewHolder ( private val binding : ItemStClassSubjectBinding) : ViewHolder(binding.root) {
        fun bind(item: Subject) {
            with(binding) {
                this.textviewSubjectName.text = item.subjectName
            }

            binding.root.setOnClickListener { view ->
                val toStSubjectActivity = StClassFragmentDirections.actionNavigationClassToStSubjectActivity()
                toStSubjectActivity.subjectName = item.subjectName!!
                view.findNavController().navigate(toStSubjectActivity)
            }
        }
    }
}
