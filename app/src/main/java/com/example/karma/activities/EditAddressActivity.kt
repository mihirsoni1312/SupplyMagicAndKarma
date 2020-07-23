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
import com.example.karma.utils.CustomProgressbar
import com.example.karma.utils.PreferenceManager
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_address.btn_back
import kotlinx.android.synthetic.main.activity_add_address.title_address
import kotlinx.android.synthetic.main.activity_add_address.txt_add_address
import kotlinx.android.synthetic.main.activity_edit_address.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException

class EditAddressActivity : BaseActivity() {

    private var appId: String = ""
    private var addressId: String = ""
    private var address: String = ""
    private var zipCode: String = ""
    private var isPrimary: Boolean = false
    private var isActive: Boolean = true
    private var isHomeAddress: Boolean = true
    var progressDialog: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_address)
        btn_edit_address.setBackgroundColor(
            Color.parseColor(
                PreferenceManager.getBackgroundColor(
                    this
                )
            )
        )

        appId = intent.getStringExtra("appId")
        addressId = intent.getStringExtra("addressId")
        address = intent.getStringExtra("address")
        zipCode = intent.getStringExtra("zipCode")
        isPrimary = intent.getBooleanExtra("isPrimary", false)
        isActive = intent.getBooleanExtra("isActive", true)
        isHomeAddress = intent.getBooleanExtra("isHomeAddress", true)

        edt_address_edit.setText(address)
        edt_zip_edit.setText(zipCode)

        if (PreferenceManager.getAppId(this) == "2") {
            Picasso.get().load(PreferenceManager.getLoginScreeLogo(this@EditAddressActivity))
                .into(img_addaddress_edit)

        } else if (PreferenceManager.getAppId(this) == "1") {
            Picasso.get().load(PreferenceManager.getLoginScreeLogo(this@EditAddressActivity))
                .into(img_addaddress_edit)
        }

        toggleButtonLayoutText_edit.selectedColor =
            Color.parseColor(PreferenceManager.getBackgroundColor(this))


        if (isHomeAddress) {
            toggleButtonLayoutText_edit.setToggled(toggleButtonLayoutText_edit.toggles[0].id, true)
        } else {
            toggleButtonLayoutText_edit.setToggled(toggleButtonLayoutText_edit.toggles[1].id, true)
        }



        btn_edit_address.setTextColor(Color.parseColor(PreferenceManager.getButtonFontColor(this)))
        edt_address_edit.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_zip_edit.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_address_edit.setHintTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_zip_edit.setHintTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))


        title_address.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        txt_add_address.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))

        changeIconColorTint(edt_address_edit, PreferenceManager.getBackgroundColor(this))
        changeIconColorTint(edt_zip_edit, PreferenceManager.getBackgroundColor(this))
        btn_edit_address.setOnClickListener {

            if (edt_address_edit.text.toString().isEmpty()) {

                Toast.makeText(
                    this@EditAddressActivity,
                    "Please enter Address",
                    Toast.LENGTH_SHORT
                ).show()

            } else if (edt_zip_edit.text.toString().isEmpty()) {

                Toast.makeText(
                    this@EditAddressActivity,
                    "Please enter ZipCode",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                updateAddress(
                    appId,
                    addressId,
                    edt_address_edit.text.toString(),
                    edt_zip_edit.text.toString(),
                    isPrimary,
                    isActive,
                    isHomeAddress
                )
            }

        }

        ImageViewCompat.setImageTintList(
            btn_back,
            ColorStateList.valueOf(Color.parseColor(PreferenceManager.getFontColor(this)))
        )

        btn_back.setOnClickListener {
            onBackPressed()
        }

        toggleButtonLayoutText_edit.onToggledListener = { toggleButtonLayout, toggle, selected ->

            isHomeAddress = toggle.id == 2131296629

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


    fun updateAddress(
        appId: String,
        addressId: String,
        address: String,
        zipCode: String,
        isPrimary: Boolean,
        isActive: Boolean,
        isHomeAddress: Boolean
    ) {
        progressDialog = CustomProgressbar().show(this@EditAddressActivity)

        val myJason = JSONObject()
        myJason.put("appId", appId)
        myJason.put("addressId", addressId)
        myJason.put("address", address)
        myJason.put("zipCode", zipCode)
        myJason.put("isPrimary", isPrimary)
        myJason.put("isActive", isActive)
        myJason.put("isHomeAddress", isHomeAddress)

        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.updateAddressByAddressId(gsonObject)
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
                        this@EditAddressActivity,
                        msg,
                        Toast.LENGTH_SHORT
                    ).show()

                    val mainIntent =
                        Intent(this@EditAddressActivity, AddressListActivity::class.java)
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(mainIntent)

                } else {
                    var msg: String = response.body()!!.message
                    Log.e("Response_Fail", "" + response.body()!!.message)
                    Toast.makeText(
                        this@EditAddressActivity,
                        msg,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<AddAddressResponse>, t: Throwable) {

                progressDialog!!.dismiss()

            }
        })


    }

    override fun onBackPressed() {
        super.onBackPressed()
        val mainIntent = Intent(this@EditAddressActivity, AddressListActivity::class.java)
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
                img_addaddress_edit.setImageURI(Uri.parse(xyz.path + "/registerbgKarma.jpg"))
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
                img_addaddress_edit.setImageURI(Uri.parse(xyz.path + "/registerbgSuplyMagic.jpg"))
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
