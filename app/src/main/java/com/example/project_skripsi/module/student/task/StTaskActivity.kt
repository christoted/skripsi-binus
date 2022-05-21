package com.example.project_skripsi.module.student.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.ActivityStTaskBinding
import com.example.project_skripsi.utils.custom_views.DummyFragmentDirections

class StTaskActivity : AppCompatActivity() {

    private lateinit var viewModel: StTaskViewModel
    private lateinit var binding: ActivityStTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[StTaskViewModel::class.java]
        binding = ActivityStTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        retrieveArgs()

        val navController = findNavController(R.id.nav_host_fragment_activity_student_task)
        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.navigation_dummy_fragment,true)
                            .build()
        navController.setGraph(R.navigation.st_task_navigation)

        viewModel.navType.observe(this, {
            when(it) {
                StTaskViewModel.NAVIGATION_EXAM -> {
                    navController.navigate(DummyFragmentDirections.actionNavigationDummyFragmentToNavigationStTaskExamFragment(),navOptions)
//                    navController.navigate(R.id.navigation_st_task_exam_fragment,null, navOptions)
                }

                StTaskViewModel.NAVIGATION_ASSIGNMENT -> {
                    navController.navigate(DummyFragmentDirections.actionNavigationDummyFragmentToNavigationStTaskAssignmentFragment(),navOptions)
//                    navController.navigate(R.id.navigation_st_task_assignment_fragment,null, navOptions)
                }

                StTaskViewModel.NAVIGATION_FORM -> {
                    val toTaskFormFragment = DummyFragmentDirections.actionNavigationDummyFragmentToNavigationStTaskFormFragment(
                        viewModel.taskFormId.value!!
                    )
                    navController.navigate(toTaskFormFragment,navOptions)
                }

            }
        })

    }

    private fun retrieveArgs(){
        val args: StTaskActivityArgs by navArgs()
        viewModel.setNavigationData(args.navigationType, args.taskFormId)
    }

    override fun onBackPressed() {
        val hasBack = findNavController(R.id.nav_host_fragment_activity_student_task).popBackStack()
        if (!hasBack) finish()
    }

}