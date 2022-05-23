package com.example.project_skripsi.module.teacher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.project_skripsi.R
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.project_skripsi.databinding.ActivityStMainBinding
import com.example.project_skripsi.databinding.ActivityTcMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class TcMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTcMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTcMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_teacher_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.tcHomeFragment,
                R.id.tcStudyClassFragment,
                R.id.tcCalendarFragment,
                R.id.tcResourceFragment,
                R.id.tcTaskFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.tcHomeFragment,
                R.id.tcStudyClassFragment,
                R.id.tcCalendarFragment,
                R.id.tcResourceFragment,
                R.id.tcTaskFragment ->
                    showBottomNav()
                else -> hideBottomNav()
            }
        }
    }

    private fun showBottomNav() {
        binding.navView.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.navView.visibility = View.GONE
    }
}