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
import com.example.karma.response.AddAddressResponse
import com.example.karma.retrofit.ApiClient
import com.example.karma.retrofit.ApiInterface
import com.example.karma.utils.AppIDConfig
import com.example.karma.utils.CustomProgressbar
import com.example.karma.utils.PreferenceManager
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException

class AddAddressActivity : BaseActivity() {
    var progressDialog: Dialog? = null
    var toggleId: String = "HOME"
    var isHomeAddress: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)


        title_address.setTextColor(Color.parseColor(PreferenceManager.getFontColor(context)))
        if (PreferenceManager.getAppId(this) == "2") {
            Picasso.get().load(PreferenceManager.getLoginScreeLogo(this@AddAddressActivity))
                .into(img_addaddress)

        } else if (PreferenceManager.getAppId(this) == "1") {
            Picasso.get().load(PreferenceManager.getLoginScreeLogo(this@AddAddressActivity))
                .into(img_addaddress)

        }

        toggleButtonLayoutText.selectedColor =
                Color.parseColor(PreferenceManager.getBackgroundColor(this))



        btn_add_address.setBackgroundColor(
                Color.parseColor(
                        PreferenceManager.getBackgroundColor(
                                this
                        )
                )
        )
        btn_add_address.setTextColor(Color.parseColor(PreferenceManager.getButtonFontColor(this)))
        edt_address.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_zip.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_address.setHintTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_zip.setHintTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))



        btn_add_address.setOnClickListener {

            if (edt_address.text.toString().isEmpty()) {
                Toast.makeText(
                        this@AddAddressActivity,
                        "Please enter Address",
                        Toast.LENGTH_SHORT
                ).show()
            } else if (edt_zip.text.toString().isEmpty()) {
                Toast.makeText(
                        this@AddAddressActivity,
                        "Please enter ZipCode",
                        Toast.LENGTH_SHORT
                ).show()
            } else {

                isHomeAddress = toggleId.equals("HOME")
                addAddress(
                        AppIDConfig.SupplyMagicAppId, PreferenceManager.getId(this),
                        edt_address.text.toString(), edt_zip.text.toString(), false, isHomeAddress
                )
            }

        }
        title_address.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        txt_add_address.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))

        changeIconColorTint(edt_address, PreferenceManager.getBackgroundColor(this))
        changeIconColorTint(edt_zip, PreferenceManager.getBackgroundColor(this))


        ImageViewCompat.setImageTintList(
                btn_back,
                ColorStateList.valueOf(Color.parseColor(PreferenceManager.getFontColor(this)))
        )

        btn_back.setOnClickListener {
            onBackPressed()
        }
        toggleButtonLayoutText.setToggled(toggleButtonLayoutText.toggles[0].id, true)

        toggleButtonLayoutText.onToggledListener = { toggleButtonLayout, toggle, selected ->


            if (toggle.title!! == "HOME") {
                isHomeAddress = true
                toggleButtonLayoutText.setToggled(toggle.id, true)
            } else {
                isHomeAddress = false
                toggleButtonLayoutText.setToggled(toggle.id, true)
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


    fun addAddress(
            appId: String,
            userId: String,
            address: String,
            zipcode: String,
            isPrimary: Boolean,
            isHomeAddress: Boolean
    ) {
        progressDialog = CustomProgressbar().show(this@AddAddressActivity)


        val myJason = JSONObject()
        myJason.put("appId", appId)
        myJason.put("userId", userId)
        myJason.put("address", address)
        myJason.put("zipCode", zipcode)
        myJason.put("isPrimary", isPrimary)
        myJason.put("isHomeAddress", isHomeAddress)

        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
                jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.addAddressByUserId(gsonObject)
        call.enqueue(object : Callback<AddAddressResponse> {
            @SuppressLint("ShowToast")
            override fun onResponse(
                    call: Call<AddAddressResponse>,
                    response: Response<AddAddressResponse>
            ) {

                progressDialog!!.dismiss()
                if (response.body()!!.responseCode == 200) {
                    var msg: String = response.body()!!.message
                    Toast.makeText(
                            this@AddAddressActivity,
                            msg,
                            Toast.LENGTH_SHORT
                    ).show()

                    val mainIntent =
                            Intent(this@AddAddressActivity, AddressListActivity::class.java)
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(mainIntent)

                } else {
                    var msg: String = response.body()!!.message
                    Log.e("Response_Fail", "" + response.body()!!.message)
                    Toast.makeText(
                            this@AddAddressActivity,
                            msg,
                            Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<AddAddressResponse>, t: Throwable) {

                progressDialog!!.dismiss()
                Toast.makeText(
                        this@AddAddressActivity,
                        "" + t.message,
                        Toast.LENGTH_SHORT
                )
            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val mainIntent = Intent(this@AddAddressActivity, AddressListActivity::class.java)
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
                img_addaddress.setImageURI(Uri.parse(xyz.path + "/registerbgKarma.jpg"))
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
                img_addaddress.setImageURI(Uri.parse(xyz.path + "/registerbgSuplyMagic.jpg"))
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

