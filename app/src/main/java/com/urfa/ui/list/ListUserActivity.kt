package com.urfa.ui.list

import android.content.Intent
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration
import com.urfa.R
import com.urfa.ui.base.BaseActivity
import com.urfa.util.observeNonNull
import kotlinx.android.synthetic.main.activity_user_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class ListUserActivity : BaseActivity(), ListNavigation, Listener {
    private val viewModel: ListViewModel by viewModel()
    private val adapter = Adapter(ArrayList(), this)
    private var deleteMenuItem: MenuItem? = null
    private var shareMenuItem: MenuItem? = null
    var headersDecorator: StickyRecyclerHeadersDecoration = StickyRecyclerHeadersDecoration(adapter)

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
        viewModel.selectedIndices.observe(this, Observer {
            it?.let {
                adapter.updateSelection(it)
            }
            deleteMenuItem?.isVisible = it != null && it.isNotEmpty()
            shareMenuItem?.isVisible = it != null && it.isNotEmpty()
        })
        val calendar = Calendar.getInstance()
        viewModel.updateFilter(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))
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
        val event = adapter.getItem(adapterPosition)
        viewModel.updateSelectedId(adapterPosition, event)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        deleteMenuItem = menu?.getItem(0)
        shareMenuItem = menu?.getItem(1)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null)
            return false
        when (item.itemId) {
            R.id.itemShare -> {
                share()
            }
            R.id.itemDelete -> {
                alertDelete()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun share() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, viewModel.getShareableText())
        startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }

    private fun alertDelete() {
        val message = if(viewModel.isSingular()) getString(R.string.singular_selected) else getString(R.string.plural_selected)
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.delete))
            .setMessage(getString(R.string.delete_selected, message))
            .setIcon(R.drawable.ic_remove)
            .setCancelable(true)
            .setNegativeButton(R.string.no) { dialog, id -> dialog.cancel() }
            .setPositiveButton(R.string.yes) { dialog, id ->
                viewModel.deleteSelected()
                dialog.cancel()
            }
        val alert = builder.create()
        alert.show()
    }
}