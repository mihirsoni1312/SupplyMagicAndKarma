package com.example.karma.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.karma.R
import com.example.karma.adapter.SpinnerAdapter
import com.example.karma.response.PCK
import com.example.karma.response.Promocoderesponse
import com.example.karma.response.deliverySlotResponse.DeliverySlotResponse
import com.example.karma.response.deliverySlotResponse.Result
import com.example.karma.response.insertOrder.InsertOrder
import com.example.karma.retrofit.ApiClient
import com.example.karma.retrofit.ApiClientForSGP
import com.example.karma.retrofit.ApiInterface
import com.example.karma.utils.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_review_checkout.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReviewCheckoutActivity : AppCompatActivity(), View.OnClickListener {

    var pickuparray = ArrayList<String>()
    lateinit var deliverySlotList: ArrayList<Result>
    var pickuparray_location = ArrayList<String>()
    var pickuparray_time_slot = ArrayList<String>()
    var progressDialog: Dialog? = null
    var timeselectedP: Int = 0
    var timeselectedD: Int = 0
    var timelist: ArrayList<Result> = arrayListOf()
    var orderComments: String = ""
    var orderType: Int = 0
    var cartId: String = ""
    var orderDiscount: String = ""
    var promoCode: String = ""
    var delivery: String = ""
    var counterFor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_review_checkout)
        btn_back_cart.setOnClickListener {
            finish()
        }
        title_productList.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        var total = intent?.let { it.getStringExtra("total") }
        cartId = intent?.let { it.getStringExtra("cartId") }.toString()
        orderComments = intent?.let { it.getStringExtra("orderdis") }.toString()

        rowTotal.text = "$ $total"

        finalTotal()

        tvNotToday.setOnClickListener(this)
        tvFifteen.setOnClickListener(this)
        tvTwenty.setOnClickListener(this)
        tvTwentyFive.setOnClickListener(this)
        Custom.setOnClickListener(this)

        btn_login.setBackgroundColor(Color.parseColor(PreferenceManager.getBackgroundColor(this)))
        btn_login.setTextColor(Color.parseColor(PreferenceManager.getButtonFontColor(this)))
        applyCoupan.setBackgroundColor(Color.parseColor(PreferenceManager.getBackgroundColor(this)))
        applyCoupan.setTextColor(Color.parseColor(PreferenceManager.getButtonFontColor(this)))


        applyCoupan.setOnClickListener {

            if (applyCoupan.text == "CANCEL") {

                applyCoupan.text = "Apply"
                promoCode = ""
                orderDiscount = ""
                tvPromocode.text = "$0.00"
                copunCode.setText("")
                finalTotal()

            } else {
                if (copunCode.text.toString().isEmpty()) {
                    Toast.makeText(this, "Please enter promo code", Toast.LENGTH_SHORT).show()
                } else {
                    PromocodeApi()
                }
            }
        }

        pickuparray.clear()

        pickuparray.add("Select")
        pickuparray.add("Delivery")
        pickuparray.add("Pick Up")



        if (PreferenceManager.getIsRestaurentApp(this@ReviewCheckoutActivity)) {
            pickuparray.add("Dine In")
        }

        if (spinner.equals("Select")) {

        }

        val adapter = ArrayAdapter(this, R.layout.spinner_layout_row, pickuparray)
        ArrayAdapter(this, R.layout.spinner_layout_row, pickuparray_location)
        spinner.adapter = adapter
        spinner.setSelected(false);  // must
        spinner.setSelection(-1, true);
        dropdown_pickup.setOnClickListener {

            spinner.performClick()
        }

//        setSpinner.setOnClickListener {
//            spinner.performClick()
//        }

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {


                if (++counterFor > 1) {

//                    setSpinner.text = pickuparray[position]

                    if (pickuparray[position] == "Select") {
                        pickUpLayout.visibility = View.GONE
                        DeliveryLayout.visibility = View.GONE
                    } else if (pickuparray[position] == "Pick Up") {
                        DeliveryLayout.visibility = View.GONE
                        pickUpLayout.visibility = View.VISIBLE
                        img_timeslot.setImageResource(R.drawable.deliver_icon)
                        tv_timesloat.text="Choose Pick Up Time Slot"
                        txt_location.text="Choose Pick Up Location"
                    } else if (pickuparray[position] == "Dine In") {
                        DeliveryLayout.visibility = View.GONE
                        pickUpLayout.visibility = View.VISIBLE
                        img_timeslot.setImageResource(R.drawable.imgpsh_danig)
                        tv_timesloat.text="Choose Dine In Time Slot"
                        txt_location.text="Choose Dine In Location"
                    } else {
                        DeliveryLayout.visibility = View.VISIBLE
                        pickUpLayout.visibility = View.GONE
                    }

                    if (pickuparray[position] == "Pick Up") {
                        orderType = 2
                    } else if (pickuparray[position] == "Delivery") {
                        orderType = 1
                    } else {
                        orderType = 3
                    }
                    if (!::deliverySlotList.isInitialized) {
                        DeliverySlotApiCall()
                    }
//                    if (setSpinner.text.equals("Delivery") || setSpinner.text.equals("Pick Up")) {
//
//                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


        spinner_pickup_time_slot.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {

                timeselectedP = position
                tvDeliveryCharge.text = "\$ ${deliverySlotList[timeselectedP].deliveryFee}"
                finalTotal()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        spinner_time_slot.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                timeselectedD = position
                tvDeliveryCharge.text = "\$ ${timelist[timeselectedP].deliveryFee}"
                finalTotal()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        customTip.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrBlank()) {

                    tvTipVal.text = "$%.2f".format(s.toString().toFloat())
                    var a: Float = rowTotal.text.toString().replace("$", "").toFloat()
                    a = a * 50 / 100
                    if (tvTipVal.text.toString().replace("$", "").toFloat() > a) {
                        utils.showMessageAlert(
                            "you can pay maximum 50% tip of total cart amount",
                            "Message",
                            this@ReviewCheckoutActivity
                        )
                    }
                    finalTotal()
                } else {
                    tvTipVal.text = "$%.2f".format(0.00)
                    finalTotal()
                }
            }
        })

        changeAddress.setOnClickListener {

            val mainIntent = Intent(this, AddressListActivity::class.java)
            startActivity(mainIntent)
        }

        btn_login.setOnClickListener {

            if (spinner.selectedItem.equals("Select")) {
                utils.showMessageAlert(
                    "Please Select order type",
                    "Message",
                    this@ReviewCheckoutActivity
                )
            } else if (PreferenceManager.getzipCode(this).equals("")) {
                utils.showMessageAlert(
                    "Currently we are not provide service on your location",
                    "Message",
                    this@ReviewCheckoutActivity
                )
            } else if (timelist.size < 1) {

                utils.showMessageAlert(
                    "Currently we are not provide service on your location",
                    "Message",
                    this@ReviewCheckoutActivity
                )
            } else {
                insertOrder()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvNotToday -> {

                tvNotToday.background =
                    resources.getDrawable(R.drawable.ronded_corner_background, null)
                tvNotToday.setTextColor(resources.getColor(R.color.colorPrimary))

                tvFifteen.setBackgroundResource(0)
                tvFifteen.setTextColor(resources.getColor(R.color.black))

                tvTwenty.setBackgroundResource(0)
                tvTwenty.setTextColor(resources.getColor(R.color.black))

                tvTwentyFive.setBackgroundResource(0)
                tvTwentyFive.setTextColor(resources.getColor(R.color.black))
                Custom.setBackgroundResource(0)
                Custom.setTextColor(resources.getColor(R.color.black))

                count(0)

                customTip.visibility = View.GONE
            }
            R.id.tvFifteen -> {
                tvFifteen.background =
                    resources.getDrawable(R.drawable.ronded_corner_background, null)
                tvFifteen.setTextColor(resources.getColor(R.color.colorPrimary))

                tvNotToday.setBackgroundResource(0)
                tvNotToday.setTextColor(resources.getColor(R.color.black))

                tvTwenty.setBackgroundResource(0)
                tvTwenty.setTextColor(resources.getColor(R.color.black))

                tvTwentyFive.setBackgroundResource(0)
                tvTwentyFive.setTextColor(resources.getColor(R.color.black))

                Custom.setBackgroundResource(0)
                Custom.setTextColor(resources.getColor(R.color.black))

                count(15)
                customTip.visibility = View.GONE

            }
            R.id.tvTwenty -> {

                tvTwenty.background =
                    resources.getDrawable(R.drawable.ronded_corner_background, null)
                tvTwenty.setTextColor(resources.getColor(R.color.colorPrimary))

                tvNotToday.setBackgroundResource(0)
                tvNotToday.setTextColor(resources.getColor(R.color.black))

                tvFifteen.setBackgroundResource(0)
                tvFifteen.setTextColor(resources.getColor(R.color.black))

                tvTwentyFive.setBackgroundResource(0)
                tvTwentyFive.setTextColor(resources.getColor(R.color.black))
                Custom.setBackgroundResource(0)
                Custom.setTextColor(resources.getColor(R.color.black))

                count(20)
                customTip.visibility = View.GONE
            }
            R.id.tvTwentyFive -> {

                tvTwentyFive.background =
                    resources.getDrawable(R.drawable.ronded_corner_background, null)
                tvTwentyFive.setTextColor(resources.getColor(R.color.colorPrimary))

                tvNotToday.setBackgroundResource(0)
                tvNotToday.setTextColor(resources.getColor(R.color.black))

                tvTwenty.setBackgroundResource(0)
                tvTwenty.setTextColor(resources.getColor(R.color.black))

                tvFifteen.setBackgroundResource(0)
                tvFifteen.setTextColor(resources.getColor(R.color.black))

                Custom.setBackgroundResource(0)
                Custom.setTextColor(resources.getColor(R.color.black))
                count(25)
                customTip.visibility = View.GONE
            }
            R.id.Custom -> {

                Custom.background = resources.getDrawable(R.drawable.ronded_corner_background, null)
                Custom.setTextColor(resources.getColor(R.color.colorPrimary))

                tvNotToday.setBackgroundResource(0)
                tvNotToday.setTextColor(resources.getColor(R.color.black))

                tvTwenty.setBackgroundResource(0)
                tvTwenty.setTextColor(resources.getColor(R.color.black))

                tvTwentyFive.setBackgroundResource(0)
                tvTwentyFive.setTextColor(resources.getColor(R.color.black))

                tvFifteen.setBackgroundResource(0)
                tvFifteen.setTextColor(resources.getColor(R.color.black))

//                tvTipVal.text = "$%.2f".format(0.00)
                count(0)
                customTip.visibility = View.VISIBLE

            }
//            R.id.PleaseSelect ->{
//                PleaseSelect.performClick()
//            }
        }
    }


    fun count(p: Int) {

        if (p != 0) {
            var a: Float = rowTotal.text.toString().replace("$", "").toFloat()
            a = a * p / 100
            tvTipVal.text = "$%.2f".format(a)
        } else {
            tvTipVal.text = "$0.00"
        }
        finalTotal()
    }


    fun finalTotal() {
        var total = rowTotal.text.toString().replace("$", "").toFloat()
        var tax = tvTax.text.toString().replace("$", "").toFloat()
        var tip = tvTipVal.text.toString().replace("$", "").toFloat()
        var deliveryCharge = tvDeliveryCharge.text.toString().replace("$", "").toFloat()
        var finalTotal = total + tax + tip + deliveryCharge
        tvFinalTotal.text = "$ %.2f".format(finalTotal)
    }


    fun DeliverySlotApiCall() {

        progressDialog = CustomProgressbar().show(this@ReviewCheckoutActivity)
        val myJason = JSONObject()
        myJason.put("VRS", PreferenceManager.getVendorId(this))
        myJason.put("ZIP", PreferenceManager.getzipCode(this))
        myJason.put("API", "1")
        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClientForSGP.client.create(ApiInterface::class.java)
        val call = apiService.getDeliverySlot(gsonObject)
        call.enqueue(object : Callback<DeliverySlotResponse> {
            override fun onFailure(call: Call<DeliverySlotResponse>, t: Throwable) {
                progressDialog?.dismiss()
            }

            override fun onResponse(
                call: Call<DeliverySlotResponse>,
                response: Response<DeliverySlotResponse>
            ) {
                progressDialog?.dismiss()
                if (response.body()!!.responseCode == 200) {
                    if (response.body()!!.result != null) {

                        deliverySlotList = response.body()?.result!! as ArrayList<Result>
                        deliverySlotList =
                            deliverySlotList.filterNot { it.type.equals("2") } as ArrayList<Result>
                        spinner_pickup_time_slot.adapter = SpinnerAdapter(
                            this@ReviewCheckoutActivity,
                            deliverySlotList
                        )


                        timelist = response.body()?.result!! as ArrayList<Result>
                        timelist = timelist.filterNot { it.type.equals("1") } as ArrayList<Result>
                        spinner_time_slot.adapter = SpinnerAdapter(
                            this@ReviewCheckoutActivity,
                            timelist
                        )

                    }
                }
            }
        })

    }

    override fun onResume() {
        super.onResume()

        DeliverySlotApiCall()
        pick_up_address.text =
            PreferenceManager.getvandorAdd(this@ReviewCheckoutActivity).toString()
        spinner_pick_up.text = PreferenceManager.getaddress(this@ReviewCheckoutActivity).toString()


    }


    fun insertOrder() {
        progressDialog = CustomProgressbar().show(this@ReviewCheckoutActivity)

        val myJArray = JsonArray()
        myJArray.add(PreferenceManager.getPaymentTypeName(this))
        val paymentTypeId = myJArray.toString()
        val myJason = JSONObject()


        /*val successObject = JSONArray()
        val PCKDefault: PCK = utils.fromJson(
            PreferenceManager.getDefaultTypeBank(this),
            object : TypeToken<PCK>() {}.type
        ) as PCK
        var jsonObject = JSONObject()
        jsonObject.put("cId", PCKDefault.cld)
        jsonObject.put("bId", PCKDefault.bld)
        successObject.put(jsonObject)*/


//        if(PreferenceManager.)

        var selected: Int = 0

        var deliveryDate: String
        var deliveryFrom: String
        var deliveryTo: String
        var cartAllowTime: String
        var deliveryCharge: String
        if (orderType == 1) {
            deliveryDate = deliverySlotList[timeselectedD].orderDate.toString()
            deliveryFrom = deliverySlotList[timeselectedD].startTime.toString()
            deliveryTo = deliverySlotList[timeselectedD].endTime.toString()
            cartAllowTime = deliverySlotList[timeselectedD].checkOutReplaceEndTime.toString()
            deliveryCharge = deliverySlotList[timeselectedD].deliveryFee.toString()
        } else {
            deliveryDate = timelist[timeselectedP].orderDate.toString()
            deliveryFrom = timelist[timeselectedP].startTime.toString()
            deliveryTo = timelist[timeselectedP].endTime.toString()
            cartAllowTime = timelist[timeselectedP].checkOutReplaceEndTime.toString()
            deliveryCharge = timelist[timeselectedP].deliveryFee.toString()

        }


        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("vendorId", PreferenceManager.getVendorId(this))
        myJason.put("userId", PreferenceManager.getId(this))
        myJason.put("cartId", cartId)
        myJason.put("isRestaurentApp", PreferenceManager.getIsRestaurentApp(this))
        myJason.put("isDineInOrder", "false")
        myJason.put("deliveryDate", "${deliveryDate}")
        myJason.put("deliveryFrom", "${deliveryFrom}")
        myJason.put("deliveryTo", "${deliveryTo}")
        myJason.put("mobileNumber", PreferenceManager.getmobileNumber(this))
        myJason.put("address", "${PreferenceManager.getaddress(this)}")
        myJason.put("zipCode", "${PreferenceManager.getzipCode(this)}")
        val myJArraypayment = JsonArray()
        var payType: String = "";
        if (PreferenceManager.getPaymentTypeName(this) == "BANK") {
            payType = "2";
            val successObject = JSONArray()
            val PCKDefault: PCK = utils.fromJson(
                PreferenceManager.getDefaultTypeBank(this),
                object : TypeToken<PCK>() {}.type
            ) as PCK
            var jsonObject = JSONObject()
            jsonObject.put("cId", PCKDefault.cld)
            jsonObject.put("bId", PCKDefault.bld)
            successObject.put(jsonObject)

            myJArraypayment.add(PreferenceManager.getDefaultTypeBank(this))
            myJason.put("paymentTypeId", successObject)
        } else if (PreferenceManager.getPaymentTypeName(this) == "CARD") {
            payType = "1";
            myJArraypayment.add(PreferenceManager.getdefaultPaymentType(this))
            myJason.put("paymentTypeId", myJArraypayment.toString())
        } else {
            payType = "3";
            myJArraypayment.add("COD")
            myJason.put("paymentTypeId", myJArraypayment.toString())
        }

        myJason.put("email", "${PreferenceManager.getEmail(this)}")
        myJason.put("paymentType", payType.toString())
        myJason.put("cartAllowTime", "${cartAllowTime}")
        myJason.put("orderDiscount", orderDiscount)
        myJason.put("promoCode", promoCode)
        myJason.put("orderComments", orderComments)
        myJason.put("paymentConfirmationId", "")
        myJason.put("paymentResponse", "")
        myJason.put("orderType", orderType.toString())
        myJason.put("deliveryCharge", "${deliveryCharge}")
        myJason.put("tip", tvTipVal.text.toString().replace("$", ""))

        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.insertOrder(gsonObject)
        call.enqueue(object : Callback<InsertOrder> {
            override fun onFailure(call: Call<InsertOrder>, t: Throwable) {
                progressDialog?.dismiss()
                Toast.makeText(
                    this@ReviewCheckoutActivity,
                    "" + t.toString(),
                    Toast.LENGTH_SHORT
                )

            }

            override fun onResponse(
                call: Call<InsertOrder>,
                response: Response<InsertOrder>
            ) {
                progressDialog?.dismiss()
                if (response.isSuccessful) {

                    if (response.body()!!.responseCode == 200) {

                        if (orderType == 2) {
                            delivery = "pickup"
                        } else if (orderType == 2) {
                            delivery = "Delivery"
                        } else {
                            delivery = "Dine In"
                        }

                        val intent =
                            Intent(this@ReviewCheckoutActivity, PlaceorderComplete::class.java)
                        intent.putExtra("deliveryon", "" + delivery)
                        intent.putExtra("date", "" + timelist[timeselectedP].dateDisplay)
                        intent.putExtra(
                            "time",
                            "" + timelist[timeselectedP].startTime + "-" + timelist[timeselectedP].endTime
                        )
                        intent.putExtra("total", "" + tvFinalTotal.text.toString())
                        intent.putExtra(
                            "dateandtime",
                            "" + timelist[timeselectedP].checkOutReplaceEndTimeDisplay
                        )
                        startActivity(intent)
                    } else {

                        Toast.makeText(
                            this@ReviewCheckoutActivity,
                            "" + response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@ReviewCheckoutActivity,
                        "" + response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    fun PromocodeApi() {
        progressDialog = CustomProgressbar().show(this@ReviewCheckoutActivity)
        val myJason = JSONObject()
        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("vendorId", PreferenceManager.getVendorId(this))
        myJason.put("promoCode", copunCode.text.toString())
        myJason.put("amount", rowTotal.text.toString().replace("$", "").replace(" ", ""))

        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.validatePromoCode(gsonObject)

        call.enqueue(object : Callback<Promocoderesponse> {
            override fun onResponse(
                call: Call<Promocoderesponse>,
                response: Response<Promocoderesponse>
            ) {

                progressDialog!!.dismiss()
                if (response.isSuccessful) {
                    if (response.body()?.responseCode == 200) {

                        applyCoupan.text = "CANCEL"
                        promoCode = response.body()?.result?.promoCode.toString()
                        orderDiscount = response.body()?.result?.totalDiscount.toString()
                        tvPromocode.text = orderDiscount
                        tvFinalTotal.text =
                            "${response.body()!!.result.currency} " + "${response.body()!!.result.amountAfterDiscount}"

                    } else {
                        toast("Invalide Promo code")
                        Toast.makeText(
                            this@ReviewCheckoutActivity,
                            "" + response.body()!!.message,
                            Toast.LENGTH_SHORT
                        )
                    }
                } else {
                    Toast.makeText(
                        this@ReviewCheckoutActivity,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    )
                }
            }

            override fun onFailure(call: Call<Promocoderesponse>, t: Throwable) {
                progressDialog!!.dismiss()

                Toast.makeText(
                    this@ReviewCheckoutActivity,
                    "" + t.message,
                    Toast.LENGTH_SHORT
                )
            }
        })
    }


}