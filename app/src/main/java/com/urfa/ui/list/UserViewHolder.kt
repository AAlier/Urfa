package com.urfa.ui.list

import android.view.ViewGroup
import com.urfa.R
import com.urfa.model.UserEntity
import com.urfa.ui.base.BaseViewHolder
import com.urfa.util.monthTimeFormatter
import com.urfa.util.timeFormatter
import kotlinx.android.synthetic.main.item_user.view.*

class UserViewHolder(parent: ViewGroup) : BaseViewHolder<UserEntity>(R.layout.item_user, parent) {
    val startDate = itemView.startDateTextView
    val endDate = itemView.endDateTextView
    val username = itemView.userName
    val dateScheduled = itemView.scheduleDateTextView

    override fun bind(item: UserEntity) {
        startDate.text = timeFormatter.format(item.startDate?.time)
        endDate.text = timeFormatter.format(item.endDate?.time)

        username.text = "${item.firstName} ${item.lastName}"
        dateScheduled.text = monthTimeFormatter.format(item.startDate?.time)
    }

    override fun unbind() {
        startDate.text = null
        endDate.text = null
        username.text = null
        dateScheduled.text = null
    }

}