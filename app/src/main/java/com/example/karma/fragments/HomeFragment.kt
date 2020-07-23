package com.example.karma.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.karma.R
import com.example.karma.activities.ProductListActivity
import com.example.karma.activities.SearchActivity
import com.example.karma.adapter.HomeCategoryListAdapter
import com.example.karma.adapter.pagerAdapter.SlidingImage_Pager_Adapter
import com.example.karma.interfaces.HomeCategoryClickListner
import com.example.karma.interfaces.SliderClickListner
import com.example.karma.response.HomeScreenResponse
import com.example.karma.responseModel.CategoryModel
import com.example.karma.responseModel.SliderInfoModel
import com.example.karma.retrofit.ApiClient
import com.example.karma.retrofit.ApiInterface
import com.example.karma.utils.AppIDConfig
import com.example.karma.utils.CustomProgressbar
import com.example.karma.utils.PreferenceManager
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(), HomeCategoryClickListner, SliderClickListner {

    var progressDialog: Dialog? = null
    private var mPager: ViewPager? = null
    private var currentPage = 0
    private var NUM_PAGES = 0
    var indicator: CirclePageIndicator? = null
    var call: Call<HomeScreenResponse>? = null
    var swipeTimer = Timer()
    private var imageModelArrayList: ArrayList<SliderInfoModel>? = null
    private var catList: ArrayList<CategoryModel>? = ArrayList()
    private var catAdapter: HomeCategoryListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        mPager = view.findViewById(R.id.pager)
        indicator = view.findViewById(R.id.indicator)
        imageModelArrayList = ArrayList()

        view.ll_searchLL.setOnClickListener {

            startActivity(Intent(context, SearchActivity::class.java))

        }


        progressDialog = CustomProgressbar().show(context!!)
        val myJason = JSONObject()
        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("vendorId", PreferenceManager.getVendorId(context!!))

        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        call = apiService.homePageInfo(gsonObject)
        call!!.enqueue(object : Callback<HomeScreenResponse> {
            @SuppressLint("ShowToast")
            override fun onResponse(
                call: Call<HomeScreenResponse>,
                response: Response<HomeScreenResponse>
            ) {
                progressDialog!!.dismiss()
                if (response.body()!!.responseCode == 200) {

                    catList = (response.body()!!.result.categories)
                    imageModelArrayList = response.body()!!.result.sliderInfo

                    if (imageModelArrayList.isNullOrEmpty()) {
                        sliderFrame.visibility = View.GONE
                    }


                    slider(imageModelArrayList!!)

                    catAdapter = HomeCategoryListAdapter(
                        context!!,
                        catList!!,
                        this@HomeFragment
                    )

                    val manager =
                        GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                    view.rv_catList?.layoutManager = manager
                    view.rv_catList?.itemAnimator = DefaultItemAnimator()
                    view.rv_catList?.adapter = catAdapter

                } else if (response.body()!!.responseCode == 514) {
                    var msg: String = response.body()!!.message
                    Log.e("Response_Fail", "" + response.body()!!.message)
//                    Toast.makeText(
//                        context,
//                        msg,
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            }

            override fun onFailure(call: Call<HomeScreenResponse>, t: Throwable) {
                progressDialog!!.dismiss()

//                Toast.makeText(
//                    context,
//                    "" + t.message,
//                    Toast.LENGTH_SHORT
//                )
            }
        })




        return view
    }

    private fun slider(images: ArrayList<SliderInfoModel>) {

        mPager!!.adapter = SlidingImage_Pager_Adapter(context!!, images, this)
        indicator!!.setViewPager(mPager)
        val density = resources.displayMetrics.density

        indicator!!.radius = 5 * density
        NUM_PAGES = imageModelArrayList!!.size
        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            mPager!!.setCurrentItem(currentPage++, true)
        }
        // Automatic Swip viewPager
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3500, 3500)
        // Pager listener over indicator
        indicator!!.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                currentPage = position
            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(pos: Int) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        swipeTimer.cancel()
    }

    override fun getProductListClick(categoryId: String, catTitle: String) {

        val intent: Intent = Intent(context, ProductListActivity::class.java)
        intent.putExtra("categoryId", "" + categoryId)
        intent.putExtra("catTitle", "" + catTitle)
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()

        call.let { c -> c?.cancel(); }
    }

    override fun sliderClick(catId: String, catTitle: String) {

        val intent: Intent = Intent(context, ProductListActivity::class.java)
        intent.putExtra("categoryId", "" + catId)
        intent.putExtra("catTitle", "" + catTitle)
        startActivity(intent)

    }

}
