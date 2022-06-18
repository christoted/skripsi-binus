package com.example.project_skripsi.module.student.task.exam

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.FragmentStTaskExamBinding
import com.example.project_skripsi.databinding.ViewRecyclerViewBinding
import com.example.project_skripsi.module.student.main.progress.graphic.StSubjectFilterViewHolder
import com.example.project_skripsi.module.student.task._sharing.TaskViewHolder
import com.example.project_skripsi.utils.generic.ItemClickListener
import com.example.project_skripsi.utils.helper.UIHelper
import com.google.android.material.bottomsheet.BottomSheetDialog

class StTaskExamFragment : Fragment(), ItemClickListener {

    private lateinit var viewModel: StTaskExamViewModel
    private var _binding: FragmentStTaskExamBinding? = null
    private val binding get() = _binding!!

    private var dialog : BottomSheetDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[StTaskExamViewModel::class.java]
        _binding = FragmentStTaskExamBinding.inflate(inflater, container, false)

        binding.vpContainer.adapter = ScreenSlidePagerAdapter()
        binding.tabLayout.setupWithViewPager(binding.vpContainer)

        viewModel.subjects.observe(viewLifecycleOwner, { list ->
            binding.btnFilter.setOnClickListener { showBottomSheet(list) }
        })

        binding.imvBack.setOnClickListener { view?.findNavController()?.popBackStack() }

        return binding.root
    }

    @SuppressLint("InflateParams")
    private fun showBottomSheet(list: List<String>) {
        dialog = BottomSheetDialog(context!!)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_st_filter, null)

        val rvItem = view.findViewById<RecyclerView>(R.id.rv_item)
        rvItem.layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        rvItem.addItemDecoration(dividerItemDecoration)

        rvItem.adapter = StSubjectFilterViewHolder(list, this@StTaskExamFragment).getAdapter()

        dialog?.let {
            it.setContentView(view)
            it.show()
        }
    }

    override fun onItemClick(itemId: String) {
        dialog?.hide()
        viewModel.filter(itemId)
        binding.tvTitle.text = ("Ujian ${if (itemId == "Semua") "" else itemId}")
    }

    override fun onResume() {
        super.onResume()
        viewModel.reInit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class ScreenSlidePagerAdapter : PagerAdapter(){

        lateinit var layoutInflater: LayoutInflater
        private var ongoingEmptyView : View? = null
        private var pastEmptyView : View? = null

        override fun getCount(): Int =
            StTaskExamViewModel.tabCount

        override fun isViewFromObject(view: View, `object`: Any): Boolean =
            view == `object`

        override fun getPageTitle(position: Int): CharSequence =
            StTaskExamViewModel.tabHeader[position]


        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = LayoutInflater.from(context)
            val bindingRV = ViewRecyclerViewBinding.inflate(layoutInflater, container, false)

            bindingRV.rvContainer.layoutManager = LinearLayoutManager(context)
            when(position) {
                StTaskExamViewModel.EXAM_ONGOING -> {
                    viewModel.ongoingList.observe(viewLifecycleOwner, { list ->
                        ongoingEmptyView?.let { bindingRV.llParent.removeView(it) }
                        if (list.isEmpty()) {
                            val emptyView = UIHelper.getEmptyList("Tidak ada ujian yang sedang berlangsung", layoutInflater, bindingRV.llParent)
                            bindingRV.llParent.addView(emptyView)
                            ongoingEmptyView = emptyView
                        }
                        bindingRV.rvContainer.adapter = TaskViewHolder(TaskViewHolder.TYPE_EXAM, list, true).getAdapter()
                    })
                }
                StTaskExamViewModel.EXAM_PAST -> {
                    viewModel.pastList.observe(viewLifecycleOwner, { list ->
                        pastEmptyView?.let { bindingRV.llParent.removeView(it) }
                        if (list.isEmpty()) {
                            val emptyView = UIHelper.getEmptyList("Tidak ada ujian yang sudah selesai", layoutInflater, bindingRV.llParent)
                            bindingRV.llParent.addView(emptyView)
                            pastEmptyView = emptyView
                        }
                        bindingRV.rvContainer.adapter = TaskViewHolder(TaskViewHolder.TYPE_EXAM, list, true).getAdapter()
                    })
                }
            }

            container.addView(bindingRV.root, 0)
            return bindingRV.root
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }


}

