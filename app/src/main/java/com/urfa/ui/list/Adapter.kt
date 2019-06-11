package com.urfa.ui.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter
import com.urfa.ui.weekview.WeekViewEvent
import com.urfa.util.monthFormatter
import java.util.concurrent.CopyOnWriteArrayList

class Adapter(
    private var list: List<WeekViewEvent>,
    private val listener: Listener
) : RecyclerView.Adapter<UserViewHolder>(),
    StickyRecyclerHeadersAdapter<HeaderHolder> {
    override fun getHeaderId(position: Int): Long {
        return getHeaderFor(position)?.hashCode()?.toLong() ?: 0
    }

    private fun getHeaderFor(position: Int): String? {
        return monthFormatter.format(list.get(position).startTime.timeInMillis)
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup?) = HeaderHolder(parent)

    override fun onBindHeaderViewHolder(holder: HeaderHolder?, position: Int) {
        getHeaderFor(position)?.let { holder?.bind(it) }
    }

    private var selectedIndices = CopyOnWriteArrayList<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(parent, listener)
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
        return selectedIndices.indexOf(index) > -1
    }

    fun updateSelection(selectedIndices: CopyOnWriteArrayList<Int>) {
        this.selectedIndices = selectedIndices
    }

    fun getItem(adapterPosition: Int): WeekViewEvent = list.get(adapterPosition)
}