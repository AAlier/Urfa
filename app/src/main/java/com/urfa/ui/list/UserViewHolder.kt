package com.urfa.ui.list

import android.annotation.SuppressLint
import android.view.ViewGroup
import com.urfa.R
import com.urfa.ui.base.BaseViewHolder
import com.urfa.ui.weekview.WeekViewEvent
import com.urfa.util.timeFormatter
import kotlinx.android.synthetic.main.item_user.view.*

class UserViewHolder(parent: ViewGroup, listener: Listener) :
    BaseViewHolder<WeekViewEvent>(R.layout.item_user, parent)
{
    val username = itemView.userName
    val timeScheduled = itemView.scheduleDateTextView
    val parentView = itemView.parentView

    init {
        itemView.setOnClickListener {
            listener.onSelect(adapterPosition)
            isIndexSelected(!itemView.isSelected)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun bind(item: WeekViewEvent) {
        username.text = "${item.name} ${item.lastName}"
        timeScheduled.text = "${timeFormatter.format(item.startTime.timeInMillis)}-${timeFormatter.format(item.endTime.timeInMillis)}"
    }

    override fun unbind() {
        username.text = null
        timeScheduled.text = null
    }

    fun isIndexSelected(indexSelected: Boolean) {
        parentView.isSelected = indexSelected
        username.isSelected = indexSelected
        timeScheduled.isSelected = indexSelected
    }
}