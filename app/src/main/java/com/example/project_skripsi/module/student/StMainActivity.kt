package com.example.project_skripsi.module.student

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.project_skripsi.R
import com.example.project_skripsi.databinding.ActivityStMainBinding

class StMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_student_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home_fragment, R.id.navigation_class_fragment, R.id.navigation_calendar_fragment, R.id.navigation_score_fragment, R.id.navigation_payment_fragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home_fragment,
                R.id.navigation_class_fragment,
                R.id.navigation_calendar_fragment,
                R.id.navigation_score_fragment,
                R.id.navigation_payment_fragment ->
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