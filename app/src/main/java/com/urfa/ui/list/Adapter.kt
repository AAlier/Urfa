package com.urfa.ui.list

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.dragselectrecyclerview.DragSelectReceiver
import com.urfa.ui.weekview.WeekViewEvent

class Adapter(
    var list: List<WeekViewEvent>
) : RecyclerView.Adapter<UserViewHolder>(){
    private var selectedIndices = SparseArray<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(parent)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list.get(position))
        holder.isIndexSelected(isSelected(position))
    }

    override fun onViewRecycled(holder: UserViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun update(list: List<WeekViewEvent>) {
        this.list = list
        notifyDataSetChanged()
    }

    private fun isSelected(index: Int): Boolean {
        // return true if this index is currently selected
        return selectedIndices.indexOfKey(index) > -1
    }

    fun updateSelection(selectedIndices: SparseArray<Int>) {
        this.selectedIndices = selectedIndices
        notifyDataSetChanged()
    }
}