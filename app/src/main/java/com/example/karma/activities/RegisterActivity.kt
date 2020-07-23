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
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.ImageViewCompat
import com.example.karma.R
import com.example.karma.base.BaseActivity
import com.example.karma.response.CommonResponse
import com.example.karma.retrofit.ApiClient
import com.example.karma.retrofit.ApiInterface
import com.example.karma.utils.AppIDConfig
import com.example.karma.utils.AppIDConfig.Companion.PASSWORD_PATTERN
import com.example.karma.utils.CustomProgressbar
import com.example.karma.utils.PreferenceManager
import com.example.karma.utils.toast
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException

class RegisterActivity : BaseActivity() {
    var progressDialog: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_register)



        if (PreferenceManager.getAppId(this) == "2") {
            if(!PreferenceManager.getRegistrationScreenLogo(
                    this@RegisterActivity
                ).equals("")) {
                Picasso.get().load(PreferenceManager.getRegistrationScreenLogo(this@RegisterActivity)).into(img_register)
            }

        } else if (PreferenceManager.getAppId(this) == "1") {

            if(!PreferenceManager.getRegistrationScreenLogo(
                    this@RegisterActivity
                ).equals("")) {
                Picasso.get().load(PreferenceManager.getRegistrationScreenLogo(this@RegisterActivity)).into(img_register)
            }
        }



        btn_signUp.setBackgroundColor(Color.parseColor(PreferenceManager.getBackgroundColor(this)))
        btn_signUp.setTextColor(Color.parseColor(PreferenceManager.getButtonFontColor(this)))
        txt_SignInGo.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_fullName.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_email.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_cellPhone.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_password.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_address.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_zip.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        txt_termsStaticRegister.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        txt_termsandPolicy.setTextColor(Color.parseColor(PreferenceManager.getBackgroundColor(this)))

        edt_fullName.setHintTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_email.setHintTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_cellPhone.setHintTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_password.setHintTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_address.setHintTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_zip.setHintTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        ImageViewCompat.setImageTintList(
            img_backToSignIn,
            ColorStateList.valueOf(Color.parseColor(PreferenceManager.getFontColor(this)))
        )


        changeIconColorTint(edt_fullName, PreferenceManager.getBackgroundColor(this))
        changeIconColorTint(edt_email, PreferenceManager.getBackgroundColor(this))
        changeIconColorTint(edt_cellPhone, PreferenceManager.getBackgroundColor(this))
        changeIconColorTint(edt_password, PreferenceManager.getBackgroundColor(this))
        changeIconColorTint(edt_address, PreferenceManager.getBackgroundColor(this))
        changeIconColorTint(edt_zip, PreferenceManager.getBackgroundColor(this))




        btn_signUp.setOnClickListener {

            if (edt_fullName.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter Full Name", Toast.LENGTH_SHORT).show()
            } else if (edt_email.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show()
            } else if (edt_cellPhone.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter Cell Phone", Toast.LENGTH_SHORT).show()
            } else if (edt_password.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show()
            }else if (!AppIDConfig.PASSWORD_PATTERN.matches(edt_password.text.toString())) {
                Toast.makeText(this, "Password must have at least one upper character and number", Toast.LENGTH_SHORT).show()
            } else if (edt_address.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter Address", Toast.LENGTH_SHORT).show()
            } else if (edt_zip.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter ZipCode", Toast.LENGTH_SHORT).show()
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edt_email.text.toString())
                    .matches()
            ) {
                Toast.makeText(this, "Please enter valid Email", Toast.LENGTH_SHORT).show()
            } else {


                progressDialog = CustomProgressbar().show(this@RegisterActivity)


                val myJason = JSONObject()
                myJason.put("name", edt_fullName.text.toString())
                myJason.put("vendorId", PreferenceManager.getTempVendorId(this))
                myJason.put("password", edt_password.text.toString())
                myJason.put("emailAddress", edt_email.text.toString())
                myJason.put("mobileNumber", edt_cellPhone.text.toString())
                myJason.put("address", edt_address.text.toString())
                myJason.put("zipCode", edt_zip.text.toString())
                myJason.put("appId", AppIDConfig.SupplyMagicAppId)
                myJason.put("isActive", true)

                val obj: JSONObject = myJason
                val jsonParser = JsonParser()
                val gsonObject =
                    jsonParser.parse(obj.toString()) as JsonObject

                val apiService = ApiClient.client.create(ApiInterface::class.java)
                val call = apiService.registration(gsonObject)
                call.enqueue(object : Callback<CommonResponse> {
                    @SuppressLint("ShowToast")
                    override fun onResponse(
                        call: Call<CommonResponse>,
                        response: Response<CommonResponse>
                    ) {
                        progressDialog!!.dismiss()
                        if (response.body()!!.responseCode == 200) {
                            var msg: String = response.body()!!.result

                            toast("OTP has been sent on your registered email id")
                            val mainIntent = Intent(this@RegisterActivity, OTPEnterScreenActivity::class.java)
                            mainIntent.putExtra("number", edt_cellPhone.text.toString())
                            mainIntent.putExtra("UserId", response.body()?.result)
                            mainIntent.putExtra("name", edt_fullName.text.toString())
                            mainIntent.putExtra("email", edt_email.text.toString())
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(mainIntent)

                        } else {
                            var msg: String = response.body()!!.result
                            Log.e("Response_Fail", "" + response.body()!!.result)
                            Toast.makeText(
                                this@RegisterActivity,
                                msg,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<CommonResponse>, t: Throwable) {

                        progressDialog!!.dismiss()
                        Toast.makeText(
                            this@RegisterActivity,
                            "" + t.message,
                            Toast.LENGTH_SHORT
                        )
                    }
                })
            }
        }



        ll_goToLoginPage.setOnClickListener {
            val mainIntent = Intent(this, LoginActivity::class.java)
            startActivity(mainIntent)
        }

        txt_termsandPolicy.setOnClickListener {
            val intent: Intent = Intent(this, CMSActivity::class.java)
            intent.putExtra("type", "2")
            startActivity(intent)

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

    private fun loadImageFromStorageKarma(path: String) {
        try {
            val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
            StrictMode.setThreadPolicy(policy)

            var xyz: File = File(path)

            if (xyz.exists()) {
                Log.e("APAPPAPAPA", "" + xyz.path)
                img_register.setImageURI(Uri.parse(xyz.path + "/registerbgKarma.jpg"))
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
                img_register.setImageURI(Uri.parse(xyz.path + "/registerbgSuplyMagic.jpg"))
            } else {
                Toast.makeText(this, "File Not Found !!", Toast.LENGTH_SHORT).show()
            }

        } catch (e: FileNotFoundException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            Log.e("", "" + e.message)
            e.printStackTrace()
        }
    }


}
