package com.example.project_skripsi.module.parent

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
import com.example.project_skripsi.databinding.ActivityPrMainBinding
import com.example.project_skripsi.databinding.ActivityStMainBinding
import com.example.project_skripsi.databinding.ActivityTcMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class PrMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPrMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }
}