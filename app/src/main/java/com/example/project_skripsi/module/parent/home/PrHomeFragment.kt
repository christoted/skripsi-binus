package com.example.project_skripsi.module.parent.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.PagerAdapter
import com.example.project_skripsi.databinding.*
import com.example.project_skripsi.utils.generic.ItemClickListener

class PrHomeFragment : Fragment(), ItemClickListener {

    private lateinit var viewModel: PrHomeViewModel
    private var _binding: FragmentPrHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[PrHomeViewModel::class.java]
        _binding = FragmentPrHomeBinding.inflate(inflater, container, false)

        viewModel.studentList.observe(viewLifecycleOwner, {
            with(binding) {
                vpStudent.adapter = ScreenSlidePagerAdapter()
                tablStudent.setupWithViewPager(binding.vpStudent)
                if (viewModel.getStudentPageCount() <= 1) binding.tablStudent.visibility = View.GONE
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(itemId: String) {
        view?.findNavController()?.navigate(
            PrHomeFragmentDirections.actionPrHomeFragmentToPrStudentDetailFragment(itemId)
        )
    }

    private inner class ScreenSlidePagerAdapter : PagerAdapter(){

        lateinit var layoutInflater: LayoutInflater

        override fun getCount(): Int =
            viewModel.getStudentPageCount()

        override fun isViewFromObject(view: View, `object`: Any): Boolean =
            view == `object`

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = LayoutInflater.from(context)
            val binding2 = ViewRecyclerViewBinding.inflate(layoutInflater, container, false)

            binding2.rvContainer.layoutManager = GridLayoutManager(context, 3)
            binding2.rvContainer.adapter = StudentViewHolder(viewModel.getStudents(position), this@PrHomeFragment).getAdapter()

            container.addView(binding2.root, 0)
            return binding2.root
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}