package com.urfa.ui.list

import android.view.ViewGroup
import com.urfa.R
import com.urfa.ui.base.BaseViewHolder
import com.urfa.ui.weekview.WeekViewEvent
import com.urfa.util.monthTimeFormatter
import kotlinx.android.synthetic.main.item_user.view.*

class UserViewHolder(parent: ViewGroup) :
    BaseViewHolder<WeekViewEvent>(R.layout.item_user, parent)
{
    val username = itemView.userName
    val dateScheduled = itemView.scheduleDateTextView
    val parentView = itemView.parentView

    override fun bind(item: WeekViewEvent) {
        username.text = "${item.name} ${item.lastName}"
        dateScheduled.text = monthTimeFormatter.format(item.startTime.time)
    }

    override fun unbind() {
        username.text = null
        dateScheduled.text = null
    }

    fun isIndexSelected(indexSelected: Boolean) {
        parentView.isSelected = indexSelected
    }
}