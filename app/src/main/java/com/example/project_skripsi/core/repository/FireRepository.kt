package com.example.project_skripsi.core.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore


class FireRepository : OnSuccessListener<Void>, OnFailureListener {

    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    companion object {
        var inst = FireRepository()

        private const val COLLECTION_ADMINISTRATOR = "administrators"
        private const val COLLECTION_TASK_FORM = "task_forms"
        private const val COLLECTION_RESOURCE = "resources"
//        private const val COLLECTION_STUDENT = "students"
//        private const val COLLECTION_TEACHER = "teachers"
//        private const val COLLECTION_PARENT = "parents"
//        private const val COLLECTION_STUDY_CLASS = "study_classes"
//        private const val COLLECTION_ANNOUNCEMENT = "announcements"
//        private const val COLLECTION_SCHOOL = "schools"

        private const val COLLECTION_STUDY_CLASS = "admin-study-class"
        private const val COLLECTION_STUDENT = "admin-student"
        private const val COLLECTION_SCHOOL = "admin-school"
        private const val COLLECTION_PARENT = "admin-parent"
        private const val COLLECTION_TEACHER = "admin-lecture"
        private const val COLLECTION_ANNOUNCEMENT = "admin-announcement"

        /** ALL SCHOOL */
//        private const val COLLECTION_ADMINISTRATOR  = "global-administrator"

        /** SCHOOL 1 */
//        private const val COLLECTION_STUDENT        = "s1-student"
//        private const val COLLECTION_TEACHER        = "s1-teacher"
//        private const val COLLECTION_PARENT         = "s1-parent"
//        private const val COLLECTION_STUDY_CLASS    = "s1-study_class"
//        private const val COLLECTION_SCHOOL         = "s1-school"
//        private const val COLLECTION_RESOURCE       = "s1-resource"
//        private const val COLLECTION_TASK_FORM      = "s1-task_form"
//        private const val COLLECTION_ANNOUNCEMENT   = "s1-announcement"

        /** SCHOOL 2 */
//        private const val COLLECTION_STUDENT        = "s2-student"
//        private const val COLLECTION_TEACHER        = "s2-teacher"
//        private const val COLLECTION_PARENT         = "s2-parent"
//        private const val COLLECTION_STUDY_CLASS    = "s2-study_class"
//        private const val COLLECTION_SCHOOL         = "s2-school"
//        private const val COLLECTION_RESOURCE       = "s2-resource"
//        private const val COLLECTION_TASK_FORM      = "s2-task_form"
//        private const val COLLECTION_ANNOUNCEMENT   = "s2-announcement"

        /** SCHOOL 3 */
//        private const val COLLECTION_STUDENT        = "s3-student"
//        private const val COLLECTION_TEACHER        = "s3-teacher"
//        private const val COLLECTION_PARENT         = "s3-parent"
//        private const val COLLECTION_STUDY_CLASS    = "s3-study_class"
//        private const val COLLECTION_SCHOOL         = "s3-school"
//        private const val COLLECTION_RESOURCE       = "s3-resource"
//        private const val COLLECTION_TASK_FORM      = "s3-task_form"
//        private const val COLLECTION_ANNOUNCEMENT   = "s3-announcement"

        /** SCHOOL 4 */
//        private const val COLLECTION_STUDENT        = "s4-student"
//        private const val COLLECTION_TEACHER        = "s4-teacher"
//        private const val COLLECTION_PARENT         = "s4-parent"
//        private const val COLLECTION_STUDY_CLASS    = "s4-study_class"
//        private const val COLLECTION_SCHOOL         = "s4-school"
//        private const val COLLECTION_RESOURCE       = "s4-resource"
//        private const val COLLECTION_TASK_FORM      = "s4-task_form"
//        private const val COLLECTION_ANNOUNCEMENT   = "s4-announcement"

        val mapCollection = mapOf(
            Student::class to COLLECTION_STUDENT,
            Teacher::class to COLLECTION_TEACHER,
            Parent::class to COLLECTION_PARENT,
            Administrator::class to COLLECTION_ADMINISTRATOR,
            StudyClass::class to COLLECTION_STUDY_CLASS,
            TaskForm::class to COLLECTION_TASK_FORM,
            Resource::class to COLLECTION_RESOURCE,
            Announcement::class to COLLECTION_ANNOUNCEMENT,
            School::class to COLLECTION_SCHOOL,
        )
    }

    inline fun <reified T> getItem(uid : String) : Pair<LiveData<T>, LiveData<Exception>> {
        val data = MutableLiveData<T>()
        val exception = MutableLiveData<Exception>()
        val db = FirebaseFirestore.getInstance()
        mapCollection[T::class]?.let { collection ->
            db.collection(collection)
                .document(uid)
                .get()
                .addOnSuccessListener { result ->
                    if (result.data != null) data.postValue(result.toObject(T::class.java))
                    else exception.postValue(java.lang.Exception("$collection uid not found"))
                }
                .addOnFailureListener { ex -> exception.postValue(ex)}
        }

        return Pair(data, exception)
    }

    inline fun <reified T> getItems(uids : List<String>) : Pair<LiveData<List<T>>, LiveData<Exception>> {
        val data = MutableLiveData<List<T>>()
        val exception = MutableLiveData<Exception>()
        val results = mutableListOf<T>()
        uids.map { uid ->
            getItem<T>(uid).let {
                it.first.observeOnce{ item ->
                    results.add(item)
                    if (results.size == uids.size) data.postValue(results)
                }
                it.second.observeOnce{ _exception -> exception.postValue(_exception) }
            }
        }
        if (results.size == uids.size) data.postValue(results)
        return Pair(data, exception)
    }

    inline fun <reified T> getAllItems() : Pair<LiveData<List<T>>, LiveData<Exception>> {
        val list = mutableListOf<T>()
        val data = MutableLiveData<List<T>>()
        val exception = MutableLiveData<Exception>()

        val db = FirebaseFirestore.getInstance()
        mapCollection[T::class]?.let { collection ->
            db.collection(collection)
                .get()
                .addOnSuccessListener { result ->
                    result.map { document ->
                        list.add(document.toObject(T::class.java))
                    }
                    data.postValue(list)
                }
                .addOnFailureListener { ex -> exception.postValue(ex)}
        }
        return Pair(data, exception)
    }


    fun alterItems(items : List<Any>) : Pair<LiveData<Boolean>, LiveData<Exception>> {
        val isSuccess = MutableLiveData<Boolean>()
        val exception = MutableLiveData<Exception>()

        var successCounter = 0
        items.map {
            mapCollection[it::class]?.let { collection ->
                db.collection(collection)
                    .document(
                        when (it) {
                            is Student -> it.id!!
                            is Teacher -> it.id!!
                            is Parent -> it.id!!
                            is Administrator -> it.id!!
                            is StudyClass -> it.id!!
                            is TaskForm -> it.id!!
                            is Resource -> it.id!!
                            is Announcement -> it.id!!
                            is School -> it.id!!
                            else -> "null"
                        }
                    )
                    .set(it)
                    .addOnSuccessListener {
                        successCounter++
                        if (successCounter == items.size) isSuccess.postValue(true)
                    }
                    .addOnFailureListener { _exception -> exception.postValue(_exception) }
            }
        }
        if (successCounter == items.size) isSuccess.postValue(true)
        return Pair(isSuccess, exception)
    }


    override fun onSuccess(p0: Void?) {
//        TODO("Not yet implemented")
    }

    override fun onFailure(p0: java.lang.Exception) {
//        TODO("Not yet implemented")
    }

}