package com.example.project_skripsi.core.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.project_skripsi.core.model.firestore.Administrator
import com.example.project_skripsi.core.model.firestore.Parent
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.model.firestore.Teacher
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult


class AuthRepository {

    companion object {
        var inst = AuthRepository()

        const val LOGIN_STUDENT = 0
        const val LOGIN_TEACHER = 1
        const val LOGIN_PARENT = 2
        const val LOGIN_ADMINISTRATOR = 3
    }

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val loggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData()
    
    private var currentUser : FirebaseUser? = null
    fun getCurrentUser() = currentUser!!

//    ### don't remove yet
//    val userLiveData: MutableLiveData<FirebaseUser?> = MutableLiveData()
//    init {
//        if (firebaseAuth.currentUser != null) {
//            userLiveData.postValue(firebaseAuth.currentUser)
//            loggedOutLiveData.postValue(false)
//        }
//    }


    // return firebaseUser, isFailure
    fun login(email : String, password : String, loginAs : Int) : Pair<LiveData<FirebaseUser>, LiveData<Boolean>> {
        val data = MutableLiveData<FirebaseUser>()
        val failure = MutableLiveData<Boolean>()
        firebaseAuth.let{ login->
            login.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if(task.isSuccessful){
                        Log.d("12345-AuthRepository", "Auth Login Successful")
                        Log.d("12345-AuthRepository", firebaseAuth.currentUser?.uid ?: "NULL")
                        firebaseAuth.currentUser?.let { user ->
                            currentUser = firebaseAuth.currentUser
                            FireRepository.inst
                                .let {
                                    when (loginAs) {
                                        LOGIN_STUDENT -> it.getItem<Student>(user.uid)
                                        LOGIN_TEACHER -> it.getItem<Teacher>(user.uid)
                                        LOGIN_PARENT -> it.getItem<Parent>(user.uid)
                                        LOGIN_ADMINISTRATOR -> it.getItem<Administrator>(user.uid)
                                        else -> null
                                    }
                                }?.let { response ->
                                    response.first.observeOnce { data.postValue(user) }
                                    response.second.observeOnce { failure.postValue(true) }
                                }
                        } ?: kotlin.run {
                            failure.postValue(true)
                        }
                    } else {
                        Log.d("12345-AuthRepository","Error Login")
                        failure.postValue(true)
                    }
                }

        }
        return Pair(data, failure)
    }

    fun logOut() {
        firebaseAuth.signOut()
        loggedOutLiveData.postValue(true)
    }

//    fun register(email: String?, password: String?) {
//        firebaseAuth.createUserWithEmailAndPassword(email!!, password!!)
//            .addOnCompleteListener(application.mainExecutor,
//                { task ->
//                    if (task.isSuccessful) {
//                        userLiveData.postValue(firebaseAuth.currentUser)
//                    } else {
//                        Toast.makeText(
//                            application.applicationContext,
//                            "Registration Failure: " + (task.exception?.message ?: "null message"),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                })
//    }

}