package com.example.karma.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.karma.R
import com.example.karma.adapter.AddressListAdapter
import com.example.karma.base.BaseActivity
import com.example.karma.interfaces.AddressClickListner
import com.example.karma.interfaces.AddressSelectClickListner
import com.example.karma.response.AddAddressResponse
import com.example.karma.response.GetAddressListResponse
import com.example.karma.responseModel.AddressListResponseModel
import com.example.karma.retrofit.ApiClient
import com.example.karma.retrofit.ApiInterface
import com.example.karma.utils.AppIDConfig
import com.example.karma.utils.CustomProgressbar
import com.example.karma.utils.PreferenceManager
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_address_list.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressListActivity : BaseActivity(), AddressClickListner, AddressSelectClickListner {
    private var addressList: ArrayList<AddressListResponseModel>? = ArrayList()
    private var addressListAdapter: AddressListAdapter? = null
    var progressDialog: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)


        title_address.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        ImageViewCompat.setImageTintList(
            btn_back,
            ColorStateList.valueOf(Color.parseColor(PreferenceManager.getFontColor(this)))
        )
        progressDialog = CustomProgressbar().show(this@AddressListActivity)


        btn_back.setOnClickListener {

            onBackPressed()

        }

        card_addNewAddress.setOnClickListener {

            val mainIntent = Intent(this, AddAddressActivity::class.java)
            startActivity(mainIntent)
        }


    }

    private fun loadData() {
        val myJason = JSONObject()
        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("userId", PreferenceManager.getId(this))
        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.getAddressByUserId(gsonObject)
        call.enqueue(object : Callback<GetAddressListResponse> {
            @SuppressLint("ShowToast")
            override fun onResponse(
                call: Call<GetAddressListResponse>,
                response: Response<GetAddressListResponse>
            ) {
                progressDialog!!.dismiss()
                if (response.body()!!.responseCode == 200) {

                    if (response.body()!!.result != null) {
                        txt_noData.visibility = View.GONE
                        rv_list_address.visibility = View.VISIBLE
                        addressList = response.body()!!.result.addressList

                        addressListAdapter = AddressListAdapter(
                            this@AddressListActivity,
                            addressList!!, this@AddressListActivity, this@AddressListActivity
                        )
                        val mLayoutManager =
                            LinearLayoutManager(
                                this@AddressListActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        rv_list_address!!.layoutManager = mLayoutManager
                        rv_list_address!!.itemAnimator = DefaultItemAnimator()
                        rv_list_address!!.adapter = addressListAdapter

                    } else {
                        txt_noData.visibility = View.VISIBLE
                        rv_list_address.visibility = View.GONE
                    }
                } else {
                    Toast.makeText(this@AddressListActivity, "Fail", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetAddressListResponse>, t: Throwable) {
                progressDialog!!.dismiss()

                Toast.makeText(this@AddressListActivity,"" + t.message,Toast.LENGTH_SHORT
                )
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun editAddress(addressListResponseModel: AddressListResponseModel, position: Int) {
        val mainIntent = Intent(context, EditAddressActivity::class.java)
        mainIntent.putExtra("appId", AppIDConfig.SupplyMagicAppId)
        mainIntent.putExtra("addressId", addressListResponseModel.addressId)
        mainIntent.putExtra("address", addressListResponseModel.address)
        mainIntent.putExtra("zipCode", addressListResponseModel.zipCode)
        mainIntent.putExtra("isPrimary", addressListResponseModel.isPrimary)
        mainIntent.putExtra("isActive", addressListResponseModel.isActive)
        mainIntent.putExtra("isHomeAddress", addressListResponseModel.isHomeAddress)
        context.startActivity(mainIntent)
    }

    override fun click(addressListResponseModel: AddressListResponseModel) {

        updateAddress(
            AppIDConfig.SupplyMagicAppId,
            addressListResponseModel.addressId,
            addressListResponseModel.address,
            addressListResponseModel.zipCode,
            true,
            addressListResponseModel.isActive,
            addressListResponseModel.isHomeAddress
        )

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
        progressDialog = CustomProgressbar().show(this@AddressListActivity)

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
                        this@AddressListActivity,
                        msg,
                        Toast.LENGTH_SHORT
                    ).show()

                    val mainIntent =
                        Intent(this@AddressListActivity, AddressListActivity::class.java)
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(mainIntent)

                } else {
                    var msg: String = response.body()!!.message
                    Log.e("Response_Fail", "" + response.body()!!.message)
                    Toast.makeText(
                        this@AddressListActivity,
                        msg,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<AddAddressResponse>, t: Throwable) {

                progressDialog!!.dismiss()
                Toast.makeText(
                    this@AddressListActivity,
                    "" + t.message,
                    Toast.LENGTH_SHORT
                )
            }
        })


    }

}
