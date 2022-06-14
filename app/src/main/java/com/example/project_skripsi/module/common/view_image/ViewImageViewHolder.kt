package com.example.project_skripsi.module.common.view_image

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.Announcement
import com.example.project_skripsi.core.model.firestore.Payment
import com.example.project_skripsi.core.model.firestore.Student
import com.example.project_skripsi.core.model.local.HomeSectionData
import com.example.project_skripsi.core.repository.FireStorage
import com.example.project_skripsi.databinding.*
import com.example.project_skripsi.utils.app.App
import com.example.project_skripsi.utils.generic.GenericAdapter
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce
import com.example.project_skripsi.utils.generic.ItemClickListener
import com.example.project_skripsi.utils.helper.CurrencyHelper
import com.example.project_skripsi.utils.helper.DateHelper

class ViewImageViewHolder(private val dataSet : List<String>) {

    fun getAdapter(): GenericAdapter<String> {
        val adapter = GenericAdapter(dataSet)
        adapter.expressionOnCreateViewHolder = {
            ItemCmViewImageBinding.inflate(LayoutInflater.from(it.context), it, false)
        }
        adapter.expressionViewHolderBinding = { item, viewBinding, holder ->
            val view = viewBinding as ItemCmViewImageBinding
            with(view) {
                tvNumber.text = ("Foto ${holder.absoluteAdapterPosition+1}")
                FireStorage.inst.getImage(item, root.context).first.observeOnce {
                    imvImage.setImageBitmap(it)
                }
            }
        }
        return adapter
    }

}