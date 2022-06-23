package com.example.project_skripsi.module.student.main.studyclass

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.PagerAdapter
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.FragmentStClassBinding
import com.example.project_skripsi.databinding.FragmentStClassSubjectBinding
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs


class StClassFragment : Fragment() {

    private lateinit var viewModel: StClassViewModel
    private var _binding: FragmentStClassBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

         viewModel = ViewModelProvider(this)[StClassViewModel::class.java]
        _binding = FragmentStClassBinding.inflate(inflater, container, false)

        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                binding.toolbar.visibility = View.VISIBLE
                binding.collapseLayout.title = binding.tvClassName.text
            } else {
                binding.toolbar.visibility = View.GONE
                binding.collapseLayout.title = ""
            }
        })

        binding.btnExam.setOnClickListener { view ->
            view.findNavController()
                .navigate(StClassFragmentDirections.actionNavigationClassFragmentToStTaskExamFragment())
        }

        binding.btnAssignment.setOnClickListener { view ->
            view.findNavController()
                .navigate(StClassFragmentDirections.actionNavigationClassFragmentToStTaskAssignmentFragment())
        }

        viewModel.studyClass.observe(viewLifecycleOwner) {
            with(binding) {
                tvClassName.text = it.name
                viewpagerSubject.adapter = ScreenSlidePagerAdapter()
                tablSubject.setupWithViewPager(binding.viewpagerSubject)
                if (viewModel.getSubjectPageCount() <= 1) binding.tablSubject.visibility = View.GONE

                it.id?.let { studyClassId ->
                    fabStudentList.setOnClickListener { _ ->
                        view?.findNavController()?.navigate(
                            StClassFragmentDirections
                                .actionNavigationClassFragmentToStStudentListFragment(
                                    studyClassId,
                                    it.name!!
                                )
                        )
                    }
                }
            }
        }

        viewModel.teacher.observe(viewLifecycleOwner) { teacher ->
            with(binding) {
                tvTeacherName.text = teacher.name
                teacher.phoneNumber?.let { imvTeacherPhone.setImageResource(R.drawable.whatsapp)
                    imvTeacherPhone.setOnClickListener {
                        goToWhatsApp(phoneNumber = teacher.phoneNumber ?: "")
                    }
                }

            }
        }

        viewModel.classChief.observe(viewLifecycleOwner) { student ->
            with(binding) {
                tvChiefName.text = student.name
                student.phoneNumber?.let {
                    imvChiefPhone.setImageResource(R.drawable.whatsapp)
                    imvChiefPhone.setOnClickListener {
                        goToWhatsApp(phoneNumber = student.phoneNumber ?: "")
                    }
                }

            }
        }
        return binding.root
    }

    private fun goToWhatsApp(phoneNumber: String) {
        val url = "https://api.whatsapp.com/send/?phone=${phoneNumber}"
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class ScreenSlidePagerAdapter : PagerAdapter(){

        lateinit var layoutInflater: LayoutInflater

        override fun getCount(): Int =
            viewModel.getSubjectPageCount()

        override fun isViewFromObject(view: View, `object`: Any): Boolean =
            view == `object`

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = LayoutInflater.from(context)
            val binding2 = FragmentStClassSubjectBinding.inflate(layoutInflater, container, false)

            binding2.rvSubjectContainer.layoutManager = GridLayoutManager(context, 4)
            binding2.rvSubjectContainer.adapter = SubjectAdapter(viewModel.getSubjects(position))

            container.addView(binding2.root, 0)
            return binding2.root
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}