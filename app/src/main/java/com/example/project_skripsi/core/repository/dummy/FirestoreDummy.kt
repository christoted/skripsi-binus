package com.example.project_skripsi.core.repository.dummy

import android.util.Log
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.repository.FireRepository.Companion.COLLECTION_ADMINISTRATOR
import com.example.project_skripsi.core.repository.FireRepository.Companion.COLLECTION_ANNOUNCEMENT
import com.example.project_skripsi.core.repository.FireRepository.Companion.COLLECTION_PARENT
import com.example.project_skripsi.core.repository.FireRepository.Companion.COLLECTION_RESOURCE
import com.example.project_skripsi.core.repository.FireRepository.Companion.COLLECTION_STUDENT
import com.example.project_skripsi.core.repository.FireRepository.Companion.COLLECTION_STUDY_CLASS
import com.example.project_skripsi.core.repository.FireRepository.Companion.COLLECTION_TASK_FORM
import com.example.project_skripsi.core.repository.FireRepository.Companion.COLLECTION_TEACHER
import com.example.project_skripsi.utils.helper.DateHelper
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class FirestoreDummy : OnSuccessListener<Any>, OnFailureListener {

    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    private fun getStudents() = mapOf(
        "P4T9d2CagYdNmhc7xFiGYh3l2oH2" to
                Student("P4T9d2CagYdNmhc7xFiGYh3l2oH2",
                    "123456789",
                    "Luis B",
                    "082363007520",
                    18,
                    "laki-laki",
                    "eMsulnik6kEpW0ESKI9V",
                    listOf(
                        Payment("Uang SPP",
                            200000,
                            "5271123456",
                            DateHelper.getCurrentDate(),
                            null
                        ),
                    ),
                    listOf("MEET001"),
                    emptyList(),
                    listOf(
                        AssignedTaskForm("HaWuFgmvLAuZYeG5JuVw",
                            false,
                            0,
                            listOf("Ok Siap Essai")
                        ),
                    ),
                    listOf(
                        AssignedTaskForm("ripyBsBZObBfarZpd085",
                            true,
                            70,
                            listOf("Minyak adalah mata uang",
                                2
                            )
                        ),
                        AssignedTaskForm("xaxaxaxa", // ini juga
                            true,
                            70,
                            listOf()
                        ),

                    )
                )
    )

    private fun getParents() = mapOf(
        "Zslqzb1vN1cehlD7TYaQBWHERo72" to
            Parent("Zslqzb1vN1cehlD7TYaQBWHERo72",
                "Arum",
                "0821123123",
                listOf("P4T9d2CagYdNmhc7xFiGYh3l2oH2")
            )
    )


    private fun getTeachers() = mapOf(
        "EAwxIDeIQfRWuNHW4P92B6Ko5G53" to
            Teacher("EAwxIDeIQfRWuNHW4P92B6Ko5G53",
                "Devita",
                "0821123123",
                "perempuan",
                "eMsulnik6kEpW0ESKI9V",
                listOf(
                    TeachingSubject("Biologi",
                        listOf("eMsulnik6kEpW0ESKI9V"))
                ),
                listOf("dxXTXZcrj0yVh8PpzYk2",
                    "feiaZB0ds1rbaWT1g8hJ"
                ),
                listOf("HaWuFgmvLAuZYeG5JuVw",
                    "ripyBsBZObBfarZpd085"
                )
            )
    )


    private fun getAdministrators() = mapOf(
        "zriNod2GJUSY1EZpqexfZCLlUwt2" to
            Administrator("zriNod2GJUSY1EZpqexfZCLlUwt2",
                "Admin",
                "0821123123",
            )
    )


    private fun getAnnouncements() = mapOf(
        "1UYvc8ji8ip4Zoejke9d" to
            Announcement("1UYvc8ji8ip4Zoejke9d",
                "Lomba Sekolah 2",
                "Kategori lomba dibagi menjadi 4 cabang",
                DateHelper.getCurrentDate()),
        "5vlpa3hwTPQcfHZ2L66z" to
            Announcement("5vlpa3hwTPQcfHZ2L66z",
                "Pengumuman Sekolah 2",
                "Pembelajaran jarak jauh dikarenakan covid 19",
                DateHelper.getCurrentDate()),
    )


    private fun getResources() = mapOf(
        "dxXTXZcrj0yVh8PpzYk2" to
            Resource("dxXTXZcrj0yVh8PpzYk2",
                "Pertemuan 1",
                "recording",
                "<<url>>",
                "Biologi",
                listOf("b1Ty3EisH2bkMlnBWGmp"),
                listOf("eMsulnik6kEpW0ESKI9V")),
        "feiaZB0ds1rbaWT1g8hJ" to
            Resource("feiaZB0ds1rbaWT1g8hJ",
                "Bab 1 Reproduksi",
                "slide",
                "<<url>>",
                "Biologi",
                listOf(),
                listOf("eMsulnik6kEpW0ESKI9V")),
    )


    private fun getTaskForms() = mapOf(
            "HaWuFgmvLAuZYeG5JuVw" to
                TaskForm("HaWuFgmvLAuZYeG5JuVw",
                    "Ujian Tengah Semester 2",
                "ujian tengah semester",
                DateHelper.getCurrentDate(),
                DateHelper.getCurrentDate(),
                "Biologi",
                listOf(
                    Question("Essai ini",
                        "essai",
                        emptyList(),
                        -1
                    )
                ),
                listOf("eMsulnik6kEpW0ESKI9V")),

            "ripyBsBZObBfarZpd085" to
                TaskForm("ripyBsBZObBfarZpd085",
                    "Tugas tentang minyak hewani",
                    "tugas",
                    DateHelper.getCurrentDate(),
                    DateHelper.getCurrentDate(),
                    "Biologi",
                    listOf(
                        Question("Jelaskan maksud minyak hewan ",
                            "essai",
                            emptyList(),
                            -1
                        ),
                        Question("Pililah jawaban yang benar. Apakah ikan hiu menghasilkan minyak?",
                            "pilihan berganda",
                            listOf("ya",
                                "tidak",
                                "kadang-kadang",
                                "sesekali",
                                "tidak ada jawaban yang benar"),
                            1
                        )
                    ),
                    listOf("eMsulnik6kEpW0ESKI9V")),

        "xaxaxaxa" to
                TaskForm("xaxaxaxa",
                    "bebas", // sesuaikan
                    "tugas", // sesuaikan  asg / exam
                    DateHelper.getCurrentDate(),
                    DateHelper.getCurrentDate(),
                    "Biologi", // sesuaikan
                    listOf(),
                    listOf("eMsulnik6kEpW0ESKI9V")),
    )

    private fun getClasses() = mapOf(
        "eMsulnik6kEpW0ESKI9V" to
            StudyClass("eMsulnik6kEpW0ESKI9V",
                "XII-IPA-2",
                "EAwxIDeIQfRWuNHW4P92B6Ko5G53",
                "P4T9d2CagYdNmhc7xFiGYh3l2oH2",
                listOf("P4T9d2CagYdNmhc7xFiGYh3l2oH2"),
                listOf(
                    Subject("Biologi",
                        "EAwxIDeIQfRWuNHW4P92B6Ko5G53",
                        listOf(
                            ClassMeeting("MEET001",
                                DateHelper.getCurrentDate(),
                                DateHelper.getCurrentDate(),
                                "online",
                                "berlangsung",
                                "feiaZB0ds1rbaWT1g8hJ",
                            )
                        ),
                        listOf("HaWuFgmvLAuZYeG5JuVw"), // exam
                        listOf("ripyBsBZObBfarZpd085","xaxaxaxa"), // asg
                        listOf("dxXTXZcrj0yVh8PpzYk2",
                            "feiaZB0ds1rbaWT1g8hJ"
                        )
                    )
                ),
            )
    )

    init {
        upload(COLLECTION_STUDENT, getStudents()) // replace
        upload(COLLECTION_PARENT, getParents()) // replace
        upload(COLLECTION_TEACHER, getTeachers()) // replace
        upload(COLLECTION_ADMINISTRATOR, getAdministrators()) // replace
        upload(COLLECTION_STUDY_CLASS, getClasses()) // replace
        upload(COLLECTION_ANNOUNCEMENT, getAnnouncements()) // replace
        upload(COLLECTION_RESOURCE, getResources()) // replace
        upload(COLLECTION_TASK_FORM, getTaskForms()) // replace
    }

    private fun upload(collection: String, items: Map<String, Any>) {
        for (item in items) {
            db.collection(collection)
                .document(item.key)
                .set(item.value)
                .addOnSuccessListener(this)
                .addOnFailureListener(this)
        }
    }

    override fun onSuccess(p0: Any?) {
        Log.d("12345-FirestoreDummy", "ok")
    }

    override fun onFailure(p0: Exception) {
        Log.d("12345-FirestoreDummy", "failed $p0")
    }

}