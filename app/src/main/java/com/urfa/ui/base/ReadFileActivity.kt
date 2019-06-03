package com.urfa.ui.base

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.urfa.R
import com.urfa.model.SelectionEntity
import com.urfa.ui.ImageViewActivity
import permissions.dispatcher.*
import java.io.*
import java.util.*

@RuntimePermissions
abstract class ReadFileActivity : BaseActivity() {
    val list by lazy {
        arrayListOf<SelectionEntity>(
            SelectionEntity(getString(R.string.image), R.drawable.ic_image),
            SelectionEntity(getString(R.string.camera), R.drawable.ic_camera)
        )
    }
    private val adapter by lazy { PickerAdapter(this, list) }
    private lateinit var fileName: String

    protected fun openFromGallery(path: String) {
        startActivity(ImageViewActivity.newInstance(this, path))
    }

    protected fun showChooserDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.view_chooser_gallery_camera, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()

        val listView = dialogView.findViewById(R.id.listView) as ListView
        listView.adapter = adapter
        listView.setOnItemClickListener { parent, view, position, id ->
            when (position) {
                0 -> {
                    pickPhotoFromGalleryWithPermissionCheck()
                }
                1 -> {
                    takePhotoFromCameraWithPermissionCheck()
                }
            }
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    open fun takePhotoFromCamera() {
        fileName = System.nanoTime().toString()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val uri = getCaptureImageOutputUri(this, fileName)
        if (uri != null) {
            val file = File(uri.path)
            if (Build.VERSION.SDK_INT >= 24) {
                intent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    FileProvider.getUriForFile(this, "${this.packageName}.provider", file)
                )
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } else intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            startActivityForResult(intent, OptionType.RESULT_CAMERA.code)
        }
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun pickPhotoFromGallery() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        intent.type = "image/*"
        startActivityForResult(intent, OptionType.RESULT_GALLERY.code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (OptionType.valueByCode(requestCode)) {
            OptionType.RESULT_CAMERA -> {
                val uri = getPickImageResultUri(this, data, fileName)
                val uriFile = getNormalizedUri(this, uri)
                val file = File(uriFile!!.path)
                onReceiveImageFile(file.path)
            }
            OptionType.RESULT_GALLERY -> {
                if (data != null && data.data != null) {
                    val fileName = getImagePathFromInputStreamUri(this, data.data!!)
                    val file = File(fileName)
                    if (file.exists()) {
                        onReceiveImageFile(file.path)
                    }
                }
            }
            OptionType.UNKNOWN -> {
                Log.e(this.localClassName, "Did not find request code")
            }
        }
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun showRationaleForLocation(request: PermissionRequest) {
        AlertDialog.Builder(this)
            .setMessage(R.string.rationale_external_storage_permission)
            .setPositiveButton(android.R.string.yes) { _, _ -> request.proceed() }
            .setNegativeButton(android.R.string.no) { _, _ -> request.cancel() }
            .show();
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    fun showRationaleForCamera(request: PermissionRequest) {
        AlertDialog.Builder(this)
            .setMessage(R.string.rationale_camera_permission)
            .setPositiveButton(android.R.string.yes) { _, _ -> request.proceed() }
            .setNegativeButton(android.R.string.no) { _, _ -> request.cancel() }
            .show();
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    fun showDeniedPermission() {
        finish()
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    fun showNeverAskForPermission() {
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun getCaptureImageOutputUri(context: Context, fileName: String): Uri? {
        var outputFileUri: Uri? = null
        val getImage = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (getImage != null) {
            outputFileUri = Uri.fromFile(File(getImage.path, "$fileName.jpeg"))
        }
        return outputFileUri
    }

    private fun getPickImageResultUri(context: Context, data: Intent?, fileName: String): Uri? {
        var isCamera = true
        if (data != null && data.data != null) {
            val action = data.action
            isCamera = action != null && action == MediaStore.ACTION_IMAGE_CAPTURE
        }
        return if (isCamera || data!!.data == null)
            getCaptureImageOutputUri(context, fileName)
        else
            data.data
    }

    private fun getNormalizedUri(context: Context, uri: Uri?): Uri? {
        return if (uri != null && uri.toString().contains("content:"))
            Uri.fromFile(getPath(context, uri, MediaStore.Images.Media.DATA))
        else
            uri
    }

    private fun getPath(context: Context, uri: Uri, column: String): File? {
        val columns = arrayOf(column)
        val cursor = context.contentResolver.query(uri, columns, null, null, null) ?: return null
        val column_index = cursor.getColumnIndexOrThrow(column)
        cursor.moveToFirst()
        val path = cursor.getString(column_index)
        cursor.close()
        return File(path)
    }

    private fun getImagePathFromInputStreamUri(context: Context, uri: Uri): String? {
        var inputStream: InputStream? = null
        var filePath: String? = null

        if (uri.authority != null) {
            try {
                inputStream = context.contentResolver.openInputStream(uri) // context needed
                val photoFile = createTemporalFileFrom(context, inputStream)

                filePath = photoFile!!.path

            } catch (e: FileNotFoundException) {
                // log
            } catch (e: IOException) {
                // log
            } finally {
                try {
                    inputStream!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }

        return filePath
    }

    @Throws(IOException::class)
    private fun createTemporalFileFrom(context: Context, inputStream: InputStream?): File? {
        var targetFile: File? = null

        if (inputStream != null) {
            var read: Int
            val buffer = ByteArray(8 * 1024)

            targetFile = createTemporalFile(context)
            val outputStream = FileOutputStream(targetFile)


            while (true) {
                read = inputStream.read(buffer)
                if (read == -1)
                    break
                outputStream.write(buffer, 0, read)
            }
            outputStream.flush()

            try {
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        return targetFile
    }

    private fun createTemporalFile(
        context: Context,
        filePath: String = Calendar.getInstance().timeInMillis.toString()
    ): File {
        return File(context.filesDir, "$filePath.jpg") // context needed
    }

    abstract fun onReceiveImageFile(path: String)

    private enum class OptionType(val code: Int) {
        RESULT_CAMERA(1),
        RESULT_GALLERY(2),
        UNKNOWN(-1);

        companion object {
            fun valueByCode(t: Int): OptionType {
                return values().firstOrNull { it.code == t } ?: UNKNOWN
            }
        }
    }

}