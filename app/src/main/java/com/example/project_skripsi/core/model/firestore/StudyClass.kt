package com.example.project_skripsi.core.model.firestore

import com.example.project_skripsi.core.model.local.HomeSectionData
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import java.util.*

@IgnoreExtraProperties
data class StudyClass(

    @DocumentId
    val id: String? = null,

    val name: String? = null,

    @get: PropertyName("grade_level")
    @set: PropertyName("grade_level")
    var gradeLevel: Int? = null,

    @get: PropertyName("homeroom_teacher")
    @set: PropertyName("homeroom_teacher")
    var homeroomTeacher: String? = null,

    @get: PropertyName("class_chief")
    @set: PropertyName("class_chief")
    var classChief: String? = null,

    val students: List<String>? = null,

    val subjects: MutableList<Subject>? = null,

    )

@IgnoreExtraProperties
data class Subject(

    @get: PropertyName("subject_name")
    @set: PropertyName("subject_name")
    var subjectName: String? = null,

    val teacher: String? = null,

    @get: PropertyName("class_meetings")
    @set: PropertyName("class_meetings")
    var classMeetings: MutableList<ClassMeeting>? = null,

    @get: PropertyName("class_exams")
    @set: PropertyName("class_exams")
    var classExams: MutableList<String>? = null,

    @get: PropertyName("class_assignments")
    @set: PropertyName("class_assignments")
    var classAssignments: MutableList<String>? = null,

    ) : HomeSectionData()

@IgnoreExtraProperties
data class ClassMeeting(

    val id: String? = null,

    @get: PropertyName("subject_name")
    @set: PropertyName("subject_name")
    var subjectName: String? = null,

    @get: PropertyName("start_time")
    @set: PropertyName("start_time")
    var startTime: Date? = null,

    @get: PropertyName("end_time")
    @set: PropertyName("end_time")
    var endTime: Date? = null,

    val location: String? = null,

    val status: String? = null,

    @get: PropertyName("meeting_resource")
    @set: PropertyName("meeting_resource")
    var meetingResource: String? = null,

    ) : HomeSectionData()

