package com.example.karma.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.core.widget.ImageViewCompat
import com.example.karma.R
import com.example.karma.base.BaseActivity
import com.example.karma.response.updateUserByIdResponse.UpdateUserByIdResponse
import com.example.karma.response.userImage.UserImage
import com.example.karma.retrofit.ApiClient
import com.example.karma.retrofit.ApiClientForSGP
import com.example.karma.retrofit.ApiInterface
import com.example.karma.utils.AppIDConfig
import com.example.karma.utils.CustomProgressbar
import com.example.karma.utils.PreferenceManager
import com.example.karma.utils.utils
import com.example.karma.utils.utils.compressImage
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_my_account.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


class MyAccountActivity : BaseActivity() {

    var selectedFile = ""
    lateinit var progressDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this@MyAccountActivity.setTheme(R.style.AppThemeeditText)
        setContentView(R.layout.activity_my_account)

        edt_fullName.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_email.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_cell_phone.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_password.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_otherAddress.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_zipCode.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        txt_save.setTextColor(Color.parseColor(PreferenceManager.getBackgroundColor(this)))
        txt_changePassword.setTextColor(Color.parseColor(PreferenceManager.getBackgroundColor(this)))
        title_myAccount.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))


        ImageViewCompat.setImageTintList(
            btn_back,
            ColorStateList.valueOf(Color.parseColor(PreferenceManager.getFontColor(this)))
        )

        edt_fullName.setHintTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_email.setHintTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_cell_phone.setHintTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_password.setHintTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_otherAddress.setHintTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_zipCode.setHintTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))

        btn_add_address.setBackgroundColor(
            Color.parseColor(
                PreferenceManager.getBackgroundColor(
                    this
                )
            )
        )
        btn_add_address.setTextColor(Color.parseColor(PreferenceManager.getButtonFontColor(this)))

        btn_back.setOnClickListener {
            onBackPressed()
        }

        txt_changePassword.setOnClickListener {
            val mainIntent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(mainIntent)

        }
        btn_add_address.setOnClickListener {
            val mainIntent = Intent(this, AddressListActivity::class.java)
            startActivity(mainIntent)
        }

        edt_fullName.setText(PreferenceManager.getname(this))
        edt_email.setText(PreferenceManager.getEmail(this))
        edt_cell_phone.setText(PreferenceManager.getmobileNumber(this))
        edt_otherAddress.setText(PreferenceManager.getaddress(this))
        edt_zipCode.setText(PreferenceManager.getzipCode(this))


        if (!PreferenceManager.getuserImage(this).equals("")) {
            Picasso.get()
                .load("http://207.244.244.64/UserImages/" + PreferenceManager.getuserImage(this))
                .into(profileImage)
        }
        ivOpenCamera.setOnClickListener {
            openCamera(5)
        }

        txt_save.setOnClickListener {
            if (!selectedFile.equals(""))
                updateUserByIdApiCall()
        }
    }

    override fun onBackPressed() {
        finish()
//        val mainIntent = Intent(this@MyAccountActivity, MainActivity::class.java)
//        mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//        startActivity(mainIntent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            Picasso.get()
                .load(File(CAMERA_PATH))
                .into(profileImage)
            var i =compressImage(File((CAMERA_PATH)),this@MyAccountActivity)
            val bm = BitmapFactory.decodeFile(i?.absolutePath)
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos) // bm is the bitmap object

            val b: ByteArray = baos.toByteArray()

            val encodedImage: String = Base64.encodeToString(b, Base64.DEFAULT)

            updateImage(encodedImage)
            selectedFile = encodedImage

        } else if (requestCode == CODE_GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            Log.d("mytag", "Data gallery : " + data!!.data)

            if (data.data != null) {
                try {


                    val uri: Uri? = data.data
                    val filePath: String? = utils.getRealPathFromURI(uri, this)

                    Picasso.get()
                        .load(File(filePath))
                        .into(profileImage)

                    var i =compressImage(File((filePath)),this@MyAccountActivity)
                    val bm = BitmapFactory.decodeFile(i?.absolutePath)
                    val baos = ByteArrayOutputStream()
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos) // bm is the bitmap object
                    val b: ByteArray = baos.toByteArray()
                    val encodedImage: String = Base64.encodeToString(b, Base64.DEFAULT)
                    updateImage(encodedImage)
                    selectedFile = encodedImage

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun updateUserByIdApiCall() {
        progressDialog = context.let { CustomProgressbar().show(it) }
        val myJason = JSONObject()

        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("userId", PreferenceManager.getId(context))
        myJason.put("name", edt_fullName.text)
        myJason.put("isVerified", true)
        myJason.put(
            "userImage", PreferenceManager.getuserImage(
                this@MyAccountActivity
            )
        )
        myJason.put("defaultPaymentType", "1")

        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

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

                    PreferenceManager.setuserImage(
                        this@MyAccountActivity,
                        response.body()?.result?.profile?.userImage
                    )
                    if (!PreferenceManager.getuserImage(this@MyAccountActivity).equals("")) {
                        Picasso.get()
                            .load(
                                "http://207.244.244.64/UserImages/" + PreferenceManager.getuserImage(
                                    this@MyAccountActivity
                                )
                            )
                            .into(profileImage)
                    }
                    Toast.makeText(
                        this@MyAccountActivity,
                        "Profile has been updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@MyAccountActivity,
                        response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })


    }


    fun updateImage(selectedFile: String) {
        progressDialog = context.let { CustomProgressbar().show(it) }
        val myJason = JSONObject()
        myJason.put("img", selectedFile)

        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClientForSGP.client.create(ApiInterface::class.java)

        val call = apiService.UserImage(gsonObject)
        call.enqueue(object : Callback<UserImage> {
            override fun onFailure(call: Call<UserImage>, t: Throwable) {
                progressDialog.dismiss()
            }

            override fun onResponse(call: Call<UserImage>, response: Response<UserImage>) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    PreferenceManager.setuserImage(
                        this@MyAccountActivity,
                        response.body()!!.result.img
                    )

                    if (!PreferenceManager.getuserImage(this@MyAccountActivity).equals("")) {
                        Picasso.get()
                            .load(
                                "http://207.244.244.64/UserImages/" + PreferenceManager.getuserImage(
                                    this@MyAccountActivity
                                )
                            )
                            .into(profileImage)

                    }
                }
            }
        })
    }
}
