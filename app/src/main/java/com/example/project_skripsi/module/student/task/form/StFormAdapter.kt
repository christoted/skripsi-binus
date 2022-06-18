package com.example.project_skripsi.module.student.task.form

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.*
import com.example.project_skripsi.core.model.local.AssignedQuestion
import com.example.project_skripsi.core.repository.FireStorage
import com.example.project_skripsi.databinding.DialogStViewImageBinding
import com.example.project_skripsi.databinding.ItemStTaskFormEssayBinding
import com.example.project_skripsi.databinding.ItemStTaskFormMcBinding
import com.example.project_skripsi.module.common.view_image.ViewImageViewHolder
import com.example.project_skripsi.module.student.StMainActivity
import com.example.project_skripsi.utils.Constant.Companion.TASK_FORM_ESSAY
import com.example.project_skripsi.utils.Constant.Companion.TASK_FORM_MC
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce

class StFormAdapter(
    val questionList: List<AssignedQuestion>,
    val studentId: String?,
    val taskFormId: String,
    var activity: StMainActivity,
    var isViewOnly: Boolean
    ) :
    Adapter<ViewHolder>() {

    val imageList : List<MutableList<String>> = List(questionList.size) { mutableListOf() }

    init {
        questionList.mapIndexed { index, assignedQuestion ->
            assignedQuestion.answer?.images?.let { imageList[index].addAll(it) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            0 -> MultipleChoiceViewHolder(ItemStTaskFormMcBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false))
            else -> EssayViewHolder(ItemStTaskFormEssayBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false))
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (questionList[position].type) {
            TASK_FORM_MC ->
                (holder as MultipleChoiceViewHolder).bind(questionList[position], position)
            TASK_FORM_ESSAY ->
                (holder as EssayViewHolder).bind(questionList[position], position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return StTaskFormViewModel.TASK_FORM_TYPE[questionList[position].type]!!
    }

    override fun getItemCount() = questionList.size

    inner class MultipleChoiceViewHolder ( private val binding : ItemStTaskFormMcBinding) : ViewHolder(binding.root) {
        fun bind(item: AssignedQuestion, position: Int) {
            with(binding) {
                tvNumber.text = ("${position+1}.")
                choice1.text = item.choices?.getOrNull(0)
                choice2.text = item.choices?.getOrNull(1)
                choice3.text = item.choices?.getOrNull(2)
                choice4.text = item.choices?.getOrNull(3)
                choice5.text = item.choices?.getOrNull(4)
                tvTitle.text = item.title
                item.answer?.answerText?.let {
                    when (it.toInt()) {
                        1 -> choice1.isChecked = true
                        2 -> choice2.isChecked = true
                        3 -> choice3.isChecked = true
                        4 -> choice4.isChecked = true
                        5 -> choice5.isChecked = true
                    }
                }
                tvScoreWeight.text = ("Bobot : ${item.scoreWeight}")
                tvImageCount.text = imageList[position].size.toString()

                if (isViewOnly) {
                    btnDeleteImage.visibility = View.GONE
                    btnAddImage.visibility = View.GONE
                }

                btnViewImage.setOnClickListener {
                    if (imageList[position].isNotEmpty()) {
                        showImagesDialog(position, root.context)
                    } else {
                        Toast.makeText(root.context, "Tidak ada gambar", Toast.LENGTH_SHORT).show()
                    }
                }
                btnDeleteImage.setOnClickListener {
                    imageList[position].clear()
                    tvImageCount.text = imageList[position].size.toString()
                }
                btnAddImage.setOnClickListener { takeImage(position, root.context, tvImageCount) }
            }
        }
    }

    inner class EssayViewHolder ( private val binding : ItemStTaskFormEssayBinding) : ViewHolder(binding.root) {
        fun bind(item: AssignedQuestion, position: Int) {
            with(binding) {
                tvNumber.text = ("${position+1}.")
                tvTitle.text = item.title
                item.answer?.answerText?.let { edtAnswer.setText(it) }
                tvScoreWeight.text = ("Bobot : ${item.scoreWeight}")
                tvImageCount.text = imageList[position].size.toString()

                if (isViewOnly) {
                    btnDeleteImage.visibility = View.GONE
                    btnAddImage.visibility = View.GONE
                }

                btnViewImage.setOnClickListener {
                    if (imageList[position].isNotEmpty()) {
                        showImagesDialog(position, root.context)
                    } else {
                        Toast.makeText(root.context, "Tidak ada gambar", Toast.LENGTH_SHORT).show()
                    }
                }
                btnDeleteImage.setOnClickListener {
                    imageList[position].clear()
                    tvImageCount.text = imageList[position].size.toString()
                }

                btnAddImage.setOnClickListener { takeImage(position, root.context, tvImageCount) }
            }
        }
    }

    private fun takeImage(questionNumber: Int, context: Context, tvCount: TextView) {
        val path = "$studentId/$taskFormId/${questionNumber+1}/pic${imageList[questionNumber].size}"
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile = FireStorage.inst.getPhotoFile(path, context)

        val fileProvider = FileProvider.getUriForFile(context, "com.example.fileprovider", photoFile)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

        activity.mARLRequestCamera.launch(takePictureIntent)
        activity.viewModel.isImageCaptured.observe(context as AppCompatActivity) {
            if (it.getContentIfNotHandled() == true) {
                val dialog = ProgressDialog.show(
                    context, "",
                    "Loading. Please wait...", true
                )

                FireStorage.inst.uploadImage(path, photoFile).first.observeOnce { success ->
                    if (success) {
                        imageList[questionNumber].add(path)
                        tvCount.text = imageList[questionNumber].size.toString()
                        dialog.dismiss()
                    }
                }
                activity.viewModel.isImageCaptured.removeObservers(context)
            }
        }
    }

    private fun showImagesDialog(questionNumber: Int, context: Context) {
        val dialog = Dialog(context)

        val sBinding = DialogStViewImageBinding.inflate(LayoutInflater.from(context))

        with(sBinding) {

            tvTitle.text = ("Foto Soal - ${questionNumber+1}")

            rvContainer.layoutManager = LinearLayoutManager(context)
            rvContainer.adapter = ViewImageViewHolder(imageList[questionNumber]).getAdapter()

            btnClose.setOnClickListener { dialog.dismiss() }

            dialog.setContentView(root)
        }

        dialog.show()
    }
}
