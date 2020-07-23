package com.example.karma.activities

import android.annotation.SuppressLint
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
import com.example.karma.response.ChangePasswordResponse
import com.example.karma.retrofit.ApiClient
import com.example.karma.retrofit.ApiInterface
import com.example.karma.utils.AppIDConfig
import com.example.karma.utils.CustomProgressbar
import com.example.karma.utils.PreferenceManager
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_change_password.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException


class ChangePasswordActivity : BaseActivity() {

    var progressDialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)



        if (PreferenceManager.getAppId(this) == "2") {
            loadImageFromStorageKarma(PreferenceManager.getRegisterAbusulatPath(this))
        } else if (PreferenceManager.getAppId(this) == "1") {
            loadImageFromStorageSuplyMagic(PreferenceManager.getRegisterAbusulatPath(this))
        }


        btn_back.setOnClickListener {
            onBackPressed()
        }
        btn_changePassword.setOnClickListener {

            if (edt_old_password.text.toString().isEmpty()) {
                Toast.makeText(
                    this@ChangePasswordActivity,
                    "Please enter old password",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (edt_new_password.text.toString().isEmpty()) {
                Toast.makeText(
                    this@ChangePasswordActivity,
                    "Please enter new password",
                    Toast.LENGTH_SHORT
                ).show()
            }else if (!AppIDConfig.PASSWORD_PATTERN.matches(edt_new_password.text.toString())) {
                Toast.makeText(this, "Password must have at least one upper character and number", Toast.LENGTH_SHORT).show()
            }
            else if (edt_reEnter_password.text.toString().isEmpty()) {
                Toast.makeText(
                    this@ChangePasswordActivity,
                    "Please enter re-enter password",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (edt_new_password.text.toString() != edt_reEnter_password.text.toString()) {
                Toast.makeText(
                    this@ChangePasswordActivity,
                    "New Password and Re-Enter Password not match",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                changePasswordAPI()
            }
        }


        btn_changePassword.setBackgroundColor(
            Color.parseColor(
                PreferenceManager.getBackgroundColor(
                    this
                )
            )
        )
        btn_changePassword.setTextColor(Color.parseColor(PreferenceManager.getButtonFontColor(this)))

        edt_old_password.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_new_password.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_reEnter_password.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        title_changePass.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))

        tint(edt_old_password)
        tint(edt_new_password)
        tint(edt_reEnter_password)

        edt_old_password.setHintTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_new_password.setHintTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_reEnter_password.setHintTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        ImageViewCompat.setImageTintList(
            btn_back,
            ColorStateList.valueOf(Color.parseColor(PreferenceManager.getFontColor(this)))
        )
    }


    fun changePasswordAPI() {

        progressDialog = CustomProgressbar().show(this@ChangePasswordActivity)

        val myJason = JSONObject()
        myJason.put("userId", PreferenceManager.getId(this))
        myJason.put("oldPassword", edt_old_password.text.toString())
        myJason.put("newPassword", edt_new_password.text.toString())
        myJason.put("appId", AppIDConfig.SupplyMagicAppId)

        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.changePassword(gsonObject)
        call.enqueue(object : Callback<ChangePasswordResponse> {
            @SuppressLint("ShowToast")
            override fun onResponse(
                call: Call<ChangePasswordResponse>,
                response: Response<ChangePasswordResponse>
            ) {

                progressDialog!!.dismiss()
                if (response.body()!!.responseCode == 200) {
                    var msg: String = response.body()!!.message
                    Toast.makeText(
                        this@ChangePasswordActivity,
                        msg,
                        Toast.LENGTH_SHORT
                    ).show()

                    val mainIntent =
                        Intent(this@ChangePasswordActivity, MyAccountActivity::class.java)
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(mainIntent)

                } else if (response.body()!!.responseCode == 201) {
                    var msg: String = response.body()!!.message
                    Log.e("Response_Fail", "" + response.body()!!.message)
                    Toast.makeText(
                        this@ChangePasswordActivity,
                        msg,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {

                progressDialog!!.dismiss()
                Toast.makeText(
                    this@ChangePasswordActivity,
                    "" + t.message,
                    Toast.LENGTH_SHORT
                )
            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val mainIntent = Intent(this@ChangePasswordActivity, MyAccountActivity::class.java)
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
                img_changePasswordLogo.setImageURI(Uri.parse(xyz.path + "/registerbgKarma.jpg"))
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
                img_changePasswordLogo.setImageURI(Uri.parse(xyz.path + "/registerbgSuplyMagic.jpg"))
            } else {
                Toast.makeText(this, "File Not Found !!", Toast.LENGTH_SHORT).show()
            }

        } catch (e: FileNotFoundException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            Log.e("", "" + e.message)
            e.printStackTrace()
        }
    }

    fun tint(Et:EditText){
        var drawable: Drawable? = resources.getDrawable(R.drawable.icn_password_3x)
        drawable = DrawableCompat.wrap(drawable!!)
        DrawableCompat.setTint(drawable, Color.parseColor(PreferenceManager.getBackgroundColor(this)))
        Et.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
    }


}
