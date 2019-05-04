package com.urfa.ui.base

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.urfa.R

abstract class BaseActivity : AppCompatActivity() {

    fun showProgressBar(){

    }

    fun hideProgressBar(){

    }

    fun onError(error: String?){
        Snackbar.make(findViewById(android.R.id.content), error ?: getString(R.string.error), Snackbar.LENGTH_LONG).show()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null) {
            return false
        }
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}