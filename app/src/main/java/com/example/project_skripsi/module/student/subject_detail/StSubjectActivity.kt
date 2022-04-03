package com.example.project_skripsi.module.student.subject_detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navArgs
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.ActivityStSubjectBinding

class StSubjectActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStSubjectBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStSubjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val args: StSubjectActivityArgs by navArgs()
        findNavController(R.id.nav_host_fragment_activity_student_subject)
            .setGraph(R.navigation.st_subject_navigation, args.toBundle())
    }

}