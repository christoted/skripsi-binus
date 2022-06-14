package com.example.project_skripsi

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.project_skripsi.core.repository.FireStorage
import com.example.project_skripsi.core.repository.dummy.FirestoreDummy
import com.example.project_skripsi.databinding.ActivityCameraBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.File


class CameraActivity : AppCompatActivity() {
    private val directory = "id-student/id-taskform/"
    private val fileName = "1.jpg"
    private lateinit var binding: ActivityCameraBinding
    private lateinit var photoFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirestoreDummy()
        binding.btnTakePhoto.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(fileName)

            val fileProvider = FileProvider.getUriForFile(this, "com.example.fileprovider", photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                mARLRequestCamera.launch(takePictureIntent)
            } else {
                Toast.makeText(applicationContext, "Unable open camera", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnUpload.setOnClickListener {
            val storage = FirebaseStorage.getInstance().getReference("$directory/$fileName")
            val baos = ByteArrayOutputStream()
            BitmapFactory.decodeFile(photoFile.absolutePath).compress(Bitmap.CompressFormat.JPEG, 15, baos)

            storage.putBytes(baos.toByteArray())
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Sukses oi", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext, "Gagal cok", Toast.LENGTH_SHORT).show()
                }
        }

        binding.btnDownload.setOnClickListener {
            val storage = FirebaseStorage.getInstance().reference.child("$directory/$fileName")
            val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + directory)
            val localFile = File.createTempFile(fileName, ".jpg", storageDirectory)
            storage.getFile(localFile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                binding.imvPhoto.setImageBitmap(bitmap)
            }.addOnSuccessListener {
                Toast.makeText(applicationContext, "Sukses oi2", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + directory)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    var mARLRequestCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            binding.imvPhoto.setImageBitmap(takenImage)
        }
    }

}