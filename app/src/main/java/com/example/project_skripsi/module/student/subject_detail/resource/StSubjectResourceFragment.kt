package com.example.project_skripsi.module.student.subject_detail.resource

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_skripsi.core.model.firestore.Resource
import com.example.project_skripsi.databinding.FragmentStSubjectResourceBinding
import com.example.project_skripsi.databinding.ViewEmptyListBinding
import com.example.project_skripsi.module.student.subject_detail.StSubjectViewModel
import com.example.project_skripsi.utils.generic.GenericLinkHandler
import com.example.project_skripsi.utils.generic.GenericLinkHandler.Companion.goToLink
import com.example.project_skripsi.utils.generic.LinkClickListener

class StSubjectResourceFragment(private val viewModel: StSubjectViewModel) : Fragment(),
    LinkClickListener {

    private var _binding: FragmentStSubjectResourceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStSubjectResourceBinding.inflate(inflater, container, false)
        binding.rvResource.layoutManager = LinearLayoutManager(context)
        viewModel.resourceList.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                val emptyView = ViewEmptyListBinding.inflate(layoutInflater, binding.llParent, false)
                emptyView.tvEmpty.text = ("Tidak ada materi")
                binding.llParent.addView(emptyView.root)
            } else {
                binding.rvResource.adapter = StSubjectResourceAdapter(it, this@StSubjectResourceFragment)
            }
        })

        viewModel.incompleteResource.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(
                    requireContext(),
                    "Materi \"${it.title}\" (${it.subjectName}) harus dibaca terlebih dahulu",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResourceItemClicked(resource: Resource) {
        viewModel.openResource(requireContext(), resource)
    }
}