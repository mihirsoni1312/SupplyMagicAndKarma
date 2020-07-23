package com.example.karma.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.karma.R
import com.example.karma.adapter.ProductListOnOrderDetailAdapter
import com.example.karma.response.CommonSimpleResponse
import com.example.karma.responseModel.orderDetailResponse.OrderDetailResponse
import com.example.karma.retrofit.ApiClient
import com.example.karma.retrofit.ApiInterface
import com.example.karma.utils.AppIDConfig
import com.example.karma.utils.CustomProgressbar
import com.example.karma.utils.PreferenceManager
import com.example.karma.utils.utils
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_order_detail.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderDetailActivity : AppCompatActivity(), View.OnClickListener,
    utils.listenerForAlertDialog {

    var progressDialog: Dialog? = null
    var orderId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        setUpView()
        if (intent.getStringExtra("orderId") != null) {
            orderId = intent.getStringExtra("orderId")
        }
        lodeData()
        btn_back_productList.setOnClickListener { finish() }

    }


    fun setUpView() {

        tvTitleOrderDetail.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        cvDetails.setCardBackgroundColor(Color.parseColor(PreferenceManager.getBackgroundColor(this)))
        tvDetail.setTextColor(Color.parseColor(PreferenceManager.getButtonFontColor(this)))

        btnEditOrder.setBackgroundColor(Color.parseColor(PreferenceManager.getBackgroundColor(this)))
        btnEditOrder.setTextColor(Color.parseColor(PreferenceManager.getButtonFontColor(this)))
        tvTotalamount.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))

        cvDetails.setOnClickListener(this)
        cvProsucts.setOnClickListener(this)
        btnEditOrder.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cvDetails -> {
                cvDetails.setCardBackgroundColor(
                    Color.parseColor(
                        PreferenceManager.getBackgroundColor(
                            this
                        )
                    )
                )
                tvDetail.setTextColor(Color.parseColor(PreferenceManager.getButtonFontColor(this)))

                cvProsucts.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
                tvProduct.setTextColor(resources.getColor(android.R.color.black))

                llDetail.visibility = View.VISIBLE
                rvProductList.visibility = View.GONE
            }
            R.id.cvProsucts -> {

                cvProsucts.setCardBackgroundColor(
                    Color.parseColor(
                        PreferenceManager.getBackgroundColor(
                            this
                        )
                    )
                )
                tvProduct.setTextColor(Color.parseColor(PreferenceManager.getButtonFontColor(this)))

                cvDetails.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
                tvDetail.setTextColor(resources.getColor(android.R.color.black))

                llDetail.visibility = View.GONE
                rvProductList.visibility = View.VISIBLE

            }
            R.id.btnEditOrder -> {

                utils.showMessageAlertWithListener(
                    "Your current cart item will be removed, are sure you want to edit this order?",
                    "Alert",
                    this,
                    this
                )

            }
        }
    }


    fun lodeData() {
        progressDialog = CustomProgressbar().show(this@OrderDetailActivity)
        val myJason = JSONObject()

        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("vendorId", PreferenceManager.getVendorId(this))
        myJason.put("userId", PreferenceManager.getId(this))
        myJason.put("orderId", orderId)
        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.getOrderDetailByOrderId(gsonObject)
        call.enqueue(object : Callback<OrderDetailResponse> {
            override fun onFailure(call: Call<OrderDetailResponse>, t: Throwable) {
                progressDialog?.dismiss()
            }

            override fun onResponse(
                call: Call<OrderDetailResponse>,
                response: Response<OrderDetailResponse>
            ) {
                progressDialog?.dismiss()
                if (response.isSuccessful) {

                    rvProductList.adapter = ProductListOnOrderDetailAdapter(
                        this@OrderDetailActivity,
                        response.body()?.result?.productInfo!!
                    )

                    tvTitleOrderDetail.text = response.body()?.result?.orderNo
                    tvOrderNumber.text = "Order #${response.body()?.result?.orderNo}"
                    tvOrderDateAndTime.text =
                        "${response.body()?.result?.orderTimeSlot?.deliveryDate} ${response.body()?.result?.orderTimeSlot?.from} - ${response.body()?.result?.orderTimeSlot?.to}"
                    tvSubTotal.text = "$ ${response.body()?.result?.tAmt}"
                    tvDeliveryCharges.text = "$ ${response.body()?.result?.deliveryCharge}"
                    tvGeneralDiscount.text = "$ ${response.body()?.result?.genralDiscount}"
                    tvPromoDiscount.text = "$ ${response.body()?.result?.promoCode?.discountAmount}"
                    tvTip.text = "$ ${response.body()?.result?.tip}"
                    tvTax.text = "$ ${response.body()?.result?.totalProductTax}"
                    tvTotalamount.text = "$ ${response.body()?.result?.pAmt}"
                    tvTotalAmount.text = "$ ${response.body()?.result?.pAmt}"
                    tvItemCount.text = "${response.body()?.result?.productInfo?.size} items"
                    tvAddress.text = "${response.body()?.result?.vendorInfo?.address} ${response.body()?.result?.vendorInfo?.zipCode}"
                    tvcity.text="${response.body()?.result?.address} ${response.body()?.result?.zipCode}"
                    tvTitleOrderDetail.text = response.body()?.result?.invoiceId
                    if (response.body()?.result?.isAllowEdit!!) {
                        btnEditOrder.visibility = View.VISIBLE
                    } else {
                        btnEditOrder.visibility = View.GONE
                    }
                    if (response.body()?.result?.status == "0") {
                        ivIcon.setImageResource(R.drawable.cancelled_icon)

                    }
                    if (response.body()?.result?.status == "1") {
                        ivIcon.setImageResource(R.drawable.placed_icon)

                    }
                    if (response.body()?.result?.status == "2") {
                        ivIcon.setImageResource(R.drawable.processing_icon)


                    }
                    if (response.body()?.result?.status == "3") {
                        ivIcon.setImageResource(R.drawable.out_for_delivery_icon)

                    }
                    if (response.body()?.result?.status == "4") {
                        ivIcon.setImageResource(R.drawable.icn_complete)

                    }
                } else {
//                    Toast.makeText(this@OrderDetailActivity, response.message(), Toast.LENGTH_SHORT).show()
                }

            }
        })
    }

    fun editCartApiCall() {
        progressDialog = CustomProgressbar().show(this@OrderDetailActivity)
        val myJason = JSONObject()

        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("vendorId", PreferenceManager.getVendorId(this))
        myJason.put("userId", PreferenceManager.getId(this))
        myJason.put("orderId", orderId)
        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.editCart(gsonObject)
        call.enqueue(object : Callback<CommonSimpleResponse> {
            override fun onFailure(call: Call<CommonSimpleResponse>, t: Throwable) {
                progressDialog?.dismiss()
            }

            override fun onResponse(
                call: Call<CommonSimpleResponse>,
                response: Response<CommonSimpleResponse>
            ) {
                progressDialog?.dismiss()
                if (response.isSuccessful) {
                    val mainIntent = Intent(this@OrderDetailActivity, MainActivity::class.java)
                    mainIntent.putExtra("fromOrderDetail", true)
                    startActivity(mainIntent)


                }
            }
        })
    }

    override fun AlertDialogClick() {
        editCartApiCall()
    }

}
