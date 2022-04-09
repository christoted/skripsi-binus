package com.example.project_skripsi.core.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.project_skripsi.core.model.firestore.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore


class FireRepository : OnSuccessListener<Void>, OnFailureListener {

    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    companion object {
        var instance = FireRepository()

        const val COLLECTION_STUDENT = "students"
        const val COLLECTION_TEACHER = "teachers"
        const val COLLECTION_PARENT = "parents"
        const val COLLECTION_ADMINISTRATOR = "administrators"
        const val COLLECTION_STUDY_CLASS = "study_classes"
        const val COLLECTION_TASK_FORM = "task_forms"
        const val COLLECTION_RESOURCE = "resources"
        const val COLLECTION_ANNOUNCEMENT = "announcements"
        const val COLLECTION_SCHOOL = "schools"
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

    fun addTaskForm(taskForm: TaskForm, teacher: Teacher) {

        db.collection(COLLECTION_TASK_FORM)
            .document(taskForm.id!!)
            .set(taskForm)
            .addOnSuccessListener(this)
            .addOnFailureListener(this)
        db.collection(COLLECTION_TEACHER)
            .document(teacher.id!!)
            .set(teacher)
            .addOnSuccessListener(this)
            .addOnFailureListener(this)
    }

    override fun onSuccess(p0: Void?) {
//        TODO("Not yet implemented")
    }

    override fun onFailure(p0: java.lang.Exception) {
//        TODO("Not yet implemented")
    }

}