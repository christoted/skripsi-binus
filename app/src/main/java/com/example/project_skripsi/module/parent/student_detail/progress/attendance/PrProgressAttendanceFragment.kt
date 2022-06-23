package com.example.project_skripsi.module.parent.student_detail.progress.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.databinding.FragmentPrProgressAttendanceBinding
import com.example.project_skripsi.databinding.ViewEmptyListBinding
import com.example.project_skripsi.module.parent.student_detail.progress.PrProgressViewModel
import com.example.project_skripsi.module.parent.student_detail.progress.attendance.viewholder.PrProgressAttendanceViewHolder

class PrProgressAttendanceFragment(private val viewModel: PrProgressViewModel) : Fragment() {

    private var _binding: FragmentPrProgressAttendanceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrProgressAttendanceBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        viewModel.sectionAttendance.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                val emptyView =
                    ViewEmptyListBinding.inflate(layoutInflater, binding.llParent, false)
                emptyView.tvEmpty.text = ("Tidak ada absensi")
                binding.llParent.addView(emptyView.root)
            } else {
                binding.recyclerView.adapter = PrProgressAttendanceViewHolder(it).getAdapter()
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}