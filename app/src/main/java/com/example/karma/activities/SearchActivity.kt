package com.example.karma.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.karma.R
import com.example.karma.adapter.SearchAdapter
import com.example.karma.base.BaseActivity
import com.example.karma.interfaces.SearchToAddToCart
import com.example.karma.response.CommonSimpleResponse
import com.example.karma.response.SearchResponse
import com.example.karma.responseModel.ProductListModel
import com.example.karma.retrofit.ApiClient
import com.example.karma.retrofit.ApiInterface
import com.example.karma.utils.AppIDConfig
import com.example.karma.utils.CustomProgressbar
import com.example.karma.utils.PreferenceManager
import com.example.karma.utils.toast
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_search.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : BaseActivity(), SearchToAddToCart {


    var searchAdapter: SearchAdapter? = null
    var searchList: ArrayList<ProductListModel> = ArrayList()
    var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        title_forgotPassword  .setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        edt_search.requestFocus()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        btn_back_search.setOnClickListener {

            onBackPressed()

        }

        edt_search.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

                if (s.length != 0) {
                    getSearchData(s.toString())
                } else {
                    searchAdapter?.let { it.clearList() }
                    searchList.clear()
                }
            }
        })

        edt_search.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action === KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                if (edt_search.text.toString().isNotEmpty()) {
                    val intent = Intent(this, SearchAdvanceActivity::class.java)
                    intent.putExtra("searchString", edt_search.text.toString())
                    startActivity(intent)
                } else {

                }

                return@OnKeyListener true
            }
            false
        })



        searchAdapter = SearchAdapter(
            this@SearchActivity,
            this@SearchActivity
        )
        tv_search!!.itemAnimator = DefaultItemAnimator()
        tv_search!!.adapter = searchAdapter



    }

    fun getSearchData(Stext: String) {
        loadData(Stext)

    }

    /*{
        "appId": "1",
        "textsearch": "App",
        "vendorId" : "5ee11c4bc2c65605081f2ff9",
        "page": 1
    }*/

    private fun loadData(text: String) {
        val myJason = JSONObject()
        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("textsearch", text)
        myJason.put("vendorId", PreferenceManager.getVendorId(this))
        myJason.put("page", 1)
        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.searchProducts(gsonObject)
        call.enqueue(object : Callback<SearchResponse> {
            @SuppressLint("ShowToast")
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {

                if (response.body()!!.responseCode == 200) {

                    if (response.body()!!.result != null) {

                        searchList = response.body()!!.result.productList

                        if (searchList.isNullOrEmpty()) {
                            searchAdapter?.clearList()
                            txt_noData.visibility = View.VISIBLE
                        } else {
                            searchAdapter?.clearList()
                            txt_noData.visibility = View.GONE


                            searchAdapter?.addData(response.body()?.result?.productList!!)
                        }


                    } else {
                        txt_noData.visibility = View.VISIBLE
//                        rv_productList.visibility = View.GONE
                    }
                } else {
                    Toast.makeText(this@SearchActivity, "Fail", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {

//                Toast.makeText(
//                    this@SearchActivity,
//                    "" + t.message,
//                    Toast.LENGTH_SHORT
//                )
            }
        })
    }

    override fun searchClick(productId: String) {

//        addToCart(productId)

    }

    private fun addToCart(productId: String) {
        progressDialog = CustomProgressbar().show(this@SearchActivity)

        /*"appId": "", (Required)
        "vendorId": "", (Required)
        "userId": "", (Required)
        "productId": "", (Required)
        "quantity": 10 (Required, But should int)*/

        val myJason = JSONObject()
        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("vendorId", PreferenceManager.getVendorId(this))
        myJason.put("productId", productId)
        myJason.put("userId", PreferenceManager.getId(this))
        myJason.put("quantity", "1")
        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.addToCart(gsonObject)
        call.enqueue(object : Callback<CommonSimpleResponse> {
            @SuppressLint("ShowToast")
            override fun onResponse(
                call: Call<CommonSimpleResponse>,
                response: Response<CommonSimpleResponse>
            ) {
                progressDialog?.dismiss()
                if (response.body()?.responseCode == 200) {

                    toast(response.body()!!.message)

                } else {

                    Toast.makeText(this@SearchActivity, "Fail", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<CommonSimpleResponse>, t: Throwable) {
                progressDialog!!.dismiss()

//                Toast.makeText(
//                    this@SearchActivity,
//                    "" + t.message,
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        })


    }
}