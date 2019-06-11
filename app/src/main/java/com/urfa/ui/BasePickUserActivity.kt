package com.urfa.ui

import android.os.Bundle
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration
import com.urfa.R
import com.urfa.ui.base.BaseActivity
import com.urfa.ui.list.Adapter
import com.urfa.ui.list.Listener
import com.urfa.ui.weekview.WeekViewEvent
import kotlinx.android.synthetic.main.activity_user_list.*
import java.util.concurrent.CopyOnWriteArrayList

abstract class BasePickUserActivity : BaseActivity() , Listener {
    private val adapter = Adapter(ArrayList(), this)
    private var headersDecorator: StickyRecyclerHeadersDecoration =
        StickyRecyclerHeadersDecoration(adapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(headersDecorator)

        val itemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.adapter = adapter
    }

    override fun onSelect(adapterPosition: Int) {

    }

    fun updateAdapter(it: PagedList<WeekViewEvent>) {
        adapter.update(it)
    }

    fun updateSelected(it: CopyOnWriteArrayList<Int>) {
        adapter.updateSelection(it)
    }

    fun getItem(position: Int) = adapter.getItem(position)
}