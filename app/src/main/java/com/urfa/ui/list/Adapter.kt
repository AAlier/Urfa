package com.urfa.ui.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.urfa.model.UserEntity

class Adapter(val list: List<UserEntity>) : RecyclerView.Adapter<UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(parent)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list.get(position))
    }

    override fun onViewRecycled(holder: UserViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }

    override fun getItemCount(): Int {
        return list.size
    }

}