package com.example.karma.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.karma.R
import com.example.karma.activities.MainActivity
import com.example.karma.activities.ReviewCheckoutActivity
import com.example.karma.adapter.CartListAdapter
import com.example.karma.interfaces.DeleteCartClickListner
import com.example.karma.response.CartListResponse
import com.example.karma.response.CommonSimpleResponse
import com.example.karma.responseModel.productByProductId.ProductInfoCartModel
import com.example.karma.retrofit.ApiClient
import com.example.karma.retrofit.ApiInterface
import com.example.karma.utils.AppIDConfig
import com.example.karma.utils.CustomProgressbar
import com.example.karma.utils.PreferenceManager
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_cart.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class CartFragment : Fragment(), DeleteCartClickListner, CartListAdapter.TotalChange {

    private var cartList: ArrayList<ProductInfoCartModel>? = ArrayList()
    private var cartAdapter: CartListAdapter? = null
    var cartId: String = ""
    var orderId: String = ""
    var totalTax: String = ""

    var call: Call<CartListResponse>? = null
    lateinit var progressDialog: Dialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_cart, container, false)
        view.btn_back_cart.setOnClickListener {

            val mainIntent = Intent(context, MainActivity::class.java)
            mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(mainIntent)

        }

        view.btn_checkout.setBackgroundColor(
            Color.parseColor(
                PreferenceManager.getBackgroundColor(
                    activity!!
                )
            )
        )
        view.btn_shop_now.setBackgroundColor(
            Color.parseColor(
                PreferenceManager.getBackgroundColor(
                    activity!!
                )
            )
        )
        view.btn_shop_now.setTextColor(
            Color.parseColor(
                PreferenceManager.getButtonFontColor(
                    activity!!
                )
            )
        )
        view.btn_checkout.setTextColor(
            Color.parseColor(
                PreferenceManager.getButtonFontColor(
                    activity!!
                )
            )
        )
        view.btn_shop_now.setOnClickListener {
            (activity as MainActivity).openMenuScreen()
        }
//        view.comment_edt_txt.setTextColor(
//            Color.parseColor(
//                PreferenceManager.getButtonFontColor(
//                    activity!!
//                )
//            )
//        )
//        onResume()
        view.btn_checkout.setOnClickListener {

            if (cartAdapter?.checkItemAvalable()!!) {

                if (!orderId.equals("")) {
                    apicallsaveCheckoutOrder()
                } else {
                    val mainIntent = Intent(context, ReviewCheckoutActivity::class.java)
                    mainIntent.putExtra("total", tvTotal.text.toString().replace("$", ""))
                    mainIntent.putExtra("cartId", cartId)
                    mainIntent.putExtra("totaltax", totalTax)
                    mainIntent.putExtra("orderdis", comment_edt_txt.text.toString())
                    mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(mainIntent)
                }
            } else {
                Toast.makeText(activity, "Please remove unavailable items", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return view
    }

    override fun onResume() {
        super.onResume()

        title_productList.setTextColor(Color.parseColor(PreferenceManager.getFontColor(activity as Context)))
        tvTotal.setTextColor(Color.parseColor(PreferenceManager.getFontColor(activity as Context)))
        titleTotal.setTextColor(Color.parseColor(PreferenceManager.getFontColor(activity as Context)))
        Objects.requireNonNull((Objects.requireNonNull(activity) as AppCompatActivity).supportActionBar)
            ?.hide()
//        view?.laout_main?.visibility = View.GONE
        getcartinfoveryfy()
    }

    private fun getcartinfoveryfy() {
        progressDialog = context?.let { CustomProgressbar().show(it) }!!
        val myJason = JSONObject()
        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("vendorId", PreferenceManager.getVendorId(context!!))
        myJason.put("userId", PreferenceManager.getId(context!!))

        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        call = apiService.getCartInfo(gsonObject)
        call?.enqueue(object : Callback<CartListResponse> {
            @SuppressLint("ShowToast")
            override fun onResponse(
                call: Call<CartListResponse>,
                response: Response<CartListResponse>
            ) {
                progressDialog.dismiss()

                if (response.isSuccessful)
                    if (response.body()?.responseCode == 200) {

                        if (response.body()?.result?.orderId.equals("")) {
                            btn_checkout.text = "Checkout"
                        } else {
                            orderId = response.body()?.result?.orderId!!
                            btn_checkout.text = "Update Order"
                        }
                        totalTax = response.body()?.result?.totalTax!!
                        cartList = (response.body()?.result?.productInfoList)
                        cartId = response.body()?.result?.cartId!!
                        if (cartList?.isEmpty()!!) {
                            view?.empty_cart_layout?.visibility = View.VISIBLE

                            view?.laout_main?.visibility = View.GONE
                            view?.bottomLayout?.visibility = View.GONE

                            if (PreferenceManager.getIsRestaurentApp(context!!)) {
                                img_emptycart.setImageResource(R.drawable.imgpsh)
                                btn_shop_now.text = "ORDER NOW"
                            }

                        } else {
                            view?.empty_cart_layout?.visibility = View.GONE
                            view?.laout_main?.visibility = View.VISIBLE
                            view?.bottomLayout?.visibility = View.VISIBLE
                        }
                        title_productList.setText("Your Cart (${cartList!!.size})")

                        context?.let {
                            cartAdapter = CartListAdapter(
                                it,
                                cartList!!,
                                this@CartFragment
                                , this@CartFragment
                            )
                        }

                        val mLayoutManager =
                            LinearLayoutManager(
                                context,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        view?.rv_cartList?.layoutManager = mLayoutManager
                        view?.rv_cartList?.itemAnimator = DefaultItemAnimator()
                        view?.rv_cartList?.adapter = cartAdapter
                        tvTotal?.text = "$ %.2f".format(cartAdapter?.getTotal())

                    } else {
//                        var msg: String = response.body()!!.message
//                        Log.e("Response_Fail", "" + response.body()!!.message)
                    }
            }

            override fun onFailure(call: Call<CartListResponse>, t: Throwable) {
                progressDialog.dismiss()

//                Toast.makeText(
//                    context,
//                    "" + t.message,
//                    Toast.LENGTH_SHORT
//                )
            }
        })
    }

    override fun onPause() {
        super.onPause()

        call.let { c -> c?.cancel(); }
    }

    override fun onStop() {
        super.onStop()
        Objects.requireNonNull((Objects.requireNonNull(activity) as AppCompatActivity).supportActionBar)
            ?.show()
    }

    override fun deleteCart(productId: String, size: Int) {
        deleteFromCart(productId)
        if (size <= 1) {
            view?.empty_cart_layout?.visibility = View.VISIBLE
            view?.laout_main?.visibility = View.GONE
            view?.bottomLayout?.visibility = View.GONE
        } else {
            view?.empty_cart_layout?.visibility = View.GONE
            view?.laout_main?.visibility = View.VISIBLE
            view?.bottomLayout?.visibility = View.VISIBLE
        }
    }

    private fun deleteFromCart(productId: String) {

        progressDialog = context?.let { CustomProgressbar().show(it) }!!
        val myJason = JSONObject()
        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("vendorId", PreferenceManager.getVendorId(context!!))
        myJason.put("productId", productId)
        myJason.put("userId", PreferenceManager.getId(context!!))
        myJason.put("cartId", cartId)
        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.removeFromCart(gsonObject)
        call.enqueue(object : Callback<CommonSimpleResponse> {
            @SuppressLint("ShowToast")
            override fun onResponse(
                call: Call<CommonSimpleResponse>,
                response: Response<CommonSimpleResponse>
            ) {
                if (response.body()!!.responseCode == 200) {
                    progressDialog.dismiss()
                    tvTotal?.text = "$ %.2f".format(cartAdapter?.getTotal())
                    Toast.makeText(context, "Item Removed Sucessfully", Toast.LENGTH_SHORT).show()
                    title_productList.setText("Your Cart (${cartList!!.size})")
                } else {
                    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<CommonSimpleResponse>, t: Throwable) {
                progressDialog.dismiss()
//                Toast.makeText(
//                    context,
//                    "" + t.message,
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        })


    }

    override fun totalChangeListener(productId: String, qty: Int) {
        tvTotal.text = "$ %.2f".format(cartAdapter?.getTotal())
        addToCart(productId, qty)
    }

    private fun addToCart(productId: String, qty: Int) {

        progressDialog = context?.let { CustomProgressbar().show(it) }!!
        val myJason = JSONObject()
        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("vendorId", PreferenceManager.getVendorId(context!!))
        myJason.put("productId", productId)
        myJason.put("userId", PreferenceManager.getId(context!!))
        myJason.put("quantity", qty)
        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.addToCart(gsonObject)
        call.enqueue(object : Callback<CommonSimpleResponse> {
            override fun onResponse(
                call: Call<CommonSimpleResponse>,
                response: Response<CommonSimpleResponse>
            ) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    if (response.body()!!.responseCode == 200) {

                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()

                    } else {

                        Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()

                    }
                }
            }

            override fun onFailure(call: Call<CommonSimpleResponse>, t: Throwable) {
                progressDialog.dismiss()
//                Toast.makeText(
//                    context,
//                    "" + t.message,
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        })

    }


    fun apicallsaveCheckoutOrder() {

        progressDialog = context?.let { CustomProgressbar().show(it) }!!
        val myJason = JSONObject()
        myJason.put("orderId", orderId)
        myJason.put("userId", PreferenceManager.getId(context!!))
        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("vendorId", PreferenceManager.getVendorId(context!!))
        myJason.put("cartId", cartId)

        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.saveCheckoutOrder(gsonObject)
        call.enqueue(object : Callback<CommonSimpleResponse> {
            override fun onFailure(call: Call<CommonSimpleResponse>, t: Throwable) {
                progressDialog.dismiss()
            }

            override fun onResponse(
                call: Call<CommonSimpleResponse>,
                response: Response<CommonSimpleResponse>
            ) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    onResume()
                }
            }

        })
    }

}
