package com.urfa.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.urfa.R
import com.urfa.ui.add.AddUserActivity
import com.urfa.ui.base.BaseActivity
import com.urfa.ui.list.ListUserActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity(), MainNavigation {
    val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()
        /*weekView.setMonthChangeListener { newYear, newMonth ->
            return@setMonthChangeListener mutableListOf()
        }*/
    }

    private fun setupViewModel(){
        viewModel.setNavigation(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list_users, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null)
            return false
        when (item.itemId) {
            R.id.item_add -> {
                startActivity(Intent(this, AddUserActivity::class.java))
            }
            R.id.item_show_all -> {
                startActivity(Intent(this, ListUserActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
