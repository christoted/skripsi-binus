package com.example.project_skripsi.core.repository

import android.graphics.BitmapFactory
import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class FireStorage {

    fun getImage(path: String): Pair<MutableLiveData<Boolean>, MutableLiveData<Exception>> {
        val isSuccess = MutableLiveData<Boolean>()
        val exception = MutableLiveData<Exception>()
//
//        val storage = FirebaseStorage.getInstance().reference.child(path)
//        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        val localFile = File.createTempFile(fileName, ".jpg", storageDirectory)
//        storage.getFile(localFile)
//            .addOnSuccessListener {
////            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
////            binding.imvPhoto.setImageBitmap(bitmap)
//                isSuccess.postValue(true)
//        }
//            .addOnFailureListener {
//            exception.postValue(it)
//        }
//
        return Pair(isSuccess, exception)
    }

}