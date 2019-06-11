package com.urfa.ui.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import com.urfa.R
import com.urfa.ui.BasePickUserActivity
import com.urfa.util.observeNonNull
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : BasePickUserActivity(), SearchNavigation, SearchView.OnQueryTextListener {
    private val viewModel: SearchViewModel by viewModel()
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchMenuItem = menu?.findItem(R.id.itemSearch)
        searchView = searchMenuItem?.actionView as? SearchView
        searchView!!.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView!!.isSubmitButtonEnabled = true
        searchView!!.setOnQueryTextListener(this)
        searchView!!.onActionViewExpanded()
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupViewModel() {
        viewModel.setNavigation(this)
        viewModel.users.observeNonNull(this) {
            updateAdapter(it)
        }
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        viewModel.searchFor(newText)
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onSelect(adapterPosition: Int) {
        EventBus.getDefault().postSticky(getItem(adapterPosition))
        super.onBackPressed()
    }

}