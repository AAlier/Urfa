package com.urfa.ui.base

import android.app.Activity
import android.content.Intent
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.urfa.R
import androidx.core.content.FileProvider
import java.io.File

abstract class BaseActivity : AppCompatActivity() {

    fun showProgressBar() {

    }

    fun hideProgressBar() {

    }

    fun onError(error: String?) {
        Snackbar.make(
            findViewById(android.R.id.content),
            error ?: getString(R.string.error),
            Snackbar.LENGTH_LONG
        ).show()
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

    protected fun pickFileExplorer() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.putExtra("CONTENT_TYPE", "*/*");

        val chooserIntent: Intent
        if (packageManager.resolveActivity(intent, 0) != null) {
            // it is device with Samsung file manager
            chooserIntent = Intent.createChooser(intent, "Open file")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(intent))
        } else {
            chooserIntent = Intent.createChooser(intent, "Open file")
        }

        try {
            startActivityForResult(chooserIntent, PICKFILE_REQUEST_CODE)
        } catch (ex: android.content.ActivityNotFoundException) {
            ex.printStackTrace()
            Toast.makeText(
                applicationContext,
                "No suitable File Manager was found.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PICKFILE_REQUEST_CODE -> {
                if (Activity.RESULT_OK == resultCode) {
                    val uri = data?.getData()
                    val cr = this.contentResolver
                    val mime = cr.getType(uri)
                }
            }
        }
    }

    fun openFile(filename: String) {
        val path = File(filesDir, "dl")
        val file = File(path, filename)

        // Get URI and MIME type of file
        val uri = FileProvider.getUriForFile(this, application.packageName + ".fileprovider", file)
        val mime = contentResolver.getType(uri)

        // Open file with user selected app
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(uri, mime)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }

    companion object {
        val PICKFILE_REQUEST_CODE = 1
    }
}