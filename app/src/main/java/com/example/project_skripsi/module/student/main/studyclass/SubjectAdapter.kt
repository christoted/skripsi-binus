package com.example.project_skripsi.module.student.main.studyclass

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView.*
import com.example.project_skripsi.core.model.firestore.Subject
import com.example.project_skripsi.databinding.ItemStClassSubjectBinding
import com.example.project_skripsi.module.student.main.studyclass.StClassViewModel.Companion.mapOfSubjectImage

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
                tvSubjectName.text = item.subjectName
                mapOfSubjectImage[item.subjectName]?.let { imvSubjectImage.setImageResource(it) }
            }

            binding.root.setOnClickListener { view ->
                view.findNavController().navigate(
                    StClassFragmentDirections.actionNavigationClassFragmentToStSubjectFragment(item.subjectName!!)
                )
            }
        }
    }
}
