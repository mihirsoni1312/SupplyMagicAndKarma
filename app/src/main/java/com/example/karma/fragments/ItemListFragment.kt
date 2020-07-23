package com.example.karma.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.karma.R
import com.example.karma.activities.MainActivity
import com.example.karma.adapter.CPListAdapter
import com.example.karma.adapter.WishListAdapter
import com.example.karma.adapter.filteradapter
import com.example.karma.interfaces.AddToCartWishListListner
import com.example.karma.interfaces.RemoveWishListClickListner
import com.example.karma.response.Category
import com.example.karma.response.CommonSimpleResponse
import com.example.karma.response.GetWishListResponse
import com.example.karma.responseModel.RelatedProduct
import com.example.karma.responseModel.WishListProductListModel
import com.example.karma.retrofit.ApiClient
import com.example.karma.retrofit.ApiInterface
import com.example.karma.utils.AppIDConfig
import com.example.karma.utils.CustomProgressbar
import com.example.karma.utils.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_product_list.title_productList
import kotlinx.android.synthetic.main.activity_product_list.txt_noData
import kotlinx.android.synthetic.main.fragment_item_list.*
import kotlinx.android.synthetic.main.fragment_item_list.view.*
import kotlinx.android.synthetic.main.layout_bottom_sheet.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ItemListFragment : Fragment(), AddToCartWishListListner, RemoveWishListClickListner,
    WishListAdapter.RelatedProduct, CPListAdapter.ItemListenerCP, filteradapter.ItemListener {

    private var wishList: ArrayList<WishListProductListModel>? = ArrayList()
    private var wishListAdapter: WishListAdapter? = null
    var progressDialog: Dialog? = null
    var call: Call<GetWishListResponse>? = null

    private var filterist: ArrayList<Category>? = ArrayList()
    private var fadapter: filteradapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_item_list, container, false)


        view.btn_back_itemList.setOnClickListener {

            val mainIntent = Intent(context, MainActivity::class.java)
            mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(mainIntent)
        }

        view.icon_filter.setOnClickListener {
            rl_filter.visibility = View.VISIBLE
            (activity as MainActivity).behavior!!.state = BottomSheetBehavior.STATE_EXPANDED
        }


        return view
    }

    override fun onResume() {
        super.onResume()
        Objects.requireNonNull((Objects.requireNonNull(activity) as AppCompatActivity).supportActionBar)
            ?.hide()
        title_productList.setTextColor(Color.parseColor(PreferenceManager.getFontColor(activity as Context)))

        lodedata("")
    }

    fun lodedata(id: String) {
        progressDialog = context?.let { CustomProgressbar().show(it) }!!

        val myJason = JSONObject()
        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("vendorId", PreferenceManager.getVendorId(context!!))
        myJason.put("userId", PreferenceManager.getId(context!!))
        myJason.put("categoryId", id)

        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        call = apiService.getWishListInfo(gsonObject)
        call?.enqueue(object : Callback<GetWishListResponse> {
            @SuppressLint("ShowToast")
            override fun onResponse(
                call: Call<GetWishListResponse>,
                response: Response<GetWishListResponse>
            ) {
                progressDialog?.dismiss()
                if (response.isSuccessful) {

                    wishList = (response.body()?.result?.productList)

                    if (wishList.isNullOrEmpty()) {
                        txt_noData.visibility = View.VISIBLE
                        wishListAdapter?.clearData()
                    } else {
                        txt_noData.visibility = View.GONE
                        context?.let {
                            wishListAdapter?.clearData()
                            wishListAdapter = WishListAdapter(
                                it,
                                wishList!!,
                                this@ItemListFragment,
                                this@ItemListFragment
                                , this@ItemListFragment
                            )
                        }
                    }
                    txt_searchHere.text = "${response.body()?.result?.totalProducts}" + " Result"
                    filterist = response.body()?.result?.filters?.category


                    recyclerView?.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    recyclerView?.setHasFixedSize(true)
                    if (filterist.isNullOrEmpty()) {
                    } else {
                        fadapter = filteradapter(context!!, filterist!!, this@ItemListFragment)
                        recyclerView?.adapter = fadapter
                    }

                    val mLayoutManager =
                        LinearLayoutManager(
                            context,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                    view?.rv_wishlist?.layoutManager = mLayoutManager
                    view?.rv_wishlist?.itemAnimator = DefaultItemAnimator()
                    view?.rv_wishlist?.adapter = wishListAdapter


                } else if (response.body()?.responseCode == 514) {
//                    var msg: String = response.body()?.message
                    Log.e("Response_Fail", "" + response.body()?.message)
//                    Toast.makeText(
//                        context,
//                        response.body()?.message,
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            }

            override fun onFailure(call: Call<GetWishListResponse>, t: Throwable) {
                progressDialog?.dismiss()
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

    override fun addToCartFromWishList(productId: String, quantity: Int) {
        addToCart(productId, quantity)
    }

    private fun addToCart(productId: String, qty: Int) {

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
            @SuppressLint("ShowToast")
            override fun onResponse(
                call: Call<CommonSimpleResponse>,
                response: Response<CommonSimpleResponse>
            ) {

                if (response.body()!!.responseCode == 200) {


                } else {

                    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<CommonSimpleResponse>, t: Throwable) {

//                Toast.makeText(
//                    context,
//                    "" + t.message,
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        })


    }

    override fun removeWishList(productId: String) {
        deleteFromWishList(productId)
    }


    private fun deleteFromWishList(productId: String) {

        val myJason = JSONObject()
        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("vendorId", PreferenceManager.getVendorId(context!!))
        myJason.put("productId", productId)
        myJason.put("userId", PreferenceManager.getId(context!!))
        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.removeFromWishList(gsonObject)
        call.enqueue(object : Callback<CommonSimpleResponse> {
            @SuppressLint("ShowToast")
            override fun onResponse(
                call: Call<CommonSimpleResponse>,
                response: Response<CommonSimpleResponse>
            ) {
                if (response.body()!!.responseCode == 200) {

                    onResume()

                    Toast.makeText(context, "Item Removed Sucessfully", Toast.LENGTH_SHORT).show()

                } else {

                    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<CommonSimpleResponse>, t: Throwable) {
                Toast.makeText(
                    context,
                    "" + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun openRelatedProduct(p: Int, relatedProductList: List<RelatedProduct>) {
//        (activity as MainActivity).recyclerView?.adapter = activity?.let {
//            RelatedProductAdapter(
//                it,
//                relatedProductList as ArrayList<RelatedProduct>
//            )
//        }
//        (activity as MainActivity).behavior!!.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onItemClick(
        name: String?,
        _id: String,
        newPrice: String,
        isFavPro: Boolean,
        qty: String,
        op: String,
        iAv: Boolean
    ) {
        (activity as MainActivity).behavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onItemClick(_id: String) {
        rl_filter.visibility = View.GONE
        lodedata(_id)
        (activity as MainActivity).behavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
    }
}
