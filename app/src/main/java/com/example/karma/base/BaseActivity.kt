package com.example.karma.base

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.view.MotionEvent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.karma.R
import com.example.karma.utils.utils
import permissions.dispatcher.*
import java.io.File

@SuppressLint("Registered")
@RuntimePermissions
open class BaseActivity : AppCompatActivity(), AlertMenuListener {
    private val CODE_REQUEST_PERMISSION_SETTING = 12
    val CODE_CAMERA_REQUEST = 3006
    val CODE_GALLERY_REQUEST = 3007
    var CAMERA_PATH: String? = null


    val activity: Activity
        get() = this

    val context: Context
        get() = this


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    @SuppressLint("NoCorrespondingNeedsPermission")
    @OnShowRationale(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    fun OnShowRationale(request: PermissionRequest) {
        AlertDialog.Builder(this, R.style.AlertDialogTheme)
            .setMessage(R.string.permission_msg)
            .setPositiveButton(R.string.button_allow) { _, _ -> request.proceed() }
            .setNegativeButton(R.string.button_deny) { _, _ -> request.cancel() }
            .show()
    }


    @OnPermissionDenied(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    open fun OnPermissionDenied() {
    }


    @OnNeverAskAgain(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    open fun OnNeverAsk() {
        Settingdialog()
    }


    fun Settingdialog() {
        AlertDialog.Builder(this, R.style.AlertDialogTheme)
            .setMessage(R.string.permission_msg)
            .setCancelable(true)
            .setPositiveButton(R.string.action_settings) { _, _ -> redirectSetting(this) }
            .show()
    }

    open fun redirectSetting(activity: AppCompatActivity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri =
            Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        startActivityForResult(intent, CODE_REQUEST_PERMISSION_SETTING)
    }

    open fun openCamera(Comefrom: Int) {
        CameraPermissionWithPermissionCheck()
    }


    override fun onPositiveButtonClicked(index: Int) {
        if (index == 0) {
            openCamera()
        } else if (index == 1) {
            galleryClick()
        }
    }


    private fun openCamera() {
        try {
            val selectedFile = File(utils.getCachePath(this), "pictures")
            if (selectedFile.exists()) {
                selectedFile.delete()
            }
            selectedFile.mkdirs()
            val file = File.createTempFile("temp", ".jpg", selectedFile)
            CAMERA_PATH = file.absolutePath
            val apkURI = FileProvider.getUriForFile(
                applicationContext,
                applicationContext.packageName.toString() + ".provider",
                file
            )
            val intentPicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, apkURI)
            intentPicture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(intentPicture, CODE_CAMERA_REQUEST)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun galleryClick() {

        var i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(i, CODE_GALLERY_REQUEST)
    }

    @NeedsPermission(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    fun CameraPermission() {
        utils.imageOptionDialog(this, this)
    }
}

interface AlertMenuListener {
    fun onPositiveButtonClicked(index: Int)
}
