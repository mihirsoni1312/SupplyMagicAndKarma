package com.example.karma.fragments

import android.app.Dialog
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
import com.example.karma.activities.OrderDetailActivity
import com.example.karma.adapter.OrderListAdapter
import com.example.karma.interfaces.OrderItemClickListner
import com.example.karma.response.getOrderListByUserId.GetOrderListByUserId
import com.example.karma.response.getOrderListByUserId.ProductList
import com.example.karma.response.getOrderListForAdminByAppIdVendorId.GetOrderListForAdminByAppIdVendorIdResponse
import com.example.karma.response.getOrderListForAdminByAppIdVendorId.OrderList
import com.example.karma.retrofit.ApiClient
import com.example.karma.retrofit.ApiInterface
import com.example.karma.utils.AppIDConfig
import com.example.karma.utils.CustomProgressbar
import com.example.karma.utils.PaginationScrollListener
import com.example.karma.utils.PreferenceManager
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.fragment_orders.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class OrdersFragment : Fragment(), OrderItemClickListner {

    lateinit var orderListAdapter: OrderListAdapter
    var CurrentPageCount = 1
    var isLastPage: Boolean = false
    var isLoading: Boolean = false

    var progressDialog: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_orders, container, false)




        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title_productList.setTextColor(Color.parseColor(activity?.let {
            PreferenceManager.getFontColor(
                it
            )
        }))

        view.btn_back_order.setOnClickListener {

            val mainIntent = Intent(context, MainActivity::class.java)
            mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(mainIntent)

        }
        orderListAdapter = OrderListAdapter(
            activity!!,
            this@OrdersFragment
        )

        val mLayoutManager =
            LinearLayoutManager(
                activity,
                LinearLayoutManager.VERTICAL,
                false
            )

        rv_catList?.layoutManager = mLayoutManager
        rv_catList?.itemAnimator = DefaultItemAnimator()
        rv_catList.adapter = orderListAdapter
        rv_catList?.addOnScrollListener(object : PaginationScrollListener(mLayoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                CurrentPageCount++
                loadData()
                isLoading = true
            }
        })

        loadData()
    }

    override fun onResume() {
        super.onResume()

        Objects.requireNonNull((Objects.requireNonNull(activity) as AppCompatActivity).supportActionBar)?.hide()


    }

    override fun onStop() {
        super.onStop()
        Objects.requireNonNull((Objects.requireNonNull(activity) as AppCompatActivity).supportActionBar)?.show()
    }

    override fun OrderItemClick(order: ProductList) {
        val i = Intent(activity, OrderDetailActivity::class.java)
        i.putExtra("orderId", order.id)
        startActivity(i)
    }

    fun loadData() {

        progressDialog = context?.let { CustomProgressbar().show(it) }!!
        val myJason = JSONObject()
        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("vendorId", PreferenceManager.getVendorId(activity!!))
        myJason.put("userId", PreferenceManager.getId(activity!!))
        myJason.put("page", CurrentPageCount.toString())
        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject = jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.getOrderListByUserId(gsonObject)
        call.enqueue(object : Callback<GetOrderListByUserId> {
            override fun onFailure(
                call: Call<GetOrderListByUserId>,
                t: Throwable
            ) {
                isLoading = false
                progressDialog?.dismiss()
                Toast.makeText(
                    activity,
                    "" + t.toString(),
                    Toast.LENGTH_SHORT
                ).show()

            }

            override fun onResponse(
                call: Call<GetOrderListByUserId>,
                response: Response<GetOrderListByUserId>
            ) {
                isLoading = false
                progressDialog?.dismiss()
                if (response.isSuccessful) {
                    if (response.body()?.result != null) {

                        if (CurrentPageCount == response.body()?.result?.totalPages) {
                            isLastPage = true
                        }


                        if (!response.body()!!.result?.productList.isNullOrEmpty()) {
                            txt_noData.visibility = View.GONE
                            orderListAdapter.addDate(response.body()!!.result!!.productList as ArrayList<ProductList>)
                        }else{
                            txt_noData.visibility = View.VISIBLE
                        }
                    }
                } else {
//                    Toast.makeText(activity, "" + response.message(), Toast.LENGTH_SHORT).show()
                }
            }
        })


    }

}
