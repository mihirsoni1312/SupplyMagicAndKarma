package com.example.karma.activities

import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.widget.ImageViewCompat
import com.example.karma.R
import com.example.karma.base.BaseActivity
import com.example.karma.response.CommonResponse
import com.example.karma.response.updateUserByIdResponse.UpdateUserByIdResponse
import com.example.karma.retrofit.ApiClient
import com.example.karma.retrofit.ApiClientForSGP
import com.example.karma.retrofit.ApiClientForSeEmail
import com.example.karma.retrofit.ApiInterface
import com.example.karma.utils.AppIDConfig
import com.example.karma.utils.CustomProgressbar
import com.example.karma.utils.PreferenceManager
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_o_t_p_enter_screen.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException


class OTPEnterScreenActivity : BaseActivity() {

    lateinit var progressDialog: Dialog
    var number = ""
    var userId = ""
    var name = ""
    var email = ""
    var otp = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_o_t_p_enter_screen)

        btn_back.setOnClickListener {
            onBackPressed()
        }
        number = intent.getStringExtra("number")
        userId = intent.getStringExtra("UserId")
        name = intent.getStringExtra("name")
        email = intent.getStringExtra("email")


        otp = number.get(0).toString()
        otp += number.get(3).toString()
        otp += number.get(number.length - 2).toString()
        otp += number.get(number.length - 1).toString()

        textforMobilenumbr.text = "Enter 4 digit OTP number sent \nto Email"

        firstCard.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                outlineAmbientShadowColor =
                    Color.parseColor(PreferenceManager.getBackgroundColor(this@OTPEnterScreenActivity))
                outlineSpotShadowColor =
                    Color.parseColor(PreferenceManager.getBackgroundColor(this@OTPEnterScreenActivity))
                strokeColor =
                    Color.parseColor(PreferenceManager.getBackgroundColor(this@OTPEnterScreenActivity))
            }
        }

        secondOTP.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                outlineAmbientShadowColor =
                    Color.parseColor(PreferenceManager.getBackgroundColor(this@OTPEnterScreenActivity))
                outlineSpotShadowColor =
                    Color.parseColor(PreferenceManager.getBackgroundColor(this@OTPEnterScreenActivity))
                strokeColor =
                    Color.parseColor(PreferenceManager.getBackgroundColor(this@OTPEnterScreenActivity))
            }
        }

        thirdOTP.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                outlineAmbientShadowColor =
                    Color.parseColor(PreferenceManager.getBackgroundColor(this@OTPEnterScreenActivity))
                outlineSpotShadowColor =
                    Color.parseColor(PreferenceManager.getBackgroundColor(this@OTPEnterScreenActivity))
                strokeColor =
                    Color.parseColor(PreferenceManager.getBackgroundColor(this@OTPEnterScreenActivity))
            }
        }

        forthOTP.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                outlineAmbientShadowColor =
                    Color.parseColor(PreferenceManager.getBackgroundColor(this@OTPEnterScreenActivity))
                outlineSpotShadowColor =
                    Color.parseColor(PreferenceManager.getBackgroundColor(this@OTPEnterScreenActivity))
                strokeColor =
                    Color.parseColor(PreferenceManager.getBackgroundColor(this@OTPEnterScreenActivity))
            }
        }

        edt_firstOTP.setTextColor(
            Color.parseColor(
                PreferenceManager.getBackgroundColor(
                    this
                )
            )
        )
        edt_secondOTP.setTextColor(
            Color.parseColor(
                PreferenceManager.getBackgroundColor(
                    this
                )
            )
        )

        edt_thied_otp.setTextColor(
            Color.parseColor(
                PreferenceManager.getBackgroundColor(
                    this
                )
            )
        )

        edt_forth_otp.setTextColor(
            Color.parseColor(
                PreferenceManager.getBackgroundColor(
                    this
                )
            )
        )


        edt_firstOTP.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable) {
                if (edt_firstOTP.text.toString().length === 1) {
                    edt_secondOTP.requestFocus()
                }
            }
        })

        edt_secondOTP.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                if (edt_secondOTP.text.toString().length === 0) {
                    edt_firstOTP.requestFocus()
                }
            }

            override fun afterTextChanged(editable: Editable) {
                if (edt_secondOTP.text.toString().length === 1) {
                    edt_thied_otp.requestFocus()
                }
            }
        })

        edt_thied_otp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                if (edt_thied_otp.text.toString().length === 0) {
                    edt_secondOTP.requestFocus()
                }
            }

            override fun afterTextChanged(editable: Editable) {
                if (edt_thied_otp.text.toString().length === 1) {
                    edt_forth_otp.requestFocus()
                }
            }
        })

        edt_forth_otp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                if (edt_forth_otp.text.toString().length === 0) {
                    edt_thied_otp.requestFocus()
                }
            }

            override fun afterTextChanged(editable: Editable) {
                // We can call api to verify the OTP here or on an explicit button click
            }
        })




        if (PreferenceManager.getAppId(this) == "2") {
            Picasso.get()
                .load(PreferenceManager.getRegistrationScreenLogo(this@OTPEnterScreenActivity))
                .into(img_verifyNumber)
//            loadImageFromStorageKarma(PreferenceManager.getRegisterAbusulatPath(this))
        } else if (PreferenceManager.getAppId(this) == "1") {
//            loadImageFromStorageSuplyMagic(PreferenceManager.getRegisterAbusulatPath(this))

            Picasso.get()
                .load(PreferenceManager.getRegistrationScreenLogo(this@OTPEnterScreenActivity))
                .into(img_verifyNumber)
        }


        title_otp.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        btn_submit_otp.setBackgroundColor(
            Color.parseColor(
                PreferenceManager.getBackgroundColor(
                    this
                )
            )
        )
        btn_submit_otp.setTextColor(
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

        btn_submit_otp.setOnClickListener {


//            Toast.makeText(this, otp, Toast.LENGTH_SHORT).show()

            if (otp.get(0).toString().equals(edt_firstOTP.text.toString()) && otp.get(1).toString()
                    .equals(edt_secondOTP.text.toString()) && otp.get(2).toString()
                    .equals(edt_thied_otp.text.toString()) && otp.get(3).toString()
                    .equals(edt_forth_otp.text.toString())
            ) {
                updateUserByIdApiCall()
            } else {
                Toast.makeText(this, "Please enter valid OTP", Toast.LENGTH_SHORT).show()
            }
        }
        ApiCall()
    }


    override fun onBackPressed() {
        if (!LoginActivity.fromLoginScreen) {
            super.onBackPressed()
            val mainIntent = Intent(this@OTPEnterScreenActivity, RegisterActivity::class.java)
            mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(mainIntent)
        }
    }

    private fun loadImageFromStorageKarma(path: String) {
        try {
            val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
            StrictMode.setThreadPolicy(policy)

            var xyz: File = File(path)

            if (xyz.exists()) {
                Log.e("APAPPAPAPA", "" + xyz.path)
                img_verifyNumber.setImageURI(Uri.parse(xyz.path + "/registerbgKarma.jpg"))
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
                img_verifyNumber.setImageURI(Uri.parse(xyz.path + "/registerbgSuplyMagic.jpg"))
            } else {
                Toast.makeText(this, "File Not Found !!", Toast.LENGTH_SHORT).show()
            }

        } catch (e: FileNotFoundException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            Log.e("", "" + e.message)
            e.printStackTrace()
        }
    }


    fun updateUserByIdApiCall() {
        progressDialog = context.let { CustomProgressbar().show(it) }
        val myJason = JSONObject()

        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("userId", userId)
        myJason.put("name", name)
        myJason.put("isVerified", true)
        myJason.put("userImage", "")
        myJason.put("defaultPaymentType", "1")

        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject = jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.updateUserById(gsonObject)
        call.enqueue(object : Callback<UpdateUserByIdResponse> {
            override fun onFailure(call: Call<UpdateUserByIdResponse>, t: Throwable) {
                progressDialog.dismiss()
            }

            override fun onResponse(
                call: Call<UpdateUserByIdResponse>,
                response: Response<UpdateUserByIdResponse>
            ) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    val mainIntent = Intent(this@OTPEnterScreenActivity, LoginActivity::class.java)
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(mainIntent)
                    Toast.makeText(
                        this@OTPEnterScreenActivity,
                        "Your verification successfully. please login now",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@OTPEnterScreenActivity,
                        response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

    }

    fun ApiCall() {
        progressDialog = context.let { CustomProgressbar().show(it) }
        val myJason = JSONObject()

        myJason.put("VRS", PreferenceManager.getTempVendorId(this))
        myJason.put("ARS", "1")
        myJason.put("TYPE", "1")
        myJason.put("TI", email)
        myJason.put("CN", name)
        myJason.put("VAL", otp)

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

//                    if (response.body()?.responseCode == 200)
//                        Toast.makeText(
//                            this@OTPEnterScreenActivity,
//                            response.message(),
//                            Toast.LENGTH_SHORT
//                        ).show()
                } else {
                    Toast.makeText(
                        this@OTPEnterScreenActivity,
                        response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

    }
}
