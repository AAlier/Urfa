package com.urfa.ui.main

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import com.urfa.R
import com.urfa.ui.add.AddUserActivity
import com.urfa.ui.base.BaseActivity
import com.urfa.ui.list.ListUserActivity
import com.urfa.util.monthTimeFormatter
import com.urfa.util.observeNonNull
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity(), MainNavigation {
    private val viewModel: MainViewModel by viewModel()
    private val dialog by lazy { initDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()
        setupListener()
    }

    private fun setupViewModel() {
        viewModel.setNavigation(this)
        viewModel.users.observeNonNull(this) {
            weekView.updateEvent(it)
        }
    }

    private fun setupListener() {
        weekView.setMonthChangeListener { newYear, newMonth ->
            viewModel.updateFilter(newYear, newMonth)
            return@setMonthChangeListener mutableListOf()
        }
        weekView.setOnEventClickListener { event, eventRect ->
            if (!dialog.isShowing) {
                val name = dialog.findViewById<TextView>(R.id.username)
                val date = dialog.findViewById<TextView>(R.id.date)
                name.text = "${event.name} ${event.lastName}"
                date.text =
                    "${monthTimeFormatter.format(event.startTime.timeInMillis)}" +
                            "-" +
                            "${monthTimeFormatter.format(event.endTime.timeInMillis)}"
                dialog.show()
            }
        }

        weekView.setEmptyViewClickListener {
            startActivity(AddUserActivity.newInstance(this, it))
        }
    }

    private fun initDialog(): Dialog {
        val dialog = Dialog(this)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_layout)
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels
        dialog.window.setLayout((width * .95).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list_users, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null)
            return false
        when (item.itemId) {
            R.id.itemFive -> {
                weekView.numberOfVisibleDays = 5
            }
            R.id.itemOne -> {
                weekView.numberOfVisibleDays = 1
            }
            R.id.itemAdd -> {
                startActivity(Intent(this, AddUserActivity::class.java))
            }
            R.id.itemShowAll -> {
                startActivity(Intent(this, ListUserActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
