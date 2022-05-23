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

        const val COLLECTION_ADMINISTRATOR = "administrators"
        const val COLLECTION_TASK_FORM = "task_forms"
        const val COLLECTION_RESOURCE = "resources"
        const val COLLECTION_STUDENT = "students"
        const val COLLECTION_TEACHER = "teachers"
        const val COLLECTION_PARENT = "parents"
        const val COLLECTION_STUDY_CLASS = "study_classes"
        const val COLLECTION_ANNOUNCEMENT = "announcements"
        const val COLLECTION_SCHOOL = "schools"

//        const val COLLECTION_STUDY_CLASS = "admin-study-class"
//        const val COLLECTION_STUDENT = "admin-student"
//        const val COLLECTION_SCHOOL = "admin-school"
//        const val COLLECTION_PARENT = "admin-parent"
//        const val COLLECTION_TEACHER = "admin-lecture"
//        const val COLLECTION_ANNOUNCEMENT = "admin-announcement"

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

    @Deprecated("Replaced by getItem")
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

    @Deprecated("Replaced by getItem")
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

    @Deprecated("Replaced by getItem")
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

    @Deprecated("Replaced by getItem")
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

    @Deprecated("Replaced by getItem")
    fun getStudyClass(uid: String) : Pair<LiveData<StudyClass>, LiveData<Exception>> {
        val data = MutableLiveData<StudyClass>()
        val exception = MutableLiveData<Exception>()
        db.collection(COLLECTION_STUDY_CLASS)
            .document(uid)
            .get()
            .addOnSuccessListener { studyClass ->
                if (studyClass.data != null) data.postValue(studyClass.toObject(StudyClass::class.java))
                else exception.postValue(java.lang.Exception("study class uid not found"))
            }
            .addOnFailureListener { ex -> exception.postValue(ex)}
        return Pair(data, exception)
    }

    @Deprecated("Replaced by getItem")
    fun getTaskForm(uid: String) : Pair<LiveData<TaskForm>, LiveData<Exception>> {
        val data = MutableLiveData<TaskForm>()
        val exception = MutableLiveData<Exception>()
        db.collection(COLLECTION_TASK_FORM)
            .document(uid)
            .get()
            .addOnSuccessListener { taskForm ->
                if (taskForm.data != null) data.postValue(taskForm.toObject(TaskForm::class.java))
                else exception.postValue(java.lang.Exception("task form uid not found"))
            }
            .addOnFailureListener { ex -> exception.postValue(ex) }
        return Pair(data, exception)
    }


    @Deprecated("Replaced by getItem")
    fun getResource(uid: String) : Pair<LiveData<Resource>, LiveData<Exception>> {
        val data = MutableLiveData<Resource>()
        val exception = MutableLiveData<Exception>()
        db.collection(COLLECTION_RESOURCE)
            .document(uid)
            .get()
            .addOnSuccessListener { resource ->
                if (resource.data != null) data.postValue(resource.toObject(Resource::class.java))
                else exception.postValue(java.lang.Exception("resource uid not found"))
            }
            .addOnFailureListener { ex -> exception.postValue(ex)}
        return Pair(data, exception)
    }

    @Deprecated("Replaced by getItems")
    fun getAnnouncements() : Pair<LiveData<List<Announcement>> , LiveData<Exception>> {
        val announcements = arrayListOf<Announcement>()
        val data = MutableLiveData<List<Announcement>>()
        val exception = MutableLiveData<Exception>()
        db.collection(COLLECTION_ANNOUNCEMENT)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("Data Announcement", "${document.id} => ${document.data}")
                    announcements.add(document.toObject(Announcement::class.java))
                }
                data.postValue(announcements)
            }
            .addOnFailureListener { exc ->
                 exception.postValue(exc)
            }
        return Pair(data, exception)
    }

    fun addResource(resource: Resource, teacher: Teacher?): Pair<LiveData<Boolean>, LiveData<Exception>> {
        val isSuccess = MutableLiveData<Boolean>()
        val exception = MutableLiveData<Exception>()
        db.collection(COLLECTION_RESOURCE)
            .document(resource.id!!)
            .set(resource)
            .addOnSuccessListener {
                isSuccess.value = true
            }
            .addOnFailureListener {
                exception.value = it
            }

        teacher?.let {
            db.collection(COLLECTION_TEACHER)
                .document(teacher.id!!)
                .set(teacher)
                .addOnSuccessListener {
                    isSuccess.value = true
                }
                .addOnFailureListener {
                    exception.value = it
                }
        }

        return Pair(isSuccess, exception)
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
                        Log.d("123456-", document.id)
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
        return Pair(isSuccess, exception)
    }


    override fun onSuccess(p0: Void?) {
//        TODO("Not yet implemented")
    }

    override fun onFailure(p0: java.lang.Exception) {
//        TODO("Not yet implemented")
    }

}