package com.example.project_skripsi.module.parent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.project_skripsi.databinding.ActivityPrMainBinding

class PrMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPrMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }
}