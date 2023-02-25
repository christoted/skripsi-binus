package com.example.project_skripsi.module.common.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class AuthViewModel : ViewModel() {

    private val _successLoginAs = MutableLiveData<Int>()
    val successLoginAs: LiveData<Int> = _successLoginAs

    private val _errorLogin = MutableLiveData<Boolean>()
    val errorLogin: LiveData<Boolean> = _errorLogin

    fun login(email: String, password: String, loginAs: Int) {
        AuthRepository.inst.login(email, password, loginAs).let { response ->
            response.first.observeOnce { _successLoginAs.postValue(loginAs) }
            response.second.observeOnce { _errorLogin.postValue(true) }
        }
    }

}