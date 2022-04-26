package com.example.project_skripsi.module.common.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.project_skripsi.R
import com.example.project_skripsi.core.repository.AuthRepository.Companion.LOGIN_PARENT
import com.example.project_skripsi.core.repository.AuthRepository.Companion.LOGIN_STUDENT
import com.example.project_skripsi.core.repository.AuthRepository.Companion.LOGIN_TEACHER
import com.example.project_skripsi.core.repository.dummy.FirestoreDummy
import com.example.project_skripsi.databinding.ActivityAuthBinding
import com.example.project_skripsi.module.parent.PrMainActivity
import com.example.project_skripsi.module.student.main.StMainActivity
import com.example.project_skripsi.module.teacher.TcMainActivity

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        with(binding) {
            btnAutoStudent.setOnClickListener{ viewModel.login("luis2@gmail.com","123456", LOGIN_STUDENT) }
            btnAutoTeacher.setOnClickListener{ viewModel.login("devita@gmail.com","devita", LOGIN_TEACHER) }
            btnAutoParent.setOnClickListener{
//                Log.d("12345", "test");
//                Toast.makeText(baseContext, "oi", Toast.LENGTH_SHORT).show()
                viewModel.login("arum@gmail.com","arum123", LOGIN_PARENT)
            }
        }
        FirestoreDummy()

    }

    private fun redirectToStudent() {
        Toast.makeText(applicationContext, "Login Success as Student", Toast.LENGTH_SHORT).show()
        val intent = Intent(this,StMainActivity::class.java)
        startActivity(intent)
    }

    private fun redirectToTeacher() {
        Toast.makeText(applicationContext, "Login Success as Teacher", Toast.LENGTH_SHORT).show()
        val intent = Intent(this,TcMainActivity::class.java)
        startActivity(intent)
    }

    private fun redirectToParent() {
        Toast.makeText(applicationContext, "Login Success as Parent", Toast.LENGTH_SHORT).show()
        val intent = Intent(this,PrMainActivity::class.java)
        startActivity(intent)
    }
}