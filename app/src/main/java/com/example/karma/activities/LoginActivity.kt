package com.example.karma.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.ImageViewCompat
import com.example.karma.R
import com.example.karma.base.BaseActivity
import com.example.karma.response.LoginResponse
import com.example.karma.retrofit.ApiClient
import com.example.karma.retrofit.ApiInterface
import com.example.karma.utils.AppIDConfig
import com.example.karma.utils.CustomProgressbar
import com.example.karma.utils.PreferenceManager
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : BaseActivity() {


    var progressDialog: Dialog? = null
   companion object{ var fromLoginScreen = false}

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_login)

        if (PreferenceManager.getAppId(this) == "2") {

            if (!PreferenceManager.getLoginScreeLogo(
                    this@LoginActivity
                ).equals("")
            ) {
                Picasso.get()
                    .load(
                        PreferenceManager.getLoginScreeLogo(
                            this@LoginActivity
                        )
                    )
                    .into(img_login)
            }//            loadImageFromStorageKarma(PreferenceManager.getLoginAbusulatPath(this))
        } else if (PreferenceManager.getAppId(this) == "1") {
//            loadImageFromStorageSuplyMagic(PreferenceManager.getLoginAbusulatPath(this))

            if (!PreferenceManager.getLoginScreeLogo(
                    this@LoginActivity
                ).equals("")
            ) {
                Picasso.get().load(PreferenceManager.getLoginScreeLogo(this@LoginActivity))
                    .into(img_login)
            }
        }



        btn_login.setBackgroundColor(Color.parseColor(PreferenceManager.getBackgroundColor(this)))
        btn_login.setTextColor(Color.parseColor(PreferenceManager.getButtonFontColor(this)))
        txt_termsStatic.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        txt_termsAndCondition.setTextColor(
            Color.parseColor(
                PreferenceManager.getBackgroundColor(
                    this
                )
            )
        )
        txtSignUpGo.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        txt_forgotPassword.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_enterCellPhone.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_enterPassword.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_enterCellPhone.setHintTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_enterPassword.setHintTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))

        ImageViewCompat.setImageTintList(
            img_next,
            ColorStateList.valueOf(Color.parseColor(PreferenceManager.getFontColor(this)))
        )




        changeIconColorTint(edt_enterCellPhone, PreferenceManager.getBackgroundColor(this))
        changeIconColorTint(edt_enterPassword, PreferenceManager.getBackgroundColor(this))

        ll_goToRegisterPage.setOnClickListener {

            val mainIntent = Intent(this, RegisterActivity::class.java)
            startActivity(mainIntent)

        }

        btn_login.setOnClickListener {

            login()

        }

        txt_forgotPassword.setOnClickListener {

            val mainIntent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(mainIntent)
        }

        txt_termsAndCondition.setOnClickListener {
            val intent = Intent(this, CMSActivity::class.java)
            intent.putExtra("type", "2")
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    @SuppressLint("ResourceType")
    private fun setTextViewDrawableColor(color: String) {
        for (drawable in edt_enterCellPhone.compoundDrawables) {
            if (drawable != null) {
                drawable.colorFilter = PorterDuffColorFilter(
                    getColor(Color.parseColor(color)),
                    PorterDuff.Mode.SRC_IN
                )
            }
        }
    }

    private fun login() {
        if (edt_enterCellPhone.text.toString().isEmpty()) {
            Toast.makeText(this, "Please enter Cell Phone", Toast.LENGTH_SHORT).show()
        } else if (edt_enterPassword.text.toString().isEmpty()) {
            Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show()
        } else {

            progressDialog = CustomProgressbar().show(this@LoginActivity)
            val myJason = JSONObject()
            myJason.put("emailorphone", edt_enterCellPhone.text.toString())
            myJason.put("password", edt_enterPassword.text.toString())
            myJason.put("appId", AppIDConfig.SupplyMagicAppId)

            val obj: JSONObject = myJason
            val jsonParser = JsonParser()
            val gsonObject =
                jsonParser.parse(obj.toString()) as JsonObject

            val apiService = ApiClient.client.create(ApiInterface::class.java)
            val call = apiService.login(gsonObject)
            call.enqueue(object : Callback<LoginResponse> {
                @SuppressLint("ShowToast")
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {

                    progressDialog!!.dismiss()
                    if (response.body()!!.responseCode == 200) {


                        PreferenceManager.setEmail(
                            this@LoginActivity,
                            response.body()!!.result.email
                        )
                        PreferenceManager.setsTokan(
                            this@LoginActivity,
                            response.body()!!.result.sToken
                        )
                        PreferenceManager.setAppId(
                            this@LoginActivity,
                            response.body()!!.result.appId
                        )
                        PreferenceManager.setId(this@LoginActivity, response.body()!!.result._id)
                        PreferenceManager.setmobileNumber(
                            this@LoginActivity,
                            response.body()!!.result.mobileNumber
                        )
                        PreferenceManager.setname(
                            this@LoginActivity,
                            response.body()!!.result.profile.name
                        )
                        PreferenceManager.setaddress(
                            this@LoginActivity,
                            response.body()!!.result.profile.address
                        )
                        PreferenceManager.setzipCode(
                            this@LoginActivity,
                            response.body()!!.result.profile.zipCode
                        )
                        PreferenceManager.setuserImage(
                            this@LoginActivity,
                            response.body()!!.result.profile.userImage
                        )
                        PreferenceManager.setisLogin(
                            this@LoginActivity,
                            true
                        )

                        if (response.body()?.result?.profile?.isVerified!!) {
                            val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(mainIntent)
                        } else {
                            PreferenceManager.clear(this@LoginActivity)
                            fromLoginScreen = true
                            val mainIntent =
                                Intent(this@LoginActivity, OTPEnterScreenActivity::class.java)
                            mainIntent.putExtra("number", response.body()?.result?.mobileNumber)
                            mainIntent.putExtra("UserId", response.body()?.result?._id)
                            mainIntent.putExtra("name", response.body()?.result?.profile?.name)
                            mainIntent.putExtra("email", response.body()?.result?.email)
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(mainIntent)
                        }


                    } else if (response.body()!!.responseCode == 514) {
                        var msg: String = response.body()!!.message
                        Log.e("Response_Fail", "" + response.body()!!.message)
                        Toast.makeText(
                            this@LoginActivity,
                            msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    progressDialog!!.dismiss()

                    Toast.makeText(
                        this@LoginActivity,
                        "" + t.message,
                        Toast.LENGTH_SHORT
                    )
                }
            })
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
}
