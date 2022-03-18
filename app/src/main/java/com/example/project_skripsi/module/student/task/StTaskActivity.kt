package com.example.project_skripsi.module.student.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.ActivityStMainBinding
import com.example.project_skripsi.databinding.ActivityStTaskBinding
import com.example.project_skripsi.module.student.subject_detail.StSubjectActivityArgs
import com.example.project_skripsi.module.student.task.assignment.StTaskAssignmentViewModel
import com.example.project_skripsi.module.student.task.form.StTaskFormViewModel

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
        when (viewModel.navType.value) {
            StTaskViewModel.NAVIGATION_EXAM ->
                navController.navigate(R.id.navigation_st_task_exam_fragment,null, navOptions)
            StTaskViewModel.NAVIGATION_ASSIGNMENT ->
                navController.navigate(R.id.navigation_st_task_assignment_fragment,null, navOptions)
            StTaskViewModel.NAVIGATION_FORM ->
                navController.navigate(R.id.navigation_st_task_form_fragment,null, navOptions)
        }
    }

    private fun retrieveArgs(){
        val args: StTaskActivityArgs by navArgs()
        viewModel.setNavigationType(args.navigationType)
    }

}