package com.example.project_skripsi.utils.generic

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class GenericAdapter<T>(private val dataSet : List<T>) : RecyclerView.Adapter<BaseViewHolder<T>>(){

    var expressionViewHolderBinding: ((T,ViewBinding,RecyclerView.ViewHolder) -> Unit)? = null
    var expressionOnCreateViewHolder:((ViewGroup)->ViewBinding)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        return expressionOnCreateViewHolder?.let {
            it(parent)
        }?.let { BaseViewHolder(it, expressionViewHolderBinding!!) }!!
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}


class BaseViewHolder<T> internal constructor(private val binding:ViewBinding, private val expression:(T, ViewBinding, RecyclerView.ViewHolder)->Unit)
    :RecyclerView.ViewHolder(binding.root){
    fun bind(item:T){
        expression(item,binding,this)
    }
}
