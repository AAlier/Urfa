package com.urfa.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.urfa.R
import com.urfa.ui.BasePickUserActivity
import com.urfa.ui.search.SearchActivity
import com.urfa.util.observeNonNull
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ListUserActivity : BasePickUserActivity(), ListNavigation {
    private val viewModel: ListViewModel by viewModel()
    private var deleteMenuItem: MenuItem? = null
    private var shareMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.setNavigation(this)
        viewModel.users.observeNonNull(this) {
            updateAdapter(it)
        }
        viewModel.selectedIndices.observe(this, Observer {
            it?.let {
                updateSelected(it)
            }
            deleteMenuItem?.isVisible = it != null && it.isNotEmpty()
            shareMenuItem?.isVisible = it != null && it.isNotEmpty()
        })
        val calendar = Calendar.getInstance()
        viewModel.updateFilter(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))
    }

    override fun onSelect(adapterPosition: Int) {
        viewModel.updateSelectedId(adapterPosition, getItem(adapterPosition))
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        deleteMenuItem = menu?.getItem(1)
        shareMenuItem = menu?.getItem(2)
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
            R.id.itemSearch -> {
                startActivity(Intent(this, SearchActivity::class.java))
            }
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
        val message =
            if (viewModel.isSingular()) getString(R.string.singular_selected) else getString(R.string.plural_selected)
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