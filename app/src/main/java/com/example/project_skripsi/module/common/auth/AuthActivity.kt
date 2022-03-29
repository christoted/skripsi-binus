package com.example.project_skripsi.module.common.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.project_skripsi.R
import com.example.project_skripsi.core.repository.AuthRepository.Companion.LOGIN_PARENT
import com.example.project_skripsi.core.repository.AuthRepository.Companion.LOGIN_STUDENT
import com.example.project_skripsi.core.repository.AuthRepository.Companion.LOGIN_TEACHER
import com.example.project_skripsi.core.repository.dummy.FirestoreDummy
import com.example.project_skripsi.module.student.main.StMainActivity

class AuthActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        viewModel.successLoginAs.observe(this, { loginAs ->
            when(loginAs) {
                LOGIN_STUDENT -> redirectToStudent()
                LOGIN_TEACHER -> redirectToTeacher()
                LOGIN_PARENT -> redirectToParent()
            }
        })

        viewModel.errorLogin.observe(this, {
            Toast.makeText(applicationContext, "Error Login", Toast.LENGTH_SHORT).show()
        })

        viewModel.login("luis2@gmail.com","123456", LOGIN_STUDENT)

        FirestoreDummy()
    }

    private fun redirectToStudent() {
        Toast.makeText(applicationContext, "Login Success as Student", Toast.LENGTH_SHORT).show()
        val intent = Intent(this,StMainActivity::class.java)
        startActivity(intent)
    }

    private fun redirectToTeacher() {
        Toast.makeText(applicationContext, "Login Success as Teacher", Toast.LENGTH_SHORT).show()
    }

    private fun redirectToParent() {
        Toast.makeText(applicationContext, "Login Success as Parent", Toast.LENGTH_SHORT).show()
    }
}