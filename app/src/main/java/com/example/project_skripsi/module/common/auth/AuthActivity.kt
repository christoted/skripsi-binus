package com.example.project_skripsi.module.common.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.project_skripsi.R
import com.example.project_skripsi.core.repository.AuthRepository.Companion.LOGIN_PARENT
import com.example.project_skripsi.core.repository.AuthRepository.Companion.LOGIN_STUDENT
import com.example.project_skripsi.core.repository.AuthRepository.Companion.LOGIN_TEACHER
import com.example.project_skripsi.core.repository.dummy.FirestoreDummy
import com.example.project_skripsi.databinding.ActivityAuthBinding
import com.example.project_skripsi.module.parent.PrMainActivity
import com.example.project_skripsi.module.student.StMainActivity
import com.example.project_skripsi.module.teacher.TcMainActivity
import com.example.project_skripsi.service.StorageSP
import com.example.project_skripsi.service.StorageSP.Companion.SP_EMAIL
import com.example.project_skripsi.service.StorageSP.Companion.SP_LOGIN_AS
import com.example.project_skripsi.service.StorageSP.Companion.SP_PASSWORD
import com.example.project_skripsi.utils.helper.ValidationHelper
import kotlin.math.log

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()


        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        viewModel.successLoginAs.observe(this, { loginAs ->
            StorageSP.set(this, SP_EMAIL, binding.edtEmail.text.toString())
            StorageSP.set(this, SP_PASSWORD, binding.edtPassword.text.toString())
            StorageSP.setInt(this, SP_LOGIN_AS, loginAs)
            when(loginAs) {
                LOGIN_STUDENT -> redirectToStudent()
                LOGIN_TEACHER -> redirectToTeacher()
                LOGIN_PARENT -> redirectToParent()
            }
        })

        viewModel.errorLogin.observe(this, {
            Toast.makeText(applicationContext, "Error Login", Toast.LENGTH_SHORT).show()
            with(binding) {
                edtEmail.isEnabled = true
                edtPassword.isEnabled = true
                btnLogin.isEnabled = true
            }
        })

        with(binding) {
            btnAutoStudent.setOnClickListener{
                edtEmail.setText(("luis2@gmail.com"))
                edtPassword.setText(("123456"))
                cgLoginAs.check(R.id.chip_student)
            }
            btnAutoTeacher.setOnClickListener{
                edtEmail.setText(("devita@gmail.com"))
                edtPassword.setText(("devita"))
                cgLoginAs.check(R.id.chip_teacher)
            }
            btnAutoParent.setOnClickListener{
                edtEmail.setText(("arum@gmail.com"))
                edtPassword.setText(("arum123"))
                cgLoginAs.check(R.id.chip_parent)
            }
            btnLogin.setOnClickListener {
                if (validateInput()) {
                    viewModel.login(edtEmail.text.toString(), edtPassword.text.toString(),
                        when (cgLoginAs.checkedChipId) {
                            R.id.chip_student -> LOGIN_STUDENT
                            R.id.chip_teacher -> LOGIN_TEACHER
                            else -> LOGIN_PARENT
                        }
                    )
                    edtEmail.isEnabled = false
                    edtPassword.isEnabled = false
                    btnLogin.isEnabled = false
                }
            }
        }


        // delaying 0.5s
        Handler(Looper.getMainLooper()).postDelayed({
            initEvent()
        }, 500)

        FirestoreDummy()

    }

    private fun initEvent() {
        val email = StorageSP.get(this, SP_EMAIL) ?: ""
        val password = StorageSP.get(this, SP_PASSWORD) ?: ""
        val loginAs = StorageSP.getInt(this, SP_LOGIN_AS, -1)
        if (loginAs != -1 && email.isNotEmpty() && password.isNotEmpty()) {
            binding.edtEmail.setText(email)
            binding.edtPassword.setText(password)
            viewModel.login(email, password, loginAs)
        } else {
            binding.imvSplashImage.visibility = View.GONE
        }
    }

    private fun validateInput(): Boolean {
        if (ValidationHelper.isStringEmpty(applicationContext, binding.edtEmail.text.toString(), "Email")) return false
        if (ValidationHelper.isStringEmpty(applicationContext, binding.edtPassword.text.toString(), "Email")) return false
        return true
    }

    private fun redirectToStudent() {
        Toast.makeText(applicationContext, "Login Success as Student", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, StMainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun redirectToTeacher() {
        Toast.makeText(applicationContext, "Login Success as Teacher", Toast.LENGTH_SHORT).show()
        val intent = Intent(this,TcMainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun redirectToParent() {
        Toast.makeText(applicationContext, "Login Success as Parent", Toast.LENGTH_SHORT).show()
        val intent = Intent(this,PrMainActivity::class.java)
        startActivity(intent)
        finish()
    }
}