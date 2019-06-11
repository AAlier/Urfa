package com.urfa.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(
    @LayoutRes
    layout: Int,
    parent: ViewGroup?
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent?.context).inflate(layout, parent, false)
) {

    abstract fun bind(item: T)
    abstract fun unbind()

}