package com.urfa.ui.list

import android.view.ViewGroup
import com.urfa.R
import com.urfa.ui.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_header.view.*

class HeaderHolder(parent: ViewGroup?) : BaseViewHolder<String>(R.layout.item_header, parent) {
    override fun bind(item: String) {
        itemView.alphabetLetter.text = item
    }

    override fun unbind() {
        itemView.alphabetLetter.text = null
    }
}
