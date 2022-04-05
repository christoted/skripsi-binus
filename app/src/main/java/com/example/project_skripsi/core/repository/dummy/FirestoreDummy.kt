package com.example.project_skripsi.core.repository.dummy

import android.util.Log
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.repository.FireRepository.Companion.COLLECTION_ADMINISTRATOR
import com.example.project_skripsi.core.repository.FireRepository.Companion.COLLECTION_ANNOUNCEMENT
import com.example.project_skripsi.core.repository.FireRepository.Companion.COLLECTION_PARENT
import com.example.project_skripsi.core.repository.FireRepository.Companion.COLLECTION_RESOURCE
import com.example.project_skripsi.core.repository.FireRepository.Companion.COLLECTION_SCHOOL
import com.example.project_skripsi.core.repository.FireRepository.Companion.COLLECTION_STUDENT
import com.example.project_skripsi.core.repository.FireRepository.Companion.COLLECTION_STUDY_CLASS
import com.example.project_skripsi.core.repository.FireRepository.Companion.COLLECTION_TASK_FORM
import com.example.project_skripsi.core.repository.FireRepository.Companion.COLLECTION_TEACHER
import com.example.project_skripsi.utils.helper.DateHelper
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception
import java.util.*
import kotlin.random.Random

class FirestoreDummy : OnSuccessListener<Any>, OnFailureListener {

    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    private val students = mapOf(
        "P4T9d2CagYdNmhc7xFiGYh3l2oH2" to
                Student("P4T9d2CagYdNmhc7xFiGYh3l2oH2",
                    "123456789",
                    "Luis B",
                    21,
                    "Jalan Timur",
                    "082363007520",
                    18,
                    "laki-laki",
                    "eMsulnik6kEpW0ESKI9V",
                    "ksalsoodapp110Kkqllp",
                    listOf(
                        Payment("Uang SPP",
                            200000,
                            "5271123456",
                            DateHelper.getCurrentDate(),
                            null
                        ),
                    ),
                    listOf(AttendedMeeting(id = "MEET001", subjectName = "Biologi", status = "hadir")),
                    emptyList(),
                    mutableListOf(
                        AssignedTaskForm("HaWuFgmvLAuZYeG5JuVw",
                            "Ujian Tengah Semester",
                            "ujian_tengah_semester",
                            DateHelper.getCurrentDate(),
                            false,
                            "Biologi",
                            95,
                            listOf("Ok Siap Essai")
                        ),
                    ),
                    mutableListOf(
                        AssignedTaskForm("ripyBsBZObBfarZpd085",
                            "Tugas tentang minyak hewani",
                            "tugas",
                            DateHelper.getCurrentDate(),
                            true,
                            "Biologi",
                            70,
                            listOf("Minyak adalah mata uang", 2)
                        ),
                    ),
                    mutableListOf(
                        Achievement(
                            "Juara 1 Lomba Renang",
                            "Diadakan oleh SMA Maju Mundur pada tanggal 15 September 2022"
                        )
                    )
                )
    )

    private val parents = mapOf(
        "Zslqzb1vN1cehlD7TYaQBWHERo72" to
            Parent("Zslqzb1vN1cehlD7TYaQBWHERo72",
                "Arum",
                "0821123123",
                listOf("P4T9d2CagYdNmhc7xFiGYh3l2oH2")
            )
    )


    private val teachers = mapOf(
        "EAwxIDeIQfRWuNHW4P92B6Ko5G53" to
            Teacher("EAwxIDeIQfRWuNHW4P92B6Ko5G53",
                "Devita",
                "0821123123",
                "perempuan",
                "eMsulnik6kEpW0ESKI9V",
                "ksalsoodapp110Kkqllp",
                listOf(
                    TeachingSubject("Biologi",
                        listOf("eMsulnik6kEpW0ESKI9V"))
                ),
                listOf("dxXTXZcrj0yVh8PpzYk2",
                    "feiaZB0ds1rbaWT1g8hJ"
                ),
                mutableListOf("HaWuFgmvLAuZYeG5JuVw"),
                mutableListOf("ripyBsBZObBfarZpd085"),
            )
    )


    private val administrators = mapOf(
        "zriNod2GJUSY1EZpqexfZCLlUwt2" to
            Administrator("zriNod2GJUSY1EZpqexfZCLlUwt2",
                "Admin",
                "0821123123",
            )
    )


    private val announcements = mapOf(
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


    val resources = mapOf(
        "dxXTXZcrj0yVh8PpzYk2" to
            Resource("dxXTXZcrj0yVh8PpzYk2",
                "Pertemuan 1",
                12,
                "recording",
                "<<url>>",
                "Biologi",
                listOf("b1Ty3EisH2bkMlnBWGmp"),
                listOf("eMsulnik6kEpW0ESKI9V")),
        "feiaZB0ds1rbaWT1g8hJ" to
            Resource("feiaZB0ds1rbaWT1g8hJ",
                "Bab 1 Reproduksi",
                12,
                "slide",
                "<<url>>",
                "Biologi",
                listOf(),
                listOf("eMsulnik6kEpW0ESKI9V")),
    )


    private val taskFroms = mutableMapOf(
            "HaWuFgmvLAuZYeG5JuVw" to
                    TaskForm("HaWuFgmvLAuZYeG5JuVw",
                        "Ujian Tengah Semester",
                        12,
                        "ujian_tengah_semester",
                        DateHelper.getCurrentDate(),
                        DateHelper.getCurrentDate(),
                        "Online",
                        "Biologi",
                        listOf(
                            Question("Essai ini",
                                "essai",
                                100,
                                emptyList(),
                                -1
                            )
                        ),
                        listOf("eMsulnik6kEpW0ESKI9V")),
            "ripyBsBZObBfarZpd085" to
                    TaskForm("ripyBsBZObBfarZpd085",
                        "Tugas tentang minyak hewani",
                        12,
                        "tugas",
                        DateHelper.getCurrentDate(),
                        DateHelper.getCurrentDate(),
                        "Online",
                        "Biologi",
                        listOf(
                            Question("Jelaskan maksud minyak hewan ",
                                "essai",
                                50,
                                emptyList(),
                                -1
                            ),
                            Question("Pililah jawaban yang benar. Apakah ikan hiu menghasilkan minyak?",
                                "pilihan berganda",
                                50,
                                listOf("ya",
                                    "tidak",
                                    "kadang-kadang",
                                    "sesekali",
                                    "tidak ada jawaban yang benar"),
                                1
                            )
                        ),
                        listOf("eMsulnik6kEpW0ESKI9V")),
            )



    private val studyClasses = mapOf(
        "eMsulnik6kEpW0ESKI9V" to
            StudyClass("eMsulnik6kEpW0ESKI9V",
                "XII-IPA-3",
                12,
                "EAwxIDeIQfRWuNHW4P92B6Ko5G53",
                "P4T9d2CagYdNmhc7xFiGYh3l2oH2",
                mutableListOf("P4T9d2CagYdNmhc7xFiGYh3l2oH2"),
                mutableListOf(
                    Subject("Biologi",
                        "EAwxIDeIQfRWuNHW4P92B6Ko5G53",
                        mutableListOf(
                            ClassMeeting("MEET001",
                                "Biologi",
                                DateHelper.getCurrentDate(),
                                DateHelper.getCurrentDate(),
                                "online",
                                "berlangsung",
                                "feiaZB0ds1rbaWT1g8hJ",
                                "123",
                            )
                        ),
                        mutableListOf("HaWuFgmvLAuZYeG5JuVw"), // exam
                        mutableListOf("ripyBsBZObBfarZpd085"), // asg
                        mutableListOf("dxXTXZcrj0yVh8PpzYk2",
                            "feiaZB0ds1rbaWT1g8hJ"
                        )
                    ),
                ),
            )
    )

    private val schools = mapOf(
        "ksalsoodapp110Kkqllp" to
                School("ksalsoodapp110Kkqllp",
                    "SMA Methodist - 3",
                    "0811123123",
                    "Jalan Jati",
                )
    )

    init {
        addGeneratedTaskForms()
        upload(COLLECTION_STUDENT, students) // replace
        upload(COLLECTION_PARENT, parents) // replace
        upload(COLLECTION_TEACHER, teachers) // replace
        upload(COLLECTION_ADMINISTRATOR, administrators) // replace
        upload(COLLECTION_STUDY_CLASS, studyClasses) // replace
        upload(COLLECTION_ANNOUNCEMENT, announcements) // replace
        upload(COLLECTION_RESOURCE, resources) // replace
        upload(COLLECTION_TASK_FORM, taskFroms) // replace
        upload(COLLECTION_SCHOOL, schools) // replace
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

    companion object {
        private val ids = listOf("FIS","KIM","BIO","SEJ","GEO","EKO","SOS","ING","IND","MAN","PJK","PKN","SBD")
        private val subjects = listOf("Fisika","Kimia","Biologi","Sejarah","Geografi","Ekonomi",
            "Sosiologi","B.Inggris","B.Indonesia","B.Mandarin","Penjaskes","PKn","Seni Budaya")

        private val startHour = listOf(7,8,9,10,11)
    }

    private fun addGeneratedTaskForms() {
        val newTaskForms : MutableMap<String, TaskForm> = mutableMapOf()
        ids.mapIndexed{ idx, id ->

            if (studyClasses["eMsulnik6kEpW0ESKI9V"]!!
                    .subjects!!.none { subject -> subject.subjectName == subjects[idx] }
            ) {
                studyClasses["eMsulnik6kEpW0ESKI9V"]!!.subjects!!.add(
                    Subject(
                        subjects[idx],
                        "EAwxIDeIQfRWuNHW4P92B6Ko5G53",
                        mutableListOf(),
                        mutableListOf(),
                        mutableListOf(),
                        mutableListOf(),
                    )
                )
            }


            val includeUTS = rnd(0,1) > 0
            if (includeUTS) {
                val taskId = "UTS_${id}"
                val title = "Ujian Tengah Semester"
                val date = getClassTime()
                newTaskForms[taskId] = TaskForm(
                    taskId,
                    title,
                    12,
                    "ujian_tengah_semester",
                    date.first,
                    date.second,
                    if (rnd50to50()) "Online" else "Offline",
                    subjects[idx],
                    listOf(),
                    listOf("eMsulnik6kEpW0ESKI9V")
                )

                studyClasses["eMsulnik6kEpW0ESKI9V"]!!
                    .subjects!!
                    .filter { it.subjectName == subjects[idx] }[0]
                    .classExams!!.add(taskId)

                teachers["EAwxIDeIQfRWuNHW4P92B6Ko5G53"]!!
                    .createdExams!!.add(taskId)

                val isChecked = rnd50to50()
                students["P4T9d2CagYdNmhc7xFiGYh3l2oH2"]!!
                    .assignedExams!!
                    .add(AssignedTaskForm(taskId,
                        title,
                        "ujian_tengah_semester",
                        date.first,
                        isChecked,
                        subjects[idx],
                        if (isChecked) rnd(0,100) else 0,
                        listOf()
                    ))
            }

            val includeUAS = rnd(0,1) > 0
            if (includeUAS) {
                val taskId = "UAS_${id}"
                val title = "Ujian Akhir Semester"
                val date = getClassTime()
                newTaskForms[taskId] = TaskForm(
                    taskId,
                    title,
                    12,
                    "ujian_akhir_semester",
                    date.first,
                    date.second,
                    if (rnd50to50()) "Online" else "Offline",
                    subjects[idx],
                    listOf(),
                    listOf("eMsulnik6kEpW0ESKI9V")
                )

                studyClasses["eMsulnik6kEpW0ESKI9V"]!!
                    .subjects!!
                    .filter { it.subjectName == subjects[idx] }[0]
                    .classExams!!.add(taskId)

                teachers["EAwxIDeIQfRWuNHW4P92B6Ko5G53"]!!
                    .createdExams!!.add(taskId)

                val isChecked = rnd50to50()
                students["P4T9d2CagYdNmhc7xFiGYh3l2oH2"]!!
                    .assignedExams!!
                    .add(AssignedTaskForm(taskId,
                        title,
                        "ujian_akhir_semester",
                        date.first,
                        isChecked,
                        subjects[idx],
                        if (isChecked) rnd(0,100) else 0,
                        listOf()
                    ))
            }


            val numAsg = rnd(0, 5)
            for (i in 1..numAsg) {
                val taskId = "TGS_${id}_0${i}"
                val title = "Tugas ${subjects[idx]} 0${i}"
                val date = getClassTime()
                newTaskForms[taskId] = TaskForm(
                    taskId,
                    title,
                    12,
                    "tugas",
                    date.first,
                    date.second,
                    if (rnd50to50()) "Online" else "Offline",
                    subjects[idx],
                    listOf(),
                    listOf("eMsulnik6kEpW0ESKI9V")
                )

                studyClasses["eMsulnik6kEpW0ESKI9V"]!!
                    .subjects!!
                    .filter { it.subjectName == subjects[idx] }[0]
                    .classAssignments!!.add(taskId)

                teachers["EAwxIDeIQfRWuNHW4P92B6Ko5G53"]!!
                    .createdAssignments!!.add(taskId)

                val isChecked = rnd50to50()
                students["P4T9d2CagYdNmhc7xFiGYh3l2oH2"]!!
                    .assignedAssignments!!
                    .add(AssignedTaskForm(taskId,
                        title,
                        "tugas",
                        date.first,
                        isChecked,
                        subjects[idx],
                        if (isChecked) rnd(0,100) else 0,
                        listOf()
                    ))
            }
        }
        taskFroms.putAll(newTaskForms)

    }

    private fun getClassTime() : Pair<Date, Date> {
        val day = rnd(1,30)
        val hour = startHour.random()
        val cal = Calendar.getInstance()
        cal.set(2022, 2, day, hour, 0)
        val cal2 = Calendar.getInstance()
        cal2.set(2022, 2, day, hour, 40)
        return Pair(cal.time, cal2.time)
    }

    private fun rnd50to50() = rnd(0,1) > 0

    private fun rnd(low: Int, high: Int) = Random.nextInt(low, high+1)

}