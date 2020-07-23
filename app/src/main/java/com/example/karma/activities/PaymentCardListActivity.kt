package com.example.karma.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.karma.R
import com.example.karma.adapter.PaymentListAdapter
import com.example.karma.interfaces.CardDeleteClickListner
import com.example.karma.response.CommonSimpleResponse
import com.example.karma.response.PCK
import com.example.karma.response.PaymentResponse
import com.example.karma.response.VendorListResponse
import com.example.karma.responseModel.PaymentCardModel
import com.example.karma.responseModel.updateDefaultPaymentType.UpdateDefaultPaymentType
import com.example.karma.retrofit.ApiClient
import com.example.karma.retrofit.ApiClientForSGP
import com.example.karma.retrofit.ApiInterface
import com.example.karma.utils.*
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_address_list.txt_noData
import kotlinx.android.synthetic.main.activity_payment_card_list.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PaymentCardListActivity : AppCompatActivity(), CardDeleteClickListner,
    PaymentListAdapter.SelectPaymentMethod {

    private var paymentCardList: ArrayList<PaymentCardModel>? = ArrayList()
    private var PaymentAdapter: PaymentListAdapter? = null
    var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_card_list)
        title_address.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        card_openWebview.setOnClickListener {

            startActivity(Intent(this, AddPaymentActivity::class.java))

        }

        btn_back.setOnClickListener {
            onBackPressed()
        }


    }


    private fun loadData() {
        val successObject = JSONArray()
        val PCKDefault: PCK = utils.fromJson(
            PreferenceManager.getDefaultTypeBank(this),
            object : TypeToken<PCK>() {}.type
        ) as PCK
        var jsonObject = JSONObject()
        jsonObject.put("cId", PCKDefault.cld)
        jsonObject.put("bId", PCKDefault.bld)

        successObject.put(jsonObject)
        progressDialog = CustomProgressbar().show(this@PaymentCardListActivity)
        val myJason = JSONObject()

        var valueList =
            utils.fromJson(PreferenceManager.getPCK(this), object : TypeToken<List<PCK>>() {}.type)
        var value: List<PCK> = valueList as List<PCK>


        var jArray = JSONArray()

        for (i in value) {
            var j: JSONObject = JSONObject()
            j.put("bId", i.bld)
            j.put("cId", i.cld)

            jArray.put(j)

        }

        myJason.put("VRS", PreferenceManager.getVendorId(this))
        myJason.put("SCK", PreferenceManager.getSck(this))
        myJason.put("TYP", PreferenceManager.getPaymentTypeName(this))
        myJason.put("SCPDefault", PreferenceManager.getdefaultPaymentType(this))
        myJason.put("PCKDefault", successObject)
        myJason.put("SCK", PreferenceManager.getSck(this))
        myJason.put("PCK", jArray)
        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClientForSGP.client.create(ApiInterface::class.java)
        val call = apiService.cardList(gsonObject)
        call.enqueue(object : Callback<PaymentResponse> {
            @SuppressLint("ShowToast")
            override fun onResponse(
                call: Call<PaymentResponse>,
                response: Response<PaymentResponse>
            ) {
                progressDialog?.dismiss()
                if (response.body()!!.responseCode == 200) {

                    if (response.body()!!.result != null) {


                        txt_noData.visibility = View.GONE
                        rv_list_payment.visibility = View.VISIBLE
                        paymentCardList = response.body()!!.result as ArrayList<PaymentCardModel>

                        if (paymentCardList!!.isEmpty()) {
                            txt_noData.visibility = View.VISIBLE
                            rv_list_payment.visibility = View.GONE
                        } else {
                            txt_noData.visibility = View.GONE
                            rv_list_payment.visibility = View.VISIBLE
                        }

                        PaymentAdapter = PaymentListAdapter(
                            this@PaymentCardListActivity,
                            paymentCardList!!,
                            this@PaymentCardListActivity
                            , this@PaymentCardListActivity
                        )
                        val mLayoutManager =
                            LinearLayoutManager(
                                this@PaymentCardListActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        rv_list_payment!!.layoutManager = mLayoutManager
                        rv_list_payment!!.itemAnimator = DefaultItemAnimator()
                        rv_list_payment!!.adapter = PaymentAdapter

                    } else {
                        txt_noData.visibility = View.VISIBLE
                        rv_list_payment.visibility = View.GONE
                    }
                } else if (response.body()!!.responseCode == 201) {

                    txt_noData.visibility = View.VISIBLE
                    rv_list_payment.visibility = View.GONE

                }
            }

            override fun onFailure(call: Call<PaymentResponse>, t: Throwable) {
                progressDialog!!.dismiss()

                Toast.makeText(
                    this@PaymentCardListActivity,
                    "" + t.message,
                    Toast.LENGTH_SHORT
                )
            }
        })
    }

    override fun cartDelete(cardId: String, paymenttype: String, bankdetails: PCK) {
        cardDeleteFun(cardId, paymenttype, bankdetails)
    }

    fun cardDeleteFun(cardId: String, paymenttype: String, bankdetails: PCK) {

        progressDialog = CustomProgressbar().show(this@PaymentCardListActivity)
        val myJason = JSONObject()
        myJason.put("VRS", PreferenceManager.getVendorId(this))
        myJason.put("SCP", cardId)
        myJason.put("TYP", paymenttype)
        myJason.put("PCK", bankdetails)
        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClientForSGP.client.create(ApiInterface::class.java)
        val call = apiService.cardDelete(gsonObject)
        call.enqueue(object : Callback<CommonSimpleResponse> {
            @SuppressLint("ShowToast")
            override fun onResponse(
                call: Call<CommonSimpleResponse>,
                response: Response<CommonSimpleResponse>
            ) {
                progressDialog?.dismiss()
                if (response.isSuccessful) {

                    toast(response.body()!!.message)
                    vendorList()
                } else {
                    toast(response.body()!!.message)
                }
            }

            override fun onFailure(call: Call<CommonSimpleResponse>, t: Throwable) {
                progressDialog?.dismiss()

                Toast.makeText(
                    this@PaymentCardListActivity,
                    "" + t.message,
                    Toast.LENGTH_SHORT
                )
            }
        })


    }

    override fun onResume() {
        super.onResume()
        vendorList()

    }

    override fun selectedPaymentmethod(paymentCardModel: PaymentCardModel) {
        progressDialog = CustomProgressbar().show(this@PaymentCardListActivity)

        var j: JSONObject = JSONObject()
        j.put("bId", paymentCardModel.BankDetails.bld)
        j.put("cId", paymentCardModel.BankDetails.cld)

        var paymentType = ""
        if (paymentCardModel.Type.equals("CARD")) {
            paymentType = paymentCardModel.id
        } else if (paymentCardModel.Type.equals("COD")) {
            paymentType = "COD"
        } else {
            paymentType = "BANK"
        }

        val myJason = JSONObject()
        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("userId", PreferenceManager.getId(this))
        myJason.put("vendorId", PreferenceManager.getVendorId(this))
        myJason.put("paymentType", paymentType)
        myJason.put("paymentTypeName", paymentCardModel.Type)
        myJason.put("defaultTypeBank", j)


        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.updateDefaultPaymentType(gsonObject)

        call.enqueue(object : Callback<UpdateDefaultPaymentType> {
            override fun onFailure(call: Call<UpdateDefaultPaymentType>, t: Throwable) {
                progressDialog?.dismiss()
            }

            override fun onResponse(
                call: Call<UpdateDefaultPaymentType>,
                response: Response<UpdateDefaultPaymentType>
            ) {
                progressDialog?.dismiss()

                if (response.isSuccessful) {
                    if (response.body()?.responseCode == 200) {
                        vendorList()
                        Toast.makeText(
                            this@PaymentCardListActivity,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })

    }


    fun vendorList() {

        val myJason = JSONObject()
        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("userId", PreferenceManager.getId(this))
        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject = jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.getAssociatedVendorList(gsonObject)
        call.enqueue(object : Callback<VendorListResponse> {
            @SuppressLint("ShowToast")
            override fun onResponse(
                call: Call<VendorListResponse>,
                response: Response<VendorListResponse>
            ) {

                if (response.isSuccessful)
                    if (response.body()?.responseCode == 200) {

                        var a: Int = 0
                        for (i in 1..response.body()?.result?.vendorList?.size!!) {
                            if (PreferenceManager.getVendorId(this@PaymentCardListActivity)
                                    .equals(response.body()?.result?.vendorList?.get(i - 1)?.vendorId)
                            ) {
                                a = i - 1
                            }
                        }

                        PreferenceManager.setVendorId(
                            this@PaymentCardListActivity,
                            response.body()!!.result.vendorList[a].vendorId
                        )

                        PreferenceManager.setvendorName(
                            this@PaymentCardListActivity,
                            response.body()!!.result.vendorList[a].appName
                        )
                        PreferenceManager.setvendorImage(
                            this@PaymentCardListActivity,
                            response.body()!!.result.vendorList[a].logo
                        )

                        PreferenceManager.setSck(
                            this@PaymentCardListActivity,
                            response.body()!!.result.vendorList[a].sck
                        )

                        PreferenceManager.setVendorPhoneNumber(
                            this@PaymentCardListActivity,
                            response.body()!!.result.vendorList[a].phone
                        )
                        PreferenceManager.setPlsk(
                            this@PaymentCardListActivity,
                            response.body()!!.result.vendorList[a].plpk
                        )
                        PreferenceManager.setDefaultPaymentType(
                            this@PaymentCardListActivity,
                            response.body()!!.result.vendorList[a].defaultPaymentType
                        )
                        PreferenceManager.setDefaultTypeBank(
                            this@PaymentCardListActivity,
                            response.body()!!.result.vendorList[a].defaultTypeBank
                        )
                        PreferenceManager.setPaymentTypeName(
                            this@PaymentCardListActivity,
                            response.body()!!.result.vendorList[a].paymentTypeName
                        )
                        PreferenceManager.setPCK(
                            this@PaymentCardListActivity,
                            response.body()!!.result.vendorList[a].pck
                        )
                        loadData()
                    } else {
                        Toast.makeText(
                            this@PaymentCardListActivity,
                            response.body()!!.toString(),
                            Toast.LENGTH_SHORT
                        ).show()

                    }
            }

            override fun onFailure(call: Call<VendorListResponse>, t: Throwable) {


                Toast.makeText(
                    this@PaymentCardListActivity,
                    "" + t.message,
                    Toast.LENGTH_SHORT
                )
            }
        })
    }

    override fun onBackPressed() {
        finish()
    }
}

