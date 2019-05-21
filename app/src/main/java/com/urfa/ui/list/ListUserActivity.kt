package com.urfa.ui.list

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.Menu
import android.view.MenuItem
import com.urfa.R
import com.urfa.ui.base.BaseActivity
import com.urfa.util.monthTimeFormatter
import com.urfa.util.observeNonNull
import kotlinx.android.synthetic.main.activity_user_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class ListUserActivity : BaseActivity(), ListNavigation {

    private val onItemTouchMultiDragListener = object : OnItemTouchMultiDragListener("parentView") {
        override fun onDownTouchableView(adapterPosition: Int) {
            select(adapterPosition)
        }

        override fun onMoveTouchableView(adapterPosition: Int) {
            select(adapterPosition)
        }

        private fun select(adapterPosition: Int) {
            if (selectedIndices.indexOfKey(adapterPosition) < 0) {
                selectedIndices.put(adapterPosition, adapterPosition)
            } else {
                selectedIndices.remove(adapterPosition)
            }
            adapter.updateSelection(selectedIndices)
        }
    }

    private val viewModel: ListViewModel by viewModel()
    private val adapter = Adapter(ArrayList())
    private val selectedIndices = SparseArray<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupViewModel()
        setupRecyclerView()
    }

    private fun setupViewModel() {
        viewModel.setNavigation(this)
        viewModel.users.observeNonNull(this) {
            adapter.update(it)
        }
        val calendar = Calendar.getInstance()
        viewModel.updateFilter(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))
    }

    private fun setupRecyclerView() {
        // adapter.setSelectionListener(this)
        recyclerView.adapter = adapter
        recyclerView.addOnItemTouchListener(onItemTouchMultiDragListener)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.share, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null)
            return false
        when (item.itemId) {
            R.id.itemShare -> {
                val builder = StringBuilder()
                for (i in 0 until selectedIndices.size()) {
                    val value = adapter.list[i]
                    builder.append("${monthTimeFormatter.format(value.startTime.time)} ${value.name} ${value.lastName}\n")
                }
                val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                val shareBody = builder.toString()
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hey There")
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
                startActivity(Intent.createChooser(sharingIntent, "Share via"))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}