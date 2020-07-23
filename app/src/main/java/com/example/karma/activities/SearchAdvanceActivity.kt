package com.example.karma.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
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
import com.example.karma.utils.*
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.activity_search_advance.*
import kotlinx.android.synthetic.main.activity_search_advance.txt_noData
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchAdvanceActivity : BaseActivity(), SearchToAddToCart {

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var CurrentPageCount = 1

    var searchAdapter: SearchAdapter? = null
    var searchList: ArrayList<ProductListModel> = ArrayList()
    var progressDialog: Dialog? = null
    var searchText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_advance)

        title_forgotPassword.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        searchText = intent.getStringExtra("searchString")

        btn_back_searchAdvanced.setOnClickListener {
            onBackPressed()

        }

        loadData(searchText, CurrentPageCount)


        searchAdapter = SearchAdapter(
            this@SearchAdvanceActivity,
            this@SearchAdvanceActivity
        )
        val mLayoutManager =
            LinearLayoutManager(
                this@SearchAdvanceActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        tv_search_advance?.layoutManager = mLayoutManager
        tv_search_advance?.itemAnimator = DefaultItemAnimator()
        tv_search_advance?.adapter = searchAdapter
        tv_search_advance?.addOnScrollListener(object : PaginationScrollListener(mLayoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                CurrentPageCount++
                loadData(searchText, CurrentPageCount)
                isLoading = true
            }
        })
    }


    private fun loadData(text: String, CurrentPageCount: Int) {
        progressDialog = CustomProgressbar().show(this@SearchAdvanceActivity)
        val myJason = JSONObject()
        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("textsearch", text)
        myJason.put("vendorId", PreferenceManager.getVendorId(this))
        myJason.put("page", CurrentPageCount)
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
                progressDialog?.dismiss()
                isLoading = false
                if (response.body()?.responseCode == 200) {

                    if (response.body()?.result != null) {

                        searchList = response.body()!!.result.productList

                        if (searchList.isNullOrEmpty()) {
                            if (CurrentPageCount == 1) {
                                searchAdapter?.clearList()
                                txt_noData.visibility = View.VISIBLE
                            }
                        } else {
                            txt_noData.visibility = View.GONE
                            searchAdapter?.addData(searchList)
                        }
                        if (CurrentPageCount == response.body()?.result?.totalPages) {
                            isLastPage = true
                        }
                    } else {
                        txt_noData.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(this@SearchAdvanceActivity, "Fail", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                progressDialog?.dismiss()
                Toast.makeText(
                    this@SearchAdvanceActivity,
                    "" + t.message,
                    Toast.LENGTH_SHORT
                )
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        var intent: Intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    override fun searchClick(productId: String) {
//        addToCart(productId)
    }

    private fun addToCart(productId: String) {


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
                progressDialog!!.dismiss()
                if (response.body()!!.responseCode == 200) {

                    toast(response.body()!!.message)

                } else {

                    Toast.makeText(this@SearchAdvanceActivity, "Fail", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<CommonSimpleResponse>, t: Throwable) {
                progressDialog!!.dismiss()

                Toast.makeText(
                    this@SearchAdvanceActivity,
                    "" + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}