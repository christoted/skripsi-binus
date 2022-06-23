package com.example.project_skripsi.module.parent.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.databinding.*
import com.example.project_skripsi.module.common.auth.AuthActivity
import com.example.project_skripsi.module.parent.home.viewholder.agenda.PrHomeRecyclerViewMainAdapter
import com.example.project_skripsi.module.parent.home.viewholder.student.StudentViewHolder
import com.example.project_skripsi.utils.generic.ItemClickListener
import com.example.project_skripsi.utils.service.storage.StorageSP

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
                val pageCount = viewModel.getStudentPageCount()
                vpStudent.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {}
                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                    }

                    override fun onPageSelected(position: Int) {
                        imvLeft.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
                        imvRight.visibility =
                            if (position + 1 == pageCount) View.INVISIBLE else View.VISIBLE
                    }
                })
                if (vpStudent.hasNext()) imvRight.visibility = View.VISIBLE
                if (vpStudent.hasPrev()) imvLeft.visibility = View.VISIBLE

                imvLeft.setOnClickListener { vpStudent.prevPage() }
                imvRight.setOnClickListener { vpStudent.nextPage() }
            }
        })

        with(binding.recyclerviewClass) {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            viewModel.sectionData.observe(
                viewLifecycleOwner,
                { adapter = PrHomeRecyclerViewMainAdapter(it) })
        }

        binding.imvLogout.setOnClickListener {
            showConfirmationDialog()
        }

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

    private inner class ScreenSlidePagerAdapter : PagerAdapter() {

        lateinit var layoutInflater: LayoutInflater

        override fun getCount(): Int =
            viewModel.getStudentPageCount()

        override fun isViewFromObject(view: View, `object`: Any): Boolean =
            view == `object`

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = LayoutInflater.from(context)
            val binding2 = ViewRecyclerViewBgwhiteBinding.inflate(layoutInflater, container, false)

            binding2.rvContainer.layoutManager = GridLayoutManager(context, 3)
            binding2.rvContainer.adapter =
                StudentViewHolder(viewModel.getStudents(position), this@PrHomeFragment).getAdapter()

            container.addView(binding2.root, 0)
            return binding2.root
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Konfirmasi")
            .setMessage("Apakah anda yakin untuk logout?")
            .setPositiveButton("Ok") { _, _ ->
                StorageSP.setString(requireActivity(), StorageSP.SP_EMAIL, "")
                StorageSP.setString(requireActivity(), StorageSP.SP_PASSWORD, "")
                StorageSP.setInt(requireActivity(), StorageSP.SP_LOGIN_AS, -1)
                AuthRepository.inst.logOut()
                val intent = Intent(binding.root.context, AuthActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
            .setNegativeButton("Batal") { _, _ -> }
            .create()
            .show()
    }
}