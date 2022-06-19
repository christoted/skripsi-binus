package com.example.project_skripsi.module.common.view_image

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView.*
import com.example.project_skripsi.core.repository.FireStorage
import com.example.project_skripsi.databinding.*
import com.example.project_skripsi.module.teacher.TcMainActivity
import com.example.project_skripsi.utils.generic.GenericAdapter
import com.example.project_skripsi.utils.generic.GenericObserver.Companion.observeOnce


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
                    imvImage.setImageBitmap(BitmapFactory.decodeFile(it.absolutePath))
                    imvImage.setOnClickListener { _ ->
                        root.context.startActivity(ViewImageActivity.newIntent(root.context, it))
                    }
                }

            }
        }
        return adapter
    }

}