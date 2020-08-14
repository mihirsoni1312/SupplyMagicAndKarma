package com.example.karma.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.View
import android.webkit.WebSettings
import android.widget.LinearLayout
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.karma.R
import com.example.karma.adapter.CPListAdapter
import com.example.karma.adapter.pagerAdapter.SlidingImage_Product_Pager_Adapter
import com.example.karma.base.BaseActivity
import com.example.karma.response.CommonSimpleResponse
import com.example.karma.response.ProductByProductIdResponse
import com.example.karma.responseModel.productByProductId.CPModel
import com.example.karma.retrofit.ApiClient
import com.example.karma.retrofit.ApiInterface
import com.example.karma.utils.AppIDConfig
import com.example.karma.utils.CustomProgressbar
import com.example.karma.utils.PreferenceManager
import com.example.karma.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.content_product_detail.*
import kotlinx.android.synthetic.main.layout_bottom_sheet_cp.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ProductDetailActivity : BaseActivity(), CPListAdapter.ItemListenerCP {

    private var mPager: ViewPager? = null
    private var behavior: BottomSheetBehavior<LinearLayout>? = null
    private var currentPage = 0
    private var NUM_PAGES = 0
    var currence = ""
    var iav: Boolean = false
    var isFav: Boolean = false

    var indicator: CirclePageIndicator? = null
    var swipeTimer = Timer()
    private var imageModelArrayList: ArrayList<String>? = null
    var progressDialog: Dialog? = null
    var digit: Int = 0
    var productId: String = ""
    private var Id: String = ""
    private var coordinatorLayout: CoordinatorLayout? = null
    private var recyclerView: RecyclerView? = null
    var pId: String = ""
    private var mAdapter: CPListAdapter? = null
    private var cpList: ArrayList<CPModel> = ArrayList()
    var positionforCpList: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val upArrow = resources.getDrawable(R.drawable.ic_backwithbackground, null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            upArrow.colorFilter =
                BlendModeColorFilter(resources.getColor(R.color.black, null), BlendMode.SRC_ATOP)
        } else {
            upArrow.setColorFilter(
                resources.getColor(R.color.black, null),
                PorterDuff.Mode.SRC_ATOP
            )
        }

        coordinatorLayout = findViewById(R.id.coordinateLayoutID)

        behavior = BottomSheetBehavior.from<LinearLayout>(bottom_sheet_cp)
        behavior!!.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // React to state change
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })

//        supportActionBar!!.setHomeAsUpIndicator(upArrow)
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        supportActionBar!!.setDisplayShowHomeEnabled(true)


        if (intent.getStringExtra("pId").isNotBlank()) {
            pId = intent.getStringExtra("pId")
        }
        mPager = findViewById(R.id.pager)
        indicator = findViewById(R.id.indicator)
        imageModelArrayList = ArrayList()

        progressDialog = CustomProgressbar().show(this@ProductDetailActivity)
        txt_btn_orderNow.setBackgroundColor(
            Color.parseColor(
                PreferenceManager.getBackgroundColor(
                    this
                )
            )
        )
        txt_btn_orderNow.setTextColor(Color.parseColor(PreferenceManager.getButtonFontColor(this)))
        loadData()

        recyclerView = findViewById(R.id.recyclerView_cp)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this@ProductDetailActivity)

        txt_add.setOnClickListener {

            digit = txt_dispayDigit.text.toString().toInt() + 1

            txt_dispayDigit.text = digit.toString()

        }


        card_dropDown.setOnClickListener {
            behavior!!.state = BottomSheetBehavior.STATE_EXPANDED
            /* if (behavior!!.state != BottomSheetBehavior.STATE_EXPANDED) {
                 behavior!!.state = BottomSheetBehavior.STATE_EXPANDED
                 //behavior.text = "Close Bottom Sheet"
             } else {

                 //btBottomSheet.text = "Expand Bottom Sheet"
             }*/

            // behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        }

        txt_minus.setOnClickListener {

            if (txt_dispayDigit.text.toString() != "0") {
                digit = txt_dispayDigit.text.toString().toInt() - 1
                txt_dispayDigit.text = digit.toString()
            }
        }

        txt_btn_orderNow.setOnClickListener {
            if (iav) {
                if (txt_dispayDigit.text.toString() == "0") {
                    toast("Please add at least one quantity")
                } else {

                    addToCart()
                }
            }


        }

        btn_back.setOnClickListener {
            onBackPressed()
        }

        cvAddToWishList.setOnClickListener {
            if (isFav) {

                deleteFromWishList()
            } else {
                addToWishList()
            }
        }

        tvUnavalable.setTextColor(Color.parseColor(PreferenceManager.getFontColor(context)))
        txt_productTitle.setTextColor(
            Color.parseColor(
                PreferenceManager.getBackgroundColor(
                    this
                )
            )
        )
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun slider(images: ArrayList<String>) {
        mPager!!.adapter = SlidingImage_Product_Pager_Adapter(
            context,
            images
        )
        indicator!!.setViewPager(mPager)
        val density = resources.displayMetrics.density
        //Set circle indicator radius
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.addtofav, menu)
        return true
    }

    private fun loadData() {
        val myJason = JSONObject()
        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("productId", pId)
//        myJason.put("deviceType", "")
        myJason.put("vendorId", PreferenceManager.getVendorId(this))
        myJason.put("userId", PreferenceManager.getId(this))
        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.productByProductId(gsonObject)
        call.enqueue(object : Callback<ProductByProductIdResponse> {
            @SuppressLint("ShowToast", "SetJavaScriptEnabled", "SetTextI18n")
            override fun onResponse(
                call: Call<ProductByProductIdResponse>,
                response: Response<ProductByProductIdResponse>
            ) {

                progressDialog!!.dismiss()
                if (response.body()!!.responseCode == 200) {


                    if (response.body()!!.result.i1.isNotEmpty()) {
                        imageModelArrayList!!.add(response.body()!!.result.i1)
                    }
                    if (response.body()!!.result.i2.isNotEmpty()) {
                        imageModelArrayList!!.add(response.body()!!.result.i2)
                    }
                    if (response.body()!!.result.i3.isNotEmpty()) {
                        imageModelArrayList!!.add(response.body()!!.result.i3)
                    }

                    tvSdis.text = response.body()?.result?.sdesc

                    isFav = response.body()?.result?.isFavPro!!
                    if (response.body()?.result?.isFavPro!!) {
                        fav.setImageDrawable(resources.getDrawable(R.drawable.favoriteicon))
                    }
                    if (imageModelArrayList!!.size == 1) {
                        indicator!!.visibility = View.GONE
                    } else {
                        indicator!!.visibility = View.VISIBLE
                    }
                    cpList = response.body()!!.result.cP
                    if (cpList.size > 0) {
                        card_dropDown.visibility = View.VISIBLE
                        quantity_product_txt.text = cpList.let { cpList[0].name }
                    } else {
                        card_dropDown.visibility = View.INVISIBLE
                    }
                    if (cpList.size > 0) {
                        if (cpList[0].isFavPro) {
                            fav.setImageDrawable(resources.getDrawable(R.drawable.favoriteicon))
                        }
                        txt_price.text = "${cpList[0].pI.c}" + cpList[0].let { cpList[0].pI.n }
                        isFav = cpList[0].isFavPro
                        currence = response.body()?.result?.pI?.c!!
                    }

                    txt_oldprice.text =
                        "${response.body()!!.result.pI.c}" + "${response.body()!!.result.pI.o}"
                    txt_oldprice.paintFlags = txt_oldprice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    iav = response.body()!!.result.iAv
                    if (iav) {
                        tvUnavalable.visibility = View.GONE
                    } else {
                        txt_btn_orderNow.setBackgroundColor(
                            resources.getColor(R.color.grey)
                        )
                        tvUnavalable.visibility = View.VISIBLE
                    }
                    mAdapter = CPListAdapter(
                        this@ProductDetailActivity,
                        cpList,
                        this@ProductDetailActivity,
                        pId
                    )
                    recyclerView!!.adapter = mAdapter

                    txt_productTitle.text = response.body()!!.result.name
                    txt_sdesc.text = response.body()!!.result.sdesc
                    // txt_price.text = response.body()!!.result.pI.n
                    productId = response.body()!!.result._id

                    webview.settings.javaScriptEnabled = true
                    val websetting: WebSettings = webview.settings

                    websetting.defaultTextEncodingName = "utf-8"
                    webview.loadDataWithBaseURL(
                        null,
                        response.body()!!.result.desc,
                        "text/html",
                        "UTF-8",
                        null
                    )
                    if (cpList.size > 0) {
                        txt_dispayDigit.text = response.body()!!.result.cP[0].qty
                        slider(imageModelArrayList!!)
                    } else {
                        txt_dispayDigit.text = response.body()!!.result.qty
                        slider(imageModelArrayList!!)
                    }
                } else {

                    Toast.makeText(this@ProductDetailActivity, "Fail", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<ProductByProductIdResponse>, t: Throwable) {
                progressDialog!!.dismiss()

                Toast.makeText(
                    this@ProductDetailActivity,
                    "" + t.message,
                    Toast.LENGTH_SHORT
                )
            }
        })
    }

    private fun addToCart() {
        progressDialog = CustomProgressbar().show(this@ProductDetailActivity)
        mAdapter?.setQuenty(txt_dispayDigit.text.toString(), positionforCpList)
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
        myJason.put("quantity", txt_dispayDigit.text.toString().toInt())
        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject = jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.addToCart(gsonObject)
        call.enqueue(object : Callback<CommonSimpleResponse> {

            override fun onResponse(
                call: Call<CommonSimpleResponse>,
                response: Response<CommonSimpleResponse>
            ) {
                if (response.isSuccessful) {
                    progressDialog!!.dismiss()
                    if (response.body()!!.responseCode == 200) {
                        toast(response.body()!!.message)
//                        loadData()
                    } else {
                        Toast.makeText(this@ProductDetailActivity, "Fail", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<CommonSimpleResponse>, t: Throwable) {
                progressDialog!!.dismiss()

                Toast.makeText(
                    this@ProductDetailActivity,
                    "" + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })


    }

    private fun addToWishList() {
        progressDialog = CustomProgressbar().show(this@ProductDetailActivity)

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
        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.addToWishList(gsonObject)
        call.enqueue(object : Callback<CommonSimpleResponse> {
            @SuppressLint("ShowToast")
            override fun onResponse(
                call: Call<CommonSimpleResponse>,
                response: Response<CommonSimpleResponse>
            ) {
                progressDialog!!.dismiss()
                if (response.body()!!.responseCode == 200) {
//                    toast(response.body()!!.status)
                    isFav = true
                    fav.setImageDrawable(resources.getDrawable(R.drawable.favoriteicon))
                    Toast.makeText(
                        this@ProductDetailActivity,
                        "Item added in your wishlist",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(this@ProductDetailActivity, "Fail", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CommonSimpleResponse>, t: Throwable) {
                progressDialog!!.dismiss()

                Toast.makeText(
                    this@ProductDetailActivity,
                    "" + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    private fun deleteFromWishList() {

        val myJason = JSONObject()
        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("vendorId", PreferenceManager.getVendorId(context))
        myJason.put("productId", productId)
        myJason.put("userId", PreferenceManager.getId(context))
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
                if (response.isSuccessful) {
                    isFav = false
                    fav.setImageDrawable(resources.getDrawable(R.drawable.unfavoriteicon))
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

    override fun onItemClick(
        pos: Int,
        name: String?,
        _id: String,
        newPrice: String,
        isFavPro: Boolean,
        qty: String,
        opp: String,
        iAv: Boolean
    ) {
        quantity_product_txt.text = name
        Id = _id
        positionforCpList = pos
        txt_price.text = "$currence$newPrice"
        productId = _id
        behavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        isFav = isFavPro
        txt_dispayDigit.text = qty
        if (isFavPro) {
            fav.setImageDrawable(resources.getDrawable(R.drawable.favoriteicon))
        } else {
            fav.setImageDrawable(resources.getDrawable(R.drawable.unfavoriteicon))
        }
        txt_oldprice.text = "${currence}" + "${opp}"

        iav = iAv
        if (iav) {
            txt_btn_orderNow.setBackgroundColor(
                Color.parseColor(
                    PreferenceManager.getBackgroundColor(
                        this
                    )
                )
            )
            tvUnavalable.visibility = View.GONE
        } else {
            txt_btn_orderNow.setBackgroundColor(
                resources.getColor(R.color.grey)
            )
            tvUnavalable.visibility = View.VISIBLE
        }
    }

}