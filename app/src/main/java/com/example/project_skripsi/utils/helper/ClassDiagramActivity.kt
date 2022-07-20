package com.example.project_skripsi.utils.helper

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.project_skripsi.R
import com.example.project_skripsi.core.repository.AuthRepository
import com.example.project_skripsi.core.repository.FireRepository
import com.example.project_skripsi.core.repository.FireStorage
import com.example.project_skripsi.module.common.auth.AuthActivity
import com.example.project_skripsi.module.common.auth.AuthViewModel
import com.example.project_skripsi.module.common.view_image.ViewImageActivity
import com.example.project_skripsi.module.common.view_image.ViewImageViewHolder
import com.example.project_skripsi.module.common.zoom.MyMeetingActivity
import com.example.project_skripsi.module.common.zoom.MyMeetingViewModel
import com.example.project_skripsi.module.parent.home.PrHomeFragment
import com.example.project_skripsi.module.parent.home.PrHomeViewModel
import com.example.project_skripsi.module.parent.home.viewholder.agenda.*
import com.example.project_skripsi.module.parent.student_detail.announcement.AnnouncementViewHolder
import com.example.project_skripsi.module.parent.student_detail.announcement.PrAnnouncementFragment
import com.example.project_skripsi.module.parent.student_detail.announcement.PrAnnouncementViewModel
import com.example.project_skripsi.module.parent.student_detail.calendar.PrCalendarAdapter
import com.example.project_skripsi.module.parent.student_detail.calendar.PrCalendarFragment
import com.example.project_skripsi.module.parent.student_detail.calendar.PrCalendarViewModel
import com.example.project_skripsi.module.parent.student_detail.payment.PrPaymentFragment
import com.example.project_skripsi.module.parent.student_detail.payment.PrPaymentViewModel
import com.example.project_skripsi.module.parent.student_detail.payment.variant.PrPaymentVariantFragment
import com.example.project_skripsi.module.parent.student_detail.payment.variant.PrPaymentVariantViewHolder
import com.example.project_skripsi.module.parent.student_detail.progress.PrProgressFragment
import com.example.project_skripsi.module.parent.student_detail.progress.PrProgressViewModel
import com.example.project_skripsi.module.parent.student_detail.progress.achievement.PrProgressAchievementFragment
import com.example.project_skripsi.module.parent.student_detail.progress.achievement.PrProgressAchievementViewHolder
import com.example.project_skripsi.module.parent.student_detail.progress.attendance.PrProgressAttendanceFragment
import com.example.project_skripsi.module.parent.student_detail.progress.attendance.viewholder.PrProgressAttendanceChildViewHolder
import com.example.project_skripsi.module.parent.student_detail.progress.attendance.viewholder.PrProgressAttendanceViewHolder
import com.example.project_skripsi.module.parent.student_detail.progress.graphic.PrProgressGraphicFragment
import com.example.project_skripsi.module.parent.student_detail.progress.graphic.PrProgressGraphicViewModel
import com.example.project_skripsi.module.parent.student_detail.progress.score.PrProgressScoreFragment
import com.example.project_skripsi.module.parent.student_detail.progress.score.viewholder.PrProgressScoreChildViewHolder
import com.example.project_skripsi.module.parent.student_detail.progress.score.viewholder.PrProgressScoreViewHolder
import com.example.project_skripsi.module.student.main._sharing.agenda.*
import com.example.project_skripsi.module.student.main.calendar.StCalendarAdapter
import com.example.project_skripsi.module.student.main.calendar.StCalendarFragment
import com.example.project_skripsi.module.student.main.calendar.StCalendarViewModel
import com.example.project_skripsi.module.student.main.home.announcement.StAnnouncementFragment
import com.example.project_skripsi.module.student.main.home.announcement.StAnnouncementViewModel
import com.example.project_skripsi.module.student.main.home.profile.StProfileFragment
import com.example.project_skripsi.module.student.main.home.profile.StProfileViewModel
import com.example.project_skripsi.module.student.main.home.view.StHomeFragment
import com.example.project_skripsi.module.student.main.home.view.adapter.StHomeRecyclerViewMainAdapter
import com.example.project_skripsi.module.student.main.home.viewmodel.StHomeViewModel
import com.example.project_skripsi.module.student.main.payment.StPaymentFragment
import com.example.project_skripsi.module.student.main.payment.StPaymentViewModel
import com.example.project_skripsi.module.student.main.payment.variant.StPaymentVariantFragment
import com.example.project_skripsi.module.student.main.payment.variant.StPaymentVariantViewHolder
import com.example.project_skripsi.module.student.main.progress.graphic.StProgressGraphicFragment
import com.example.project_skripsi.module.student.main.progress.graphic.StProgressGraphicViewModel
import com.example.project_skripsi.module.student.main.progress.view.StScoreFragment
import com.example.project_skripsi.module.student.main.progress.view.adapter.StScoreAchievementAdapter
import com.example.project_skripsi.module.student.main.progress.view.adapter.StScoreContentAdapter
import com.example.project_skripsi.module.student.main.progress.view.adapter.StScoreContentChildAdapter
import com.example.project_skripsi.module.student.main.progress.view.fragment.achievement.StProgressAchievementFragment
import com.example.project_skripsi.module.student.main.progress.view.fragment.attendance.StProgressAttendanceFragment
import com.example.project_skripsi.module.student.main.progress.view.fragment.score.StProgressScoreFragment
import com.example.project_skripsi.module.student.main.progress.viewmodel.StScoreViewModel
import com.example.project_skripsi.module.student.main.studyclass.StClassFragment
import com.example.project_skripsi.module.student.main.studyclass.StClassViewModel
import com.example.project_skripsi.module.student.main.studyclass.SubjectAdapter
import com.example.project_skripsi.module.student.main.studyclass.student_list.StStudentListFragment
import com.example.project_skripsi.module.student.main.studyclass.student_list.StStudentListViewModel
import com.example.project_skripsi.module.student.main.studyclass.student_list.StudentListViewHolder
import com.example.project_skripsi.module.student.subject_detail.StSubjectFragment
import com.example.project_skripsi.module.student.subject_detail.StSubjectViewModel
import com.example.project_skripsi.module.student.subject_detail.resource.StSubjectResourceAdapter
import com.example.project_skripsi.module.student.subject_detail.resource.StSubjectResourceFragment
import com.example.project_skripsi.module.student.task.assignment.StTaskAssignmentFragment
import com.example.project_skripsi.module.student.task.assignment.StTaskAssignmentViewModel
import com.example.project_skripsi.module.student.task.exam.StTaskExamFragment
import com.example.project_skripsi.module.student.task.exam.StTaskExamViewModel
import com.example.project_skripsi.module.student.task.form.StFormAdapter
import com.example.project_skripsi.module.student.task.form.StTaskFormFragment
import com.example.project_skripsi.module.student.task.form.StTaskFormViewModel
import com.example.project_skripsi.module.teacher._sharing.AssignmentViewHolder
import com.example.project_skripsi.module.teacher._sharing.ClassViewHolder
import com.example.project_skripsi.module.teacher._sharing.ResourceViewHolder
import com.example.project_skripsi.module.teacher._sharing.agenda.TcAgendaAssignmentViewHolder
import com.example.project_skripsi.module.teacher._sharing.agenda.TcAgendaExamViewHolder
import com.example.project_skripsi.module.teacher._sharing.agenda.TcAgendaMeetingViewHolder
import com.example.project_skripsi.module.teacher.form.alter.TcAlterTaskFragment
import com.example.project_skripsi.module.teacher.form.alter.TcAlterTaskViewModel
import com.example.project_skripsi.module.teacher.form.alter.TcFormAdapter
import com.example.project_skripsi.module.teacher.form.alter.finalization.TcAlterTaskFinalizationFragment
import com.example.project_skripsi.module.teacher.form.alter.finalization.TcAlterTaskFinalizationViewModel
import com.example.project_skripsi.module.teacher.form.assessment.TcAssessmentFormAdapter
import com.example.project_skripsi.module.teacher.form.assessment.TcAssessmentTaskFormFragment
import com.example.project_skripsi.module.teacher.form.assessment.TcAssessmentTaskFormViewModel
import com.example.project_skripsi.module.teacher.main.calendar.TcCalendarAdapter
import com.example.project_skripsi.module.teacher.main.calendar.TcCalendarFragment
import com.example.project_skripsi.module.teacher.main.calendar.TcCalendarViewModel
import com.example.project_skripsi.module.teacher.main.home.TcHomeFragment
import com.example.project_skripsi.module.teacher.main.home.adapter.TcHomeChildAdapter
import com.example.project_skripsi.module.teacher.main.home.adapter.TcHomeMainAdapter
import com.example.project_skripsi.module.teacher.main.home.announcement.TcAnnouncementFragment
import com.example.project_skripsi.module.teacher.main.home.announcement.TcAnnouncementViewModel
import com.example.project_skripsi.module.teacher.main.home.profile.TcProfileFragment
import com.example.project_skripsi.module.teacher.main.home.profile.TcProfileViewModel
import com.example.project_skripsi.module.teacher.main.home.viewmodel.TcHomeViewModel
import com.example.project_skripsi.module.teacher.main.study_class.TcStudyClassFragment
import com.example.project_skripsi.module.teacher.main.study_class.TcStudyClassViewHolder
import com.example.project_skripsi.module.teacher.main.study_class.TcStudyClassViewModel
import com.example.project_skripsi.module.teacher.main.task.TcTaskFragment
import com.example.project_skripsi.module.teacher.main.task.TcTaskViewModel
import com.example.project_skripsi.module.teacher.resource.view.TcAlterResourceFragment
import com.example.project_skripsi.module.teacher.resource.view.TcAlterResourceViewModel
import com.example.project_skripsi.module.teacher.resource.view.TcMeetingNumberViewHolder
import com.example.project_skripsi.module.teacher.student_detail.view.TcStudentFragment
import com.example.project_skripsi.module.teacher.student_detail.view.adapter.TcScoreViewPagerAdapter
import com.example.project_skripsi.module.teacher.student_detail.view.attendance.TcStudentDetailAttendance
import com.example.project_skripsi.module.teacher.student_detail.view.payment.TcStudentDetailPayment
import com.example.project_skripsi.module.teacher.student_detail.view.payment.TcStudentDetailPaymentAdapter
import com.example.project_skripsi.module.teacher.student_detail.view.payment.TcStudentDetailPaymentContentViewHolder
import com.example.project_skripsi.module.teacher.student_detail.view.score.TcStudentDetailScore
import com.example.project_skripsi.module.teacher.student_detail.view.score.adapter.TcStudentDetailScoreAdapter
import com.example.project_skripsi.module.teacher.student_detail.view.score.adapter.TcStudentDetailScoreChildAdapter
import com.example.project_skripsi.module.teacher.student_detail.viewmodel.TcStudentDetailViewModel
import com.example.project_skripsi.module.teacher.study_class.homeroom.HomeroomStudentViewHolder
import com.example.project_skripsi.module.teacher.study_class.homeroom.TcStudyClassHomeroomFragment
import com.example.project_skripsi.module.teacher.study_class.homeroom.TcStudyClassHomeroomViewModel
import com.example.project_skripsi.module.teacher.study_class.resource.TcStudyClassResourceFragment
import com.example.project_skripsi.module.teacher.study_class.resource.TcStudyClassResourceViewModel
import com.example.project_skripsi.module.teacher.study_class.teaching.TcStudyClassTeachingFragment
import com.example.project_skripsi.module.teacher.study_class.teaching.TcStudyClassTeachingViewModel
import com.example.project_skripsi.module.teacher.study_class.teaching.TeachingStudentViewHolder
import com.example.project_skripsi.utils.service.storage.StorageSP
import java.util.*
import kotlin.reflect.KVisibility
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties

class ClassDiagramActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ClassDiagramGenerator"
        private val INSPECT_CLASSES = listOf(
            MyMeetingActivity::class,
            MyMeetingViewModel::class
        )

        private val whatsappList = listOf(
            "graciela jesslyn w 0895328264423",
            "Michelle Angela Hartanto 089506194146",
            "Santa Ave 081393999325",
            "Fabianus Hans 082237382336",
            "keishya liem 089507032905",
            "Michael Henry Hartono 081377336017",
            "Florensia Shinta Dewi Prasasti 081227051908",
            "Vanessa W 085292762020",
            "branito dhammaraya 082136383391",
            "Michelle Asher Dian 085290747533",
            "devina 08561729740",
            "Maria Marcella Wibisono 082186254975",
            "Benedictus Oktaviano Marantika Prasetyo 083849382149",
            "Leonard Sutanto 081237527400",
            "Angelice Chilly 081382511977",
            "Michelle Putri Hartono 081377336019",
            "Angela Ardelia Salvia Pakpahan 085876079626",
            "Emanuella intan 081933915100",
            "Vicka Rumanti Putri Kinanthi 082242573070",
            "Marcellinus Adjie S 082139134149",
            "Michelle Wirawan 081215677575",
            "Nataniela Bertha Christanty 081320725925",
            "Benedict Cleavon Aldiputra 081329824629",
            "Didakus Carvalho Tegar Ezar Widya Adyatma 08980114555",
            "Agustina Ari Putri 081325295019",
            "Gisela Kalista Putri 081567974595",
            "olda odelia 089649185604",
            "Rafel Nanda Purnomo 081930628686",
            "Tedy Imanuel sugiyanto 081326174463",
            "Verena Monita Wibowo 081293146986",
            "marchellyla lorenza 081326977787",
            "Carissa Aurelia Chavi 085694033244",
            "Angela Putri Permata Bukardi 082144527530",
            "erina florencia 081933230205",
            "Evelyn chesa s. 081267777021",
            "Carollyn Graciela Sugiono 082335336632",
            "Helena Cevi N 081226360364",
            "Geraldo Angkasa Putra 089523220549",
            "valentina sekar ayuningtyas 082243629323",
            "Palupi Sekar Wahyutami 08112638458",
            "Angela Chesa 085884561320",
            "Chaterine Vincencia E M S 082135632121",
            "Monica Gabriella Christina 08992055777",
            "Agnes Valerie 085865205520",
            "Michelle Dewanti Saputro 081326940832",
            "Hanania Fidela Leylani 081226638865",
            "Felisha 0895329337661",
            "Catherine Nirmala Setiawan 8881233929",
            "Cahayani Agustina Santoso 088230924304",
            "Jenif Eliya Driatmaja 085291497808",
            "Louise Aurellia Rahardjo 085856833480",
            "Katarina Krisjayanti 081231400065"
        )
    }

    private lateinit var mPrev : Button
    private lateinit var mNext : Button
    private lateinit var mSend : Button
    private lateinit var edtText : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_diagram)

        createClassDiagramProperty()

        mPrev = findViewById(R.id.btn_prev)
        mNext = findViewById(R.id.btn_next)
        mSend = findViewById(R.id.btn_send)
        edtText = findViewById(R.id.edt_number)

        mPrev.setOnClickListener {
            val num = edtText.text.toString().toInt() - 1
            edtText.setText(num.toString())

            findViewById<TextView>(R.id.tv_name).text = TextHelper.capitalize(whatsappList[num].split(" ").first().lowercase(Locale.getDefault()))
            findViewById<TextView>(R.id.tv_phone_number).text = whatsappList[num].split(" ").last()
        }

        mNext.setOnClickListener {
            val num = edtText.text.toString().toInt() + 1
            edtText.setText(num.toString())

            findViewById<TextView>(R.id.tv_name).text = TextHelper.capitalize(whatsappList[num].split(" ").first().lowercase(Locale.getDefault()))
            findViewById<TextView>(R.id.tv_phone_number).text = whatsappList[num].split(" ").last()
        }

        mSend.setOnClickListener {
            val num = edtText.text.toString().toInt()
            goToWhatsApp(
                whatsappList[num].split(" ").last(),
                TextHelper.capitalize(whatsappList[num].split(" ").first().lowercase(Locale.getDefault()))
            )
        }

        findViewById<TextView>(R.id.tv_number).text = (" / ${whatsappList.size}")
    }

    private fun createClassDiagramProperty() {
        INSPECT_CLASSES.map { kClass ->
            Log.d(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")

            Log.d(TAG, kClass.toString().cleaningType())

            Log.d(TAG, "===============================")

            for (prop in kClass.declaredMemberProperties) {
                Log.d(TAG,
                    (if (prop.visibility == KVisibility.PUBLIC) "+" else "-") + " " +
                            prop.name + ": " +
                            prop.returnType.toString().cleaningType())
            }

            Log.d(TAG, "-------------------------------")

            kClass.companionObject?.let {
                for (prop in it.declaredMemberProperties) {
                    Log.d(
                        TAG,
                        (if (prop.visibility == KVisibility.PUBLIC) "+" else "-") + " " +
                                prop.name + ": " +
                                prop.returnType.toString().cleaningType()
                    )
                }
            }


            Log.d(TAG, "===============================")

            for (prop in kClass.declaredMemberFunctions) {
                Log.d(TAG,
                    (if (prop.visibility == KVisibility.PUBLIC) "+" else "-")+ " " +
                            prop.name +
                            "(" + getParam(prop.parameters.first().toString()) + ")" + ": " +
                            prop.returnType.toString().cleaningType())

            }

            Log.d(TAG, "-------------------------------")

            kClass.companionObject?.let {
                for (prop in it.declaredMemberFunctions) {
                    Log.d(TAG,
                        (if (prop.visibility == KVisibility.PUBLIC) "+" else "-")+ " " +
                                prop.name +
                                "(" + getParam(prop.parameters.first().toString()) + ")" + ": " +
                                prop.returnType.toString().cleaningType())

                }
            }
        }
    }


    private fun getParam(s : String): String {
        s.split("(").getOrNull(1)?.let { ss ->
            ss.split(")").getOrNull(0)?.let { sss ->
                return sss.split(",").joinToString(",") { ssss ->
                    ssss.cleaningType()
                }
            }
        }
        return "null"
    }


    private fun String.cleaningType() : String {
        var cnt = 0
        var hasComma = false
        for (ch in this) {
            if (ch == '<') cnt++
            else if (ch == '>') cnt--;
            else if (ch == ',' && cnt == 0) hasComma = true
        }

        if (hasComma) {
            return this.split(",").joinToString(",") {
                it.cleaningType()
            }
        }

        if (!this.contains("<")) return this.getLatestDot()
        val l = this.indexOf("<")
        val r = this.lastIndexOf(">")
        return this.substring(0, l).getLatestDot() + "<" + this.substring(l+1,r).cleaningType() + ">"
    }

    private fun String.getLatestDot() : String {
        return this.split(".").last().split(" ").first()
    }

    private fun goToWhatsApp(phoneNumber: String, text: String) {
        val cleanPhone = "62" + phoneNumber.removePrefix("0")
        val template = "Halo ${text}, masih ingat dengan kami Christopher Teddy Mienarto (alumni SMA Regina Pacis), Luis Anthonie Alkins, dan Devita Setyaningrum, mahasiswa dari Bina Nusantara Jakarta, yang sedang melakukan skripsi dengan topik Mobile Application. Saat ini, kami sudah selesai mengembangkan aplikasi yang bernama OneClick,  OneClick  merupakan aplikasi Learning Management System (LMS) yang berfungsi untuk mengatur dan membantu pembelajaran sekolah online. Aplikasi OneClick ini akan digunakan oleh siswa, orang tua dan guru. \n" +
                "\n" +
                "Untuk itu kami mohon ketersediaan waktu teman-teman untuk melakukan evaluasi terhadap aplikasi ini dengan cara menonton video atau mendownload *apk* aplikasi yang sudah kami sediakan di dalam gform ini. (Jika ada kendala atau pertanyaan saat mendownload dan menggunakan aplikasi, bisa langsung hubungi saya)\n" +
                "\n" +
                "https://forms.gle/4vf1J1H4Jae1jbnFA\n" +
                "\n" +
                "Terima kasih \uD83D\uDE4F"
        val url = "https://api.whatsapp.com/send/?phone=${cleanPhone}&text=${template}"
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}