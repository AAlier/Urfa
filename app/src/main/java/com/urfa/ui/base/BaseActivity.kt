package com.urfa.ui.base

import android.app.ProgressDialog
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.urfa.R

abstract class BaseActivity : AppCompatActivity() {
    private val progressBar by lazy {
        val progressBar = ProgressDialog(this)
        progressBar.setMessage(getString(R.string.loading))
        progressBar.setCancelable(false)
        progressBar
    }

    fun showProgressBar() {
        runOnUiThread {
            progressBar.show()
        }
    }

    fun hideProgressBar() {
        runOnUiThread {
            progressBar.hide()
        }
    }

    fun onError(error: String?) {
        runOnUiThread {
            Snackbar.make(
                findViewById(android.R.id.content),
                error ?: getString(R.string.error),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        hideProgressBar()
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