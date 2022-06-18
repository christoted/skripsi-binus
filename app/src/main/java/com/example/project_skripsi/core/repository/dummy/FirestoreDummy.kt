package com.example.project_skripsi.core.repository.dummy

import android.util.Log
import com.example.project_skripsi.core.model.firestore.*
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
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
        "KmMDuXr945X8eGnCkFLFe1sP8TH3" to
                Student("KmMDuXr945X8eGnCkFLFe1sP8TH3",
                    "123456789",
                    "Luis B",
                    "https://media-exp1.licdn.com/dms/image/C5603AQErx4vpP4mHkA/profile-displayphoto-shrink_200_200/0/1625740341499?e=1657756800&v=beta&t=KQxAFRTMH4sLuPbY0yN3-xE08u4MPokPWygzMtPRY1I",
                    21,
                    "Jalan Timur",
                    "082363007520",
                    18,
                    "laki-laki",
                    "0JGXQLl7R9eryNGbzptA3923JKT2",
                    "eMsulnik6kEpW0ESKI9V",
                    "ksalsoodapp110Kkqllp",
                    listOf(
                        Payment("Uang SPP",
                            200000,
                            "5271123456",
                            DateHelper.getCurrentTime(),
                            null
                        ),
                        Payment("Uang SPP2",
                            200000,
                            "5271123456",
                            DateHelper.getCurrentTime(),
                            DateHelper.getCurrentTime()
                        ),
                        Payment("Uang SPP3",
                            200000,
                            "5271123456",
                            DateHelper.getTomorrow(),
                            null
                        ),
                    ),
                    listOf(
                        AttendedMeeting(
                            "MEET001",
                            "hadir",
                            DateHelper.getCurrentTime(),
                            DateHelper.getCurrentTime(),
                            "Biologi",

                            )
                    ),
                    emptyList(),
                    mutableListOf(
                        AssignedTaskForm("HaWuFgmvLAuZYeG5JuVw",
                            "Ujian Tengah Semester",
                            "ujian_tengah_semester",
                            DateHelper.getCurrentTime(),
                            DateHelper.getCurrentTime(),
                            true,
                            false,
                            "Biologi",
                            95,
                            listOf(
                                Answer("Ok Siap Essai", 0)
                            )
                        ),
                    ),
                    mutableListOf(
                        AssignedTaskForm("ripyBsBZObBfarZpd085",
                            "Tugas tentang minyak hewani",
                            "tugas",
                            DateHelper.getCurrentTime(),
                            DateHelper.getCurrentTime(),
                            true,
                            true,
                            "Biologi",
                            70,
                            listOf(
                                Answer("Minyak adalah mata uang", 0),
                                Answer("2", 0)
                            )
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
        "0JGXQLl7R9eryNGbzptA3923JKT2" to
                Parent("0JGXQLl7R9eryNGbzptA3923JKT2",
                    "Arum",
                    "0821123123",
                    listOf("KmMDuXr945X8eGnCkFLFe1sP8TH3",
                        "KmMDuXr945X8eGnCkFLFe1sP8TH3",
                        "KmMDuXr945X8eGnCkFLFe1sP8TH3",
                        "KmMDuXr945X8eGnCkFLFe1sP8TH3")
                )
    )


    private val teachers = mapOf(
        "fUepYcW4j8Z2M8lX3L78ddEprIX2" to
                Teacher("fUepYcW4j8Z2M8lX3L78ddEprIX2",
                    "Devita",
                    "https://www.google.com/search?q=devita+setyaningrum&sxsrf=ALiCzsZvO6qPVHPXAV21dASGyM69ZfwLxA:1654423545740&source=lnms&tbm=isch&sa=X&ved=2ahUKEwjqwYH-h5b4AhVD7XMBHcCRC-cQ_AUoAXoECAEQAw&biw=1536&bih=784&dpr=1.25#imgrc=cWTYUVfkb5WQRM",
                    "0821123123",
                    "perempuan",
                    "eMsulnik6kEpW0ESKI9V",
                    "ksalsoodapp110Kkqllp",
                    mutableListOf(
                        TeachingGroup(
                            "Biologi",
                            12,
                            listOf("eMsulnik6kEpW0ESKI9V"),
                            mutableListOf("dxXTXZcrj0yVh8PpzYk2", "feiaZB0ds1rbaWT1g8hJ", "RS1201", "RS1101", "RF1201"),
                            mutableListOf("HaWuFgmvLAuZYeG5JuVw"),
                            mutableListOf("ripyBsBZObBfarZpd085")
                        )
                    ),
                    mutableListOf(
                        Payment(
                            title = "Pembayaran SPP Genap",
                            nominal = 2000000,
                            accountNumber = "521812312",
                            paymentDate = DateHelper.getCurrentTime(),
                            paymentDeadline = DateHelper.getCurrentTime()
                        )
                    )
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
                    DateHelper.getCurrentTime()),
        "5vlpa3hwTPQcfHZ2L661" to
                Announcement("5vlpa3hwTPQcfHZ2L66z",
                    "Pengumuman Sekolah 2",
                    "Pembelajaran jarak jauh dikarenakan covid 19",
                    DateHelper.getCurrentTime()),
        "5vlpa3hwTPQcfHZ2L662" to
                Announcement("5vlpa3hwTPQcfHZ2L66z",
                    "Pengumuman Sekolah 2",
                    "Pembelajaran jarak jauh dikarenakan covid 19",
                    DateHelper.getCurrentTime()),
        "5vlpa3hwTPQcfHZ2L663" to
                Announcement("5vlpa3hwTPQcfHZ2L66z",
                    "Pengumuman Sekolah 2",
                    "Pembelajaran jarak jauh dikarenakan covid 19",
                    DateHelper.getCurrentTime()),
        "5vlpa3hwTPQcfHZ2L664" to
                Announcement("5vlpa3hwTPQcfHZ2L66z",
                    "Pengumuman Sekolah 2",
                    "Pembelajaran jarak jauh dikarenakan covid 19",
                    DateHelper.getCurrentTime()),
        "5vlpa3hwTPQcfHZ2L665" to
                Announcement("5vlpa3hwTPQcfHZ2L66z",
                    "Pengumuman Sekolah 2",
                    "Pembelajaran jarak jauh dikarenakan covid 19",
                    DateHelper.getCurrentTime()),
        "5vlpa3hwTPQcfHZ2L666" to
                Announcement("5vlpa3hwTPQcfHZ2L66z",
                    "Pengumuman Sekolah 2",
                    "Pembelajaran jarak jauh dikarenakan covid 19",
                    DateHelper.getCurrentTime()),
        "5vlpa3hwTPQcfHZ2L667" to
                Announcement("5vlpa3hwTPQcfHZ2L66z",
                    "Pengumuman Sekolah 2",
                    "Pembelajaran jarak jauh dikarenakan covid 19",
                    DateHelper.getCurrentTime()),
        "5vlpa3hwTPQcfHZ2L668" to
                Announcement("5vlpa3hwTPQcfHZ2L66z",
                    "Pengumuman Sekolah 2",
                    "Pembelajaran jarak jauh dikarenakan covid 19",
                    DateHelper.getCurrentTime()),
        "5vlpa3hwTPQcfHZ2L669" to
                Announcement("5vlpa3hwTPQcfHZ2L66z",
                    "Pengumuman Sekolah 2",
                    "Pembelajaran jarak jauh dikarenakan covid 19",
                    DateHelper.getCurrentTime()),
        "5vlpa3hwTPQcfHZ2L660" to
                Announcement("5vlpa3hwTPQcfHZ2L66z",
                    "Pengumuman Sekolah 2",
                    "Pembelajaran jarak jauh dikarenakan covid 19",
                    DateHelper.getCurrentTime()),
        "5vlpa3hwTPQcfHZ2L66a" to
                Announcement("5vlpa3hwTPQcfHZ2L66z",
                    "Pengumuman Sekolah 2",
                    "Pembelajaran jarak jauh dikarenakan covid 19",
                    DateHelper.getCurrentTime()),
        "5vlpa3hwTPQcfHZ2L66b" to
                Announcement("5vlpa3hwTPQcfHZ2L66z",
                    "Pengumuman Sekolah 2",
                    "Pembelajaran jarak jauh dikarenakan covid 19",
                    DateHelper.getCurrentTime()),
        "5vlpa3hwTPQcfHZ2L66v" to
                Announcement("5vlpa3hwTPQcfHZ2L66z",
                    "Pengumuman Sekolah 2",
                    "Pembelajaran jarak jauh dikarenakan covid 19",
                    DateHelper.getCurrentTime()),
        "5vlpa3hwTPQcfHZ2L66c" to
                Announcement("5vlpa3hwTPQcfHZ2L66z",
                    "Pengumuman Sekolah 2",
                    "Pembelajaran jarak jauh dikarenakan covid 19",
                    DateHelper.getCurrentTime()),
    )


    val resources = mapOf(
        "dxXTXZcrj0yVh8PpzYk2" to
                Resource("dxXTXZcrj0yVh8PpzYk2",
                    "Pertemuan 1",
                    12,
                    1,
                    "<<url>>",
                    "Biologi",
                    listOf("b1Ty3EisH2bkMlnBWGmp"),
                    listOf("eMsulnik6kEpW0ESKI9V")),
        "feiaZB0ds1rbaWT1g8hJ" to
                Resource("feiaZB0ds1rbaWT1g8hJ",
                    "Bab 1 Reproduksi",
                    12,
                    2,
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
                    DateHelper.getCurrentTime(),
                    DateHelper.getCurrentTime(),
                    "Online",
                    "Biologi",
                    listOf(
                        Question("Essai ini",
                            "essai",
                            100,
                            emptyList(),
                            ""
                        )
                    ),
                    listOf("eMsulnik6kEpW0ESKI9V"),
                    emptyList(),
                    emptyList(),
                    true
                ),
        "ripyBsBZObBfarZpd085" to
                TaskForm("ripyBsBZObBfarZpd085",
                    "Tugas tentang minyak hewani",
                    12,
                    "tugas",
                    DateHelper.getCurrentTime(),
                    DateHelper.getCurrentTime(),
                    "Online",
                    "Biologi",
                    listOf(
                        Question("Jelaskan maksud minyak hewan ",
                            "essai",
                            50,
                            emptyList(),
                            ""
                        ),
                        Question("Pililah jawaban yang benar. Apakah ikan hiu menghasilkan minyak?",
                            "pilihan berganda",
                            50,
                            listOf("ya",
                                "tidak",
                                "kadang-kadang",
                                "sesekali",
                                "tidak ada jawaban yang benar"),
                            "1"
                        )
                    ),
                    listOf("eMsulnik6kEpW0ESKI9V"),
                    emptyList(),
                    emptyList(),
                    true
                ),
    )



    private val studyClasses = mapOf(
        "eMsulnik6kEpW0ESKI9V" to
                StudyClass("eMsulnik6kEpW0ESKI9V",
                    "XII-IPA-3",
                    12,
                    "fUepYcW4j8Z2M8lX3L78ddEprIX2",
                    "KmMDuXr945X8eGnCkFLFe1sP8TH3",
                    mutableListOf("KmMDuXr945X8eGnCkFLFe1sP8TH3"),
                    mutableListOf(
                        Subject("Biologi",
                            "fUepYcW4j8Z2M8lX3L78ddEprIX2",
                            mutableListOf(
                                ClassMeeting("MEET001",
                                    "Biologi",
                                    DateHelper.getCurrentTime(),
                                    DateHelper.getCurrentTime(),
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
//        addGeneratedTaskForms()
//        upload(COLLECTION_STUDENT, students) // replace
//        upload(COLLECTION_PARENT, parents) // replace
//        upload(COLLECTION_TEACHER, teachers) // replace
//        upload(COLLECTION_ADMINISTRATOR, administrators) // replace
//        upload(COLLECTION_STUDY_CLASS, studyClasses) // replace
//        upload(COLLECTION_ANNOUNCEMENT, announcements) // replace
//        upload(COLLECTION_RESOURCE, resources) // replace
//        upload(COLLECTION_TASK_FORM, taskFroms) // replace
//        upload(COLLECTION_SCHOOL, schools) // replace

//        checkNull()

    }

    private fun checkNull() {
//        FireRepository.inst.getAllItems<Administrator>().first.observeOnce{ ls ->
//            ls.map { if (it.toString().contains("null")) Log.d("12345-FirestoreDummyAdministrator", it.toString()) }
//        }
        FireRepository.inst.getAllItems<Announcement>().first.observeOnce{ ls ->
            ls.map { if (it.toString().contains("null")) Log.d("12345-FirestoreDummyAnnouncement", it.toString()) }
        }
        FireRepository.inst.getAllItems<Parent>().first.observeOnce{ ls ->
            ls.map { if (it.toString().contains("null")) Log.d("12345-FirestoreDummyParent", it.toString()) }
        }
//        FireRepository.inst.getAllItems<Resource>().first.observeOnce{ ls ->
//            ls.map { if (it.toString().contains("null")) Log.d("12345-FirestoreDummyResource", it.toString()) }
//        }
        FireRepository.inst.getAllItems<School>().first.observeOnce{ ls ->
            ls.map { if (it.toString().contains("null")) Log.d("12345-FirestoreDummySchool", it.toString()) }
        }
        FireRepository.inst.getAllItems<Student>().first.observeOnce{ ls ->
            ls.map { if (it.toString().contains("null")) Log.d("12345-FirestoreDummyStudent", it.toString()) }
        }
        FireRepository.inst.getAllItems<StudyClass>().first.observeOnce{ ls ->
            ls.map { if (it.toString().contains("null")) Log.d("12345-FirestoreDummyStudyClass", it.toString()) }
        }
//        FireRepository.inst.getAllItems<TaskForm>().first.observeOnce{ ls ->
//            ls.map { if (it.toString().contains("null")) Log.d("12345-FirestoreDummyTaskForm", it.toString()) }
//        }
        FireRepository.inst.getAllItems<Teacher>().first.observeOnce{ ls ->
            ls.map { if (it.toString().contains("null")) Log.d("12345-FirestoreDummyTeacher", it.toString()) }
        }

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
        private const val COLLECTION_ADMINISTRATOR = "administrators"
        private const val COLLECTION_TASK_FORM = "task_forms"
        private const val COLLECTION_RESOURCE = "resources"
        private const val COLLECTION_STUDENT = "students"
        private const val COLLECTION_TEACHER = "teachers"
        private const val COLLECTION_PARENT = "parents"
        private const val COLLECTION_STUDY_CLASS = "study_classes"
        private const val COLLECTION_ANNOUNCEMENT = "announcements"
        private const val COLLECTION_SCHOOL = "schools"

        private val ids = listOf("FIS","KIM","BIO","SEJ","GEO","EKO","SOS","ING","IND","MAN","PJK","PKN","SBD")
        private val subjects = listOf("Fisika","Kimia","Biologi","Sejarah","Geografi","Ekonomi",
            "Sosiologi","B.Inggris","B.Indonesia","B.Mandarin","Penjaskes","PKn","Seni Budaya")

        private val startHour = listOf(7,8,9,10,11)
    }

    private fun addGeneratedTaskForms() {
        val newTaskForms : MutableMap<String, TaskForm> = mutableMapOf()
        ids.mapIndexed{ idx, id ->

            val gradeLevel = 12

            if (studyClasses["eMsulnik6kEpW0ESKI9V"]!!
                    .subjects!!.none { subject -> subject.subjectName == subjects[idx] }
            ) {
                studyClasses["eMsulnik6kEpW0ESKI9V"]!!.subjects!!.add(
                    Subject(
                        subjects[idx],
                        "fUepYcW4j8Z2M8lX3L78ddEprIX2",
                        mutableListOf(),
                        mutableListOf(),
                        mutableListOf(),
                        mutableListOf(),
                    )
                )
            }

            if (teachers["fUepYcW4j8Z2M8lX3L78ddEprIX2"]!!
                    .teachingGroups!!.none { it.gradeLevel == gradeLevel && it.subjectName == subjects[idx] }
            ) {
                teachers["fUepYcW4j8Z2M8lX3L78ddEprIX2"]!!.teachingGroups!!.add(
                    TeachingGroup(
                        subjects[idx],
                        gradeLevel,
                        mutableListOf("eMsulnik6kEpW0ESKI9V"),
                        mutableListOf(),
                        mutableListOf(),
                        mutableListOf(),
                    )
                )
            }

            val includeUTS = rnd50to50()
            if (includeUTS) {
                val taskId = "UTS_${id}"
                val title = "Ujian Tengah Semester"
                val date = getClassTime()
                newTaskForms[taskId] = TaskForm(
                    taskId,
                    title,
                    gradeLevel,
                    "ujian_tengah_semester",
                    date.first,
                    date.second,
                    if (rnd50to50()) "Online" else "Offline",
                    subjects[idx],
                    listOf(),
                    listOf("eMsulnik6kEpW0ESKI9V"),
                    emptyList(),
                    emptyList(),
                    true
                )

                studyClasses["eMsulnik6kEpW0ESKI9V"]!!
                    .subjects!!
                    .filter { it.subjectName == subjects[idx] }[0]
                    .classExams!!.add(taskId)

                teachers["fUepYcW4j8Z2M8lX3L78ddEprIX2"]!!
                    .teachingGroups!!
                    .filter { it.gradeLevel == gradeLevel && it.subjectName == subjects[idx] }[0]
                    .createdExams!!.add(taskId)

                val isChecked = rnd50to50()
                students["KmMDuXr945X8eGnCkFLFe1sP8TH3"]!!
                    .assignedExams!!
                    .add(AssignedTaskForm(taskId,
                        title,
                        "ujian_tengah_semester",
                        date.first,
                        date.second,
                        isChecked,
                        isChecked,
                        subjects[idx],
                        if (isChecked) rnd(0,100) else 0,
                        listOf()
                    ))
            }

            val includeUAS = rnd50to50()
            if (includeUAS) {
                val taskId = "UAS_${id}"
                val title = "Ujian Akhir Semester"
                val date = getClassTime()
                newTaskForms[taskId] = TaskForm(
                    taskId,
                    title,
                    gradeLevel,
                    "ujian_akhir_semester",
                    date.first,
                    date.second,
                    if (rnd50to50()) "Online" else "Offline",
                    subjects[idx],
                    listOf(),
                    listOf("eMsulnik6kEpW0ESKI9V"),
                    emptyList(),
                    emptyList(),
                    true
                )

                studyClasses["eMsulnik6kEpW0ESKI9V"]!!
                    .subjects!!
                    .filter { it.subjectName == subjects[idx] }[0]
                    .classExams!!.add(taskId)

                teachers["fUepYcW4j8Z2M8lX3L78ddEprIX2"]!!
                    .teachingGroups!!
                    .filter { it.gradeLevel == gradeLevel && it.subjectName == subjects[idx] }[0]
                    .createdExams!!.add(taskId)

                val isChecked = rnd50to50()
                students["KmMDuXr945X8eGnCkFLFe1sP8TH3"]!!
                    .assignedExams!!
                    .add(AssignedTaskForm(taskId,
                        title,
                        "ujian_akhir_semester",
                        date.first,
                        date.second,
                        isChecked,
                        isChecked,
                        subjects[idx],
                        if (isChecked) rnd(0,100) else 0,
                        listOf(),
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
                    gradeLevel,
                    "tugas",
                    date.first,
                    date.second,
                    if (rnd50to50()) "Online" else "Offline",
                    subjects[idx],
                    listOf(),
                    listOf("eMsulnik6kEpW0ESKI9V"),
                    emptyList(),
                    emptyList(),
                    true
                )

                studyClasses["eMsulnik6kEpW0ESKI9V"]!!
                    .subjects!!
                    .filter { it.subjectName == subjects[idx] }[0]
                    .classAssignments!!.add(taskId)

                teachers["fUepYcW4j8Z2M8lX3L78ddEprIX2"]!!
                    .teachingGroups!!
                    .filter { it.gradeLevel == gradeLevel && it.subjectName == subjects[idx] }[0]
                    .createdAssignments!!.add(taskId)

                val isChecked = rnd50to50()
                students["KmMDuXr945X8eGnCkFLFe1sP8TH3"]!!
                    .assignedAssignments!!
                    .add(AssignedTaskForm(taskId,
                        title,
                        "tugas",
                        date.first,
                        date.second,
                        isChecked,
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