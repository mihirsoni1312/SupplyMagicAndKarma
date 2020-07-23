package com.example.karma.activities

import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.ImageViewCompat
import com.example.karma.R
import com.example.karma.base.BaseActivity
import com.example.karma.response.CommonResponse
import com.example.karma.retrofit.ApiClientForSeEmail
import com.example.karma.retrofit.ApiInterface
import com.example.karma.utils.CustomProgressbar
import com.example.karma.utils.PreferenceManager
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException

class ForgotPasswordActivity : BaseActivity() {


    lateinit var progressDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        btn_back.setOnClickListener {
            onBackPressed()
        }

        if (PreferenceManager.getAppId(this) == "2") {

            if (!PreferenceManager.getRegistrationScreenLogo(
                    this@ForgotPasswordActivity
                ).equals("")
            ) {
                Picasso.get()
                    .load(PreferenceManager.getRegistrationScreenLogo(this@ForgotPasswordActivity))
                    .into(img_forgotPass)
            }
        } else if (PreferenceManager.getAppId(this) == "1") {

            if (!PreferenceManager.getRegistrationScreenLogo(
                    this@ForgotPasswordActivity
                ).equals("")
            ) {
                Picasso.get()
                    .load(PreferenceManager.getRegistrationScreenLogo(this@ForgotPasswordActivity))
                    .into(img_forgotPass)
            }
        }


        changeIconColorTint(edt_emailId, PreferenceManager.getBackgroundColor(this))
        title_forgotPassword.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_emailId.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_emailId.setHintTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        btn_submit_forgotPass.setBackgroundColor(
            Color.parseColor(
                PreferenceManager.getBackgroundColor(
                    this
                )
            )
        )
        btn_submit_forgotPass.setTextColor(
            Color.parseColor(
                PreferenceManager.getButtonFontColor(
                    this
                )
            )
        )

        ImageViewCompat.setImageTintList(
            btn_back,
            ColorStateList.valueOf(Color.parseColor(PreferenceManager.getFontColor(this)))
        )

        btn_submit_forgotPass.setOnClickListener {

            if (edt_emailId.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter Email Id", Toast.LENGTH_SHORT).show()
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edt_emailId.text.toString())
                    .matches()
            ) {
                Toast.makeText(this, "Please enter valid Email", Toast.LENGTH_SHORT).show()
            } else {
                ApiCall()
            }

        }


    }

    fun changeIconColorTint(editText: EditText, color: String) {
        val drawables: Drawable = editText.compoundDrawables.get(0)
        var drawable: Drawable? = drawables
        drawable = DrawableCompat.wrap(drawable!!)
        DrawableCompat.setTint(drawable, Color.parseColor(color))
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
        editText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val mainIntent = Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(mainIntent)
    }

    private fun loadImageFromStorageKarma(path: String) {
        try {
            val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
            StrictMode.setThreadPolicy(policy)

            var xyz: File = File(path)

            if (xyz.exists()) {
                Log.e("APAPPAPAPA", "" + xyz.path)
                img_forgotPass.setImageURI(Uri.parse(xyz.path + "/registerbgKarma.jpg"))
            } else {
                Toast.makeText(this, "File Not Found !!", Toast.LENGTH_SHORT).show()
            }

        } catch (e: FileNotFoundException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            Log.e("", "" + e.message)
            e.printStackTrace()
        }
    }

    private fun loadImageFromStorageSuplyMagic(path: String) {
        try {
            val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
            StrictMode.setThreadPolicy(policy)

            var xyz: File = File(path)

            if (xyz.exists()) {
                Log.e("APAPPAPAPA", "" + xyz.path)
                img_forgotPass.setImageURI(Uri.parse(xyz.path + "/registerbgSuplyMagic.jpg"))
            } else {
                Toast.makeText(this, "File Not Found !!", Toast.LENGTH_SHORT).show()
            }

        } catch (e: FileNotFoundException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            Log.e("", "" + e.message)
            e.printStackTrace()
        }
    }


    fun ApiCall() {
        progressDialog = context.let { CustomProgressbar().show(it) }
        val myJason = JSONObject()

        myJason.put("ARS", "1")
        myJason.put("TI", edt_emailId.text)
        myJason.put("VRS", PreferenceManager.getTempVendorId(this))
        myJason.put("TYPE", "4")

        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClientForSeEmail.client.create(ApiInterface::class.java)
        val call = apiService.SendEmail(gsonObject)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                progressDialog.dismiss()
            }

            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                progressDialog.dismiss()
                if (response.isSuccessful) {

                    if (response.body()?.responseCode == 200)
                        finish()
                } else {
                    Toast.makeText(
                        this@ForgotPasswordActivity,
                        response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

    }

}
