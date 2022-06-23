package com.example.project_skripsi.module.student.main.studyclass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.model.firestore.StudyClass
import com.example.project_skripsi.core.model.firestore.Subject
import com.example.project_skripsi.core.model.firestore.Teacher
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import kotlin.math.min

class StClassViewModel : ViewModel() {

    companion object {
        val mapOfSubjectImage = mapOf(
            "Agama" to R.drawable.mapel_agama,
            "B.Indonesia" to R.drawable.mapel_bahasa_indonesia,
            "B.Inggris" to R.drawable.mapel_bahasa_inggris,
            "B.Jawa" to R.drawable.mapel_bahasa_jawa,
            "B.Mandarin" to R.drawable.mapel_bahasa_mandarin,
            "Biologi" to R.drawable.mapel_biologi,
            "Ekonomi" to R.drawable.mapel_ekonomi,
            "Fisika" to R.drawable.mapel_fisika,
            "Geografi" to R.drawable.mapel_geografi,
            "Kimia" to R.drawable.mapel_kimia,
            "Komputer" to R.drawable.mapel_komputer,
            "Matematika" to R.drawable.mapel_matematika,
            "Penjaskes" to R.drawable.mapel_penjaskes,
            "PKn" to R.drawable.mapel_pkn,
            "Sejarah" to R.drawable.mapel_sejarah,
            "Seni Budaya" to R.drawable.mapel_seni_budaya,
            "Sosiologi" to R.drawable.mapel_sosiologi,
        )
    }

    private val _studyClass = MutableLiveData<StudyClass>()
    val studyClass: LiveData<StudyClass> = _studyClass

    private val _teacher = MutableLiveData<Teacher>()
    val teacher: LiveData<Teacher> = _teacher

    private val _classChief = MutableLiveData<Student>()
    val classChief: LiveData<Student> = _classChief

    init {
        loadStudent(AuthRepository.inst.getCurrentUser().uid)
    }

    private fun loadStudent(uid: String) {
        FireRepository.inst.getItem<Student>(uid).first.observeOnce { student ->
            student.studyClass?.let { loadStudyClass(it) }
        }
    }

    private fun loadStudyClass(uid: String) {
        FireRepository.inst.getItem<StudyClass>(uid).first.observeOnce { studyClass ->
            with(studyClass) {
                _studyClass.postValue(this)
                homeroomTeacher?.let { loadTeacher(it) }
                classChief?.let { loadClassChief(it) }
            }
        }
    }

    private fun loadTeacher(uid: String) {
        FireRepository.inst.getItem<Teacher>(uid).first.observeOnce { _teacher.postValue(it) }
    }

    private fun loadClassChief(uid: String) {
        FireRepository.inst.getItem<Student>(uid).first.observeOnce { _classChief.postValue(it) }
    }


    fun getSubjects(page: Int): List<Subject> {
        val startIdx = page * 8
        val endIdx = min(startIdx + 8, studyClass.value?.subjects?.size ?: 0)
        return studyClass.value?.subjects?.subList(startIdx, endIdx) ?: emptyList()
    }

    fun getSubjectPageCount(): Int {
        val subjects = studyClass.value?.subjects?.size ?: 0
        return (subjects + 8 - 1) / 8
    }


}