package com.example.karma.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.karma.BuildConfig
import com.example.karma.R
import com.example.karma.base.AlertMenuListener
import com.example.karma.response.PCK
import com.google.gson.Gson
import id.zelory.compressor.Compressor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.IOException
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object utils {

    fun getCachePath(context: Context): String? {
        var path: String? = context.applicationInfo.dataDir + "/media"
        path = context.externalCacheDir!!.path
        val file = File(path)
        if (!file.exists()) {
            file.mkdir()
        }
        return path
    }

    fun getRealPathFromURI(uri: Uri?, context: Activity): String? {
        val projection =
            arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = context.managedQuery(uri, projection, null, null, null)
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    fun imageOptionDialog(mcontext: Activity, listener: AlertMenuListener) {
        val builder =
            AlertDialog.Builder(mcontext)
        builder.setCancelable(false)
        builder.setTitle("Select your option:")
        builder.setItems(
            R.array.options,
            DialogInterface.OnClickListener { dialog, which ->
                listener.onPositiveButtonClicked(
                    which
                )
            })
        builder.setNegativeButton(R.string.cancel,
            DialogInterface.OnClickListener { dialog, which ->
                //the user clicked on Cancel
            })
        val alert = builder.create()
        alert.show()
        //  builder.show();
        val nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
        nbutton.setTextColor(mcontext.resources.getColor(R.color.colorBlue))
    }

    fun showMessageAlert(
        message: String?,
        title: String?,
        context: Context?
    ) {
        val messageAlert =
            AlertDialog.Builder(context!!, R.style.CustomDialog)
        val v: View =
            LayoutInflater.from(context).inflate(R.layout.dialog_validation, null)
        messageAlert.setView(v)
        val tvTitle = v.findViewById<TextView>(R.id.tvTitle)
        tvTitle.text = title
        val tvMessage = v.findViewById<TextView>(R.id.tvMessage)
        tvMessage.text = message
        val tvOk = v.findViewById<TextView>(R.id.tvOk)
        val dialog: Dialog = messageAlert.create()
        tvOk.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    fun toJson(jsonObject: Any?): String? {
        return Gson().toJson(jsonObject)
    }

    fun fromJson(jsonString: String?, type: Type?): Any? {
        return Gson().fromJson(jsonString, type)
    }


    fun parseDateToddMMyyyy(time: String?): String? {
        val inputPattern = "dd-mm-yyyy"
        val outputPattern = "MMM dd YYYY"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)
        var date: Date? = null
        var str: String? = null
        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }

    fun showMessageAlertWithListener(
        message: String?,
        title: String?,
        context: Context?,
        listener: listenerForAlertDialog
    ) {
        val messageAlert =
            AlertDialog.Builder(context!!, R.style.CustomDialog)
        val v: View =
            LayoutInflater.from(context).inflate(R.layout.dailog_for_order_edit, null)
        messageAlert.setView(v)
        val tvTitle = v.findViewById<TextView>(R.id.tvTitle)
        tvTitle.text = title
        val tvMessage = v.findViewById<TextView>(R.id.tvMessage)
        tvMessage.text = message
        val tvOk = v.findViewById<TextView>(R.id.tvYes)
        val tvNo = v.findViewById<TextView>(R.id.tvNo)
        val dialog: Dialog = messageAlert.create()
        tvOk.setOnClickListener {
            listener.AlertDialogClick()
            dialog.dismiss()
        }
        tvNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


    interface listenerForAlertDialog{
        fun AlertDialogClick()
    }


    fun compressImage(image: File?, context: Context?): File? {
        var compressedImgFile: File? = null
        try {
            compressedImgFile = Compressor(context).compressToFile(image)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return compressedImgFile
    }

}