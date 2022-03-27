package com.example.project_skripsi.core.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.project_skripsi.core.model.firestore.Administrator
import com.example.project_skripsi.core.model.firestore.Parent
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.model.firestore.Teacher
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepository {

    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    companion object {
        var instance = FirestoreRepository()

        const val COLLECTION_STUDENT = "students"
        const val COLLECTION_TEACHER = "teachers"
        const val COLLECTION_PARENT = "parents"
        const val COLLECTION_ADMINISTRATOR = "administrators"
        const val COLLECTION_STUDY_CLASS = "study_classes"
        const val COLLECTION_TASK_FORM = "task_forms"
        const val COLLECTION_RESOURCE = "resources"
        const val COLLECTION_ANNOUNCEMENT = "announcements"
    }

    fun getStudent(uid: String) : Pair<LiveData<Student>, LiveData<Exception>> {
        val data = MutableLiveData<Student>()
        val exception = MutableLiveData<Exception>()
        db.collection(COLLECTION_STUDENT)
            .document(uid)
            .get()
            .addOnSuccessListener { student ->
                if (student.data != null) data.postValue(student.toObject(Student::class.java))
                else exception.postValue(java.lang.Exception("student uid not found"))
            }
            .addOnFailureListener { ex -> exception.postValue(ex)}
        return Pair(data, exception)
    }

    fun getTeacher(uid: String) : Pair<LiveData<Teacher>, LiveData<Exception>> {
        val data = MutableLiveData<Teacher>()
        val exception = MutableLiveData<Exception>()
        db.collection(COLLECTION_TEACHER)
            .document(uid)
            .get()
            .addOnSuccessListener { teacher ->
                if (teacher.data != null) data.postValue(teacher.toObject(Teacher::class.java))
                else exception.postValue(java.lang.Exception("teacher uid not found"))
            }
            .addOnFailureListener { ex -> exception.postValue(ex)}
        return Pair(data, exception)
    }

    fun getParent(uid: String) : Pair<LiveData<Parent>, LiveData<Exception>> {
        val data = MutableLiveData<Parent>()
        val exception = MutableLiveData<Exception>()
        db.collection(COLLECTION_PARENT)
            .document(uid)
            .get()
            .addOnSuccessListener { parent ->
                if (parent.data != null) data.postValue(parent.toObject(Parent::class.java))
                else exception.postValue(java.lang.Exception("parent uid not found"))
            }
            .addOnFailureListener { ex -> exception.postValue(ex)}
        return Pair(data, exception)
    }

    fun getAdministrator(uid: String) : Pair<LiveData<Administrator>, LiveData<Exception>> {
        val data = MutableLiveData<Administrator>()
        val exception = MutableLiveData<Exception>()
        db.collection(COLLECTION_ADMINISTRATOR)
            .document(uid)
            .get()
            .addOnSuccessListener { administrator ->
                if (administrator.data != null) data.postValue(administrator.toObject(Administrator::class.java))
                else exception.postValue(java.lang.Exception("administrator uid not found"))
            }
            .addOnFailureListener { ex -> exception.postValue(ex)}
        return Pair(data, exception)
    }

}