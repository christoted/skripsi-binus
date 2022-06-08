package com.example.project_skripsi.module.student.main.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.FragmentStHomeBinding
import com.example.project_skripsi.module.student.main.home.view.adapter.ItemListener
import com.example.project_skripsi.module.student.main.home.view.adapter.StHomeRecyclerViewMainAdapter
import com.example.project_skripsi.module.student.main.home.viewmodel.StHomeViewModel


class StHomeFragment : Fragment(), ItemListener {

    private lateinit var viewModel: StHomeViewModel
    private var _binding: FragmentStHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[StHomeViewModel::class.java]
        _binding = FragmentStHomeBinding.inflate(inflater, container, false)

        viewModel.currentStudent.observe(viewLifecycleOwner, {
            binding.tvProfileName.text = ("${it.name} (${it.attendanceNumber})")
            it.profile?.let { imageUrl ->
                Glide
                    .with(context!!)
                    .load(imageUrl)
                    .placeholder(R.drawable.profile_empty)
                    .into(binding.ivProfilePicture)
            }
        })

        viewModel.profileClass.observe(viewLifecycleOwner, { binding.tvProfileClass.text = it })

        with(binding.recyclerviewClass) {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            viewModel.sectionData.observe(viewLifecycleOwner, { adapter = StHomeRecyclerViewMainAdapter(it, this@StHomeFragment) })
        }

        binding.imvSettings.setOnClickListener {
            view?.findNavController()?.navigate(
                StHomeFragmentDirections.actionNavigationHomeFragmentToStProfileFragment()
            )
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTaskFormItemClicked(taskFormId: String, subjectName: String) {
        view?.findNavController()?.navigate(
            StHomeFragmentDirections.actionNavigationHomeFragmentToStTaskFormFragment(taskFormId)
        )
    }

    override fun onClassItemClicked(Position: Int) {
       
    }

    override fun onMaterialItemClicked(Position: Int) {

    }
}