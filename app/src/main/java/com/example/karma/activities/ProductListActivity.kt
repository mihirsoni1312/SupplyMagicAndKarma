package com.example.karma.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.karma.R
import com.example.karma.adapter.CPListAdapter
import com.example.karma.adapter.ProductListAdapter
import com.example.karma.adapter.RelatedProductAdapter
import com.example.karma.base.BaseActivity
import com.example.karma.response.ProductByCategoryIdResponse
import com.example.karma.responseModel.ProductListModel
import com.example.karma.responseModel.RelatedProduct
import com.example.karma.retrofit.ApiClient
import com.example.karma.retrofit.ApiInterface
import com.example.karma.utils.AppIDConfig
import com.example.karma.utils.CustomProgressbar
import com.example.karma.utils.PaginationScrollListener
import com.example.karma.utils.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.layout_bottom_sheet_cp.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductListActivity : BaseActivity(), ProductListAdapter.RelatedProduct,
    CPListAdapter.ItemListenerCP {

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var CurrentPageCount = 1
    private var recyclerView: RecyclerView? = null
    var categoryId: String = ""
    var catTitle: String = ""
    var progressDialog: Dialog? = null
    private var productList: ArrayList<ProductListModel>? = ArrayList()
    private var productListAdapter: ProductListAdapter? = null
    private var behavior: BottomSheetBehavior<LinearLayout>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)


        categoryId = intent.getStringExtra("categoryId")
        catTitle = intent.getStringExtra("catTitle")


        title_productList.text = catTitle

        title_productList.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))

        btn_back_productList.setOnClickListener {
            finish()
        }

        ImageViewCompat.setImageTintList(
            btn_back_productList,
            ColorStateList.valueOf(Color.parseColor(PreferenceManager.getFontColor(this)))
        )


        loadData(CurrentPageCount)

        behavior = BottomSheetBehavior.from<LinearLayout>(bottom_sheet_cp)

        behavior!!.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // React to state change
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })

        productListAdapter = ProductListAdapter(
            this@ProductListActivity,
            this@ProductListActivity
        )

        val mLayoutManager =
            LinearLayoutManager(
                this@ProductListActivity,
                LinearLayoutManager.VERTICAL,
                false
            )

        rv_productList?.layoutManager = mLayoutManager
        rv_productList?.itemAnimator = DefaultItemAnimator()
        rv_productList?.adapter = productListAdapter
        rv_productList?.addOnScrollListener(object : PaginationScrollListener(mLayoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                CurrentPageCount++
                loadData(CurrentPageCount)
                isLoading = true
            }
        })


    }

    private fun loadData(CurrentPageCount: Int) {
        progressDialog = CustomProgressbar().show(this@ProductListActivity)
        val myJason = JSONObject()
        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("categoryId", categoryId)
        myJason.put("vendorId", PreferenceManager.getVendorId(this))
        myJason.put("page", CurrentPageCount)
        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.productListByCategoryId(gsonObject)
        call.enqueue(object : Callback<ProductByCategoryIdResponse> {
            @SuppressLint("ShowToast")
            override fun onResponse(
                call: Call<ProductByCategoryIdResponse>,
                response: Response<ProductByCategoryIdResponse>
            ) {
                isLoading = false
                progressDialog!!.dismiss()
                if (response.body()?.responseCode == 200) {
                    if (response.body()!!.result != null) {
                        txt_noData.visibility = View.GONE
                        rv_productList.visibility = View.VISIBLE

                        if (CurrentPageCount == response.body()?.result?.totalPages) {
                            isLastPage = true
                        }

                        if (response.body()?.result?.productList.isNullOrEmpty()) {
                            if (CurrentPageCount == 1) {
                                txt_noData.visibility = View.VISIBLE
                            }
                        } else {
                            txt_noData.visibility = View.GONE
                            productListAdapter?.addData(response.body()?.result?.productList!!)
                        }


                    } else {
                        txt_noData.visibility = View.VISIBLE
                        rv_productList.visibility = View.GONE
                    }
                } else {

                    Toast.makeText(this@ProductListActivity, "Fail", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<ProductByCategoryIdResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                isLoading = false
//                Toast.makeText(
//                    this@ProductListActivity,
//                    "" + t.message,
//                    Toast.LENGTH_SHORT
//                )
            }
        })
    }

    override fun openRelatedProduct(p: Int, relatedProductList: List<RelatedProduct>) {
        recyclerView = findViewById(R.id.recyclerView_cp)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter =
            RelatedProductAdapter(this, relatedProductList as ArrayList<RelatedProduct>)
        behavior!!.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onItemClick(
        pos: Int,
        name: String?,
        _id: String,
        newPrice: String,
        isFavPro: Boolean,
        qty: String,
        op: String,
        iAv: Boolean
    ) {
        behavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onBackPressed() {
        finish()
    }
}