package com.example.project_skripsi.core.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.File

class FireStorage {

    companion object {
        var inst = FireStorage()
    }

    fun getImage(
        pathFile: String,
        context: Context
    ): Pair<MutableLiveData<File>, MutableLiveData<Exception>> {
        val data = MutableLiveData<File>()
        val exception = MutableLiveData<Exception>()

        val tempFile = getPhotoFile(pathFile, context)

        FirebaseStorage.getInstance().reference.child(pathFile)
            .getFile(tempFile)
            .addOnSuccessListener {
                data.postValue(tempFile)
            }.addOnFailureListener {
                exception.postValue(it)
            }

        return Pair(data, exception)
    }

    fun uploadImage(
        pathFile: String,
        localFile: File
    ): Pair<MutableLiveData<Boolean>, MutableLiveData<Exception>> {
        val isSuccess = MutableLiveData<Boolean>()
        val exception = MutableLiveData<Exception>()

        val baos = ByteArrayOutputStream()
        BitmapFactory
            .decodeFile(localFile.absolutePath)
            ?.compress(Bitmap.CompressFormat.JPEG, 15, baos)

        FirebaseStorage.getInstance().getReference(pathFile)
            .putBytes(baos.toByteArray())
            .addOnSuccessListener {
                isSuccess.postValue(true)
            }
            .addOnFailureListener {
                exception.postValue(it)
            }

        return Pair(isSuccess, exception)
    }

    private fun splitPath(pathFile: String): Pair<String, String> {
        val split = pathFile.split("/")
        var path = ""
        repeat(split.size - 1) {
            if (it > 0) path += "/"
            path += split[it]
        }
        val fileName = split.last()
        return Pair(path, fileName)
    }

    fun getPhotoFile(pathFile: String, context: Context): File {
        val split = splitPath(pathFile)
        val storageDirectory =
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + split.first)
        return File.createTempFile(split.second, ".jpg", storageDirectory)
    }

}