package com.example.karma.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.karma.R
import com.example.karma.adapter.NavigationDrawerAdapter
import com.example.karma.base.BaseActivity
import com.example.karma.dummy.MediaItemAdapter
import com.example.karma.fragments.*
import com.example.karma.interfaces.NavigationMenuItemClickListner
import com.example.karma.models.Item
import com.example.karma.response.CreateUserStripeResponse
import com.example.karma.response.PCK
import com.example.karma.response.UpdateCustomerKeyResponse
import com.example.karma.response.VendorListResponse
import com.example.karma.responseModel.VendorListModel
import com.example.karma.retrofit.ApiClient
import com.example.karma.retrofit.ApiClientForSGP
import com.example.karma.retrofit.ApiInterface
import com.example.karma.utils.AppIDConfig
import com.example.karma.utils.PreferenceManager
import com.example.karma.utils.toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.layout_bottom_sheet.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : BaseActivity(), NavigationMenuItemClickListner,
    BottomNavigationView.OnNavigationItemSelectedListener, MediaItemAdapter.ItemListener {

    var backCounter: Int = 0
    var behavior: BottomSheetBehavior<*>? = null
    var recyclerView: RecyclerView? = null
    private var mAdapter: MediaItemAdapter? = null
    private var coordinatorLayout: CoordinatorLayout? = null

    var fromOrderDetail: Boolean = false
    private var itemList = ArrayList<Item>()
    private var navigationDrawerAdapter: NavigationDrawerAdapter? = null
    private var vendorList: ArrayList<VendorListModel> = ArrayList()
    private var plpks: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        txt_vendorName.setTextColor(Color.parseColor(PreferenceManager.getFontColor(context)))
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, 0, 0
        )

        coordinatorLayout = findViewById(R.id.coordinatorLayout)

        if (intent.getBooleanExtra("fromOrderDetail", false)) {
            loadFragment(CartFragment())
            fromOrderDetail = true
            bottom_navigation.selectedItemId = R.id.bottom_cart
        }
        vendorList()

        behavior = BottomSheetBehavior.from(bottom_sheet)
        behavior!!.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // React to state change
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })

        btn_openBottonSheet.setOnClickListener {
            behavior!!.state = BottomSheetBehavior.STATE_EXPANDED
//            vendorList()
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toggle.drawerArrowDrawable.color = getColor(R.color.black)
        } else {
            toggle.drawerArrowDrawable.color = resources.getColor(R.color.black)
        }
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        bottom_navigation.setOnNavigationItemSelectedListener(this)
        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked),
            intArrayOf()
        )
        val colors = intArrayOf(
            Color.parseColor("#C0C5CA"),
            Color.parseColor(PreferenceManager.getBackgroundColor(this)),
            Color.parseColor("#C0C5CA")
        )

        val navigationViewColorStateList = ColorStateList(states, colors)
        bottom_navigation.itemTextColor = navigationViewColorStateList
        bottom_navigation.itemIconTintList = navigationViewColorStateList

        drawer_layout.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {
                window.statusBarColor = resources.getColor(R.color.navigationColor)
                window.navigationBarColor = resources.getColor(R.color.navigationColor)
            }

            override fun onDrawerClosed(drawerView: View) {
                window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
                window.navigationBarColor = resources.getColor(R.color.colorPrimaryDark)
            }

            override fun onDrawerStateChanged(newState: Int) {}
        })

        val userHeader = nav_view.getHeaderView(0)
        val img_backClick: ImageView = userHeader.findViewById(R.id.img_backClick)
        val navUserName: TextView = userHeader.findViewById(R.id.header_txt_name)
        val rl_root_header: RelativeLayout = userHeader.findViewById(R.id.rl_root_header)
        rl_root_header.setBackgroundColor(
            Color.parseColor(
                PreferenceManager.getslideMenuBackGroundColor(
                    this
                )
            )
        )
        navUserName.text = PreferenceManager.getname(this)

        img_backClick.setOnClickListener {
            if (drawer_layout!!.isDrawerOpen(GravityCompat.START)) {
                drawer_layout!!.closeDrawer(GravityCompat.START)
            }
        }

        var staticModel =
            Item("My Account", R.drawable.icn_my_account_3x)
        itemList.add(staticModel)

        staticModel =
            Item("Support", R.drawable.icn_support_my_account_3x)
        itemList.add(staticModel)

        staticModel =
            Item("Payment", R.drawable.payment)
        itemList.add(staticModel)

        staticModel =
            Item("Settings", R.drawable.icn_settings_my_account_3x)
        itemList.add(staticModel)
        staticModel =
            Item("Logout", R.drawable.icn_logout_my_account_3x)
        itemList.add(staticModel)

        navigationDrawerAdapter = NavigationDrawerAdapter(
            context,
            itemList,
            this
        )
        val mLayoutManagerFeaturedAllCity =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_menuItems!!.layoutManager = mLayoutManagerFeaturedAllCity
        rv_menuItems!!.itemAnimator = DefaultItemAnimator()
        rv_menuItems!!.adapter = navigationDrawerAdapter
        rv_menuItems!!.setBackgroundColor(
            Color.parseColor(
                PreferenceManager.getslideMenuBackGroundColor(
                    this
                )
            )
        )

    }

    override fun onResume() {
        super.onResume()
        backCounter = 0
        setImage()


    }

    override fun navigationItemClick(position: Int) {
        if (position == 0) {

            if (drawer_layout!!.isDrawerOpen(GravityCompat.START)) {
                drawer_layout!!.closeDrawer(GravityCompat.START)
            }
            startActivity(Intent(this, MyAccountActivity::class.java))
        } else if (position == 1) {
            if (drawer_layout!!.isDrawerOpen(GravityCompat.START)) {
                drawer_layout!!.closeDrawer(GravityCompat.START)
            }
            loadFragment(HelpFragment())
            bottom_navigation.selectedItemId = R.id.bottom_help

        } else if (position == 2) {

            if (drawer_layout!!.isDrawerOpen(GravityCompat.START)) {
                drawer_layout!!.closeDrawer(GravityCompat.START)
            }
            startActivity(Intent(this, PaymentCardListActivity::class.java))

        } else if (position == 3) {

            if (drawer_layout!!.isDrawerOpen(GravityCompat.START)) {
                drawer_layout!!.closeDrawer(GravityCompat.START)
            }
            startActivity(Intent(this, SettingsActivity::class.java))
        } else if (position == 4) {
//            PreferenceManager.removePrefSingle(this, VariableBag.isLogin)
//            PreferenceManager.removePrefSingle(this, VariableBag._id)
            PreferenceManager.clear(context)
            val mainIntent =
                Intent(this@MainActivity, LoginActivity::class.java)
            mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(mainIntent)
        }

    }

    private fun loadFragment(fragment: Fragment?) {
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit()

        }
    }

    @SuppressLint("ResourceType")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        var fragment: Fragment? = null
        var f: Fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!
        var notThatfreg = false

        when (item.itemId) {
            R.id.bottom_menu -> {

                if (f !is HomeFragment) {
                    notThatfreg = false
                    fragment = HomeFragment()
                    vendorList()
                } else {
                    notThatfreg = true
                }
            }
            R.id.bottom_itemList -> {

                if (f !is ItemListFragment) {

                    notThatfreg = false
                    fragment = ItemListFragment()
                } else {
                    notThatfreg = true
                }
            }

            R.id.bottom_orders -> {

                if (f !is OrdersFragment) {
//                    active =  OrdersFragment()
                    notThatfreg = false
                    fragment = OrdersFragment()
                } else {
                    notThatfreg = true
                }
            }

            R.id.bottom_cart -> {

                if (f !is CartFragment) {
//                    active =  CartFragment()
                    notThatfreg = false
                    fragment = CartFragment()
                } else {
                    notThatfreg = true
                }
            }

            R.id.bottom_help -> {

                if (f !is HelpFragment) {
//                    active =  HelpFragment()
                    notThatfreg = false
                    fragment = HelpFragment()
                } else {
                    notThatfreg = true
                }
            }
        }
        if (!notThatfreg)
            loadFragment(fragment)
        return true
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        var f: Fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!
        if (f !is HomeFragment) {
            backCounter = 0
            bottom_navigation.selectedItemId = R.id.bottom_menu
            loadFragment(HomeFragment())
            behavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            if (backCounter == 0) {
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_LONG).show()
                backCounter++
                Handler().postDelayed(Runnable { backCounter = 0 }, 1000)
            } else if (backCounter == 1) {
                finishAffinity()

            }
        }
    }


    fun vendorList() {

        val myJason = JSONObject()
        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("userId", PreferenceManager.getId(this))
        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject = jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.getAssociatedVendorList(gsonObject)
        call.enqueue(object : Callback<VendorListResponse> {
            @SuppressLint("ShowToast")
            override fun onResponse(
                call: Call<VendorListResponse>,
                response: Response<VendorListResponse>
            ) {

                if (response.isSuccessful)
                    if (response.body()?.responseCode == 200) {

                        if (response.body()?.result?.totalVendors == 1) {

                            btn_openBottonSheet.visibility = View.GONE

                            PreferenceManager.setVendorId(
                                this@MainActivity,
                                response.body()!!.result.vendorList[0].vendorId
                            )

                            PreferenceManager.setvendorName(
                                this@MainActivity,
                                response.body()!!.result.vendorList[0].appName
                            )
                            PreferenceManager.setvendorImage(
                                this@MainActivity,
                                response.body()!!.result.vendorList[0].logo
                            )


                            if (!response.body()!!.result.vendorList[0].sck.isNullOrBlank()) {
                                PreferenceManager.setSck(
                                    this@MainActivity,
                                    response.body()!!.result.vendorList[0].sck
                                )
                            } else {

                                callCcsApi(
                                    PreferenceManager.getname(this@MainActivity),
                                    PreferenceManager.getEmail(this@MainActivity),
                                    PreferenceManager.getmobileNumber(this@MainActivity),
                                    PreferenceManager.getVendorId(this@MainActivity)
                                )
                            }

                            PreferenceManager.setVendorPhoneNumber(
                                this@MainActivity,
                                response.body()!!.result.vendorList[0].phone
                            )
                            PreferenceManager.setPlsk(
                                this@MainActivity,
                                response.body()!!.result.vendorList[0].plpk
                            )
                            PreferenceManager.setDefaultPaymentType(
                                this@MainActivity,
                                response.body()!!.result.vendorList[0].defaultPaymentType
                            )
                            PreferenceManager.setDefaultTypeBank(
                                this@MainActivity,
                                response.body()!!.result.vendorList[0].defaultTypeBank
                            )
                            PreferenceManager.setPaymentTypeName(
                                this@MainActivity,
                                response.body()!!.result.vendorList[0].paymentTypeName
                            )
                            PreferenceManager.setPCK(
                                this@MainActivity,
                                response.body()!!.result.vendorList[0].pck
                            )
                            PreferenceManager.setvandorAdd(
                                this@MainActivity,
                                response.body()!!.result.vendorList[0].add
                            )


//                            plpks = response.body()!!.result.vendorList[0].plpk


                            Picasso.get().load(
                                "http://207.244.244.64/LogoImage/" + PreferenceManager.getvendorImage(
                                    this@MainActivity
                                )
                            ).into(img_vendor)

                            txt_vendorName.text = response.body()!!.result.vendorList[0].appName

                            if (!fromOrderDetail) {

                                loadFragment(HomeFragment())
                            } else {
                                fromOrderDetail = false
                            }


                        } else {

                            btn_openBottonSheet.visibility = View.VISIBLE
                            vendorList = response.body()!!.result.vendorList
                            recyclerView = findViewById(R.id.recyclerView)
                            recyclerView?.layoutManager =
                                LinearLayoutManager(
                                    this@MainActivity,
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                            recyclerView?.setHasFixedSize(true)
                            mAdapter =
                                MediaItemAdapter(this@MainActivity,
                                    vendorList,
                                    this@MainActivity
                                )
                            recyclerView?.adapter = mAdapter


                            if (!PreferenceManager.getVendorId(this@MainActivity).equals("")) {
                                behavior?.state = BottomSheetBehavior.STATE_COLLAPSED
                                Picasso.get()
                                    .load(
                                        "http://207.244.244.64/LogoImage/" + PreferenceManager.getvendorImage(
                                            this@MainActivity
                                        )
                                    )
                                    .into(img_vendor)

                                txt_vendorName.text = PreferenceManager.getvendorName(this@MainActivity)

                            } else {

                                PreferenceManager.setVendorId(
                                    this@MainActivity,
                                    response.body()!!.result.vendorList[0].vendorId
                                )

                                PreferenceManager.setvendorName(
                                    this@MainActivity,
                                    response.body()!!.result.vendorList[0].appName
                                )
                                PreferenceManager.setvendorImage(
                                    this@MainActivity,
                                    response.body()!!.result.vendorList[0].logo
                                )

                                if (!response.body()!!.result.vendorList[0].sck.isNullOrBlank()) {
                                    PreferenceManager.setSck(
                                        this@MainActivity,
                                        response.body()!!.result.vendorList[0].sck
                                    )
                                } else {
                                    callCcsApi(
                                        PreferenceManager.getname(this@MainActivity),
                                        PreferenceManager.getEmail(this@MainActivity),
                                        PreferenceManager.getmobileNumber(this@MainActivity),
                                        PreferenceManager.getVendorId(this@MainActivity)
                                    )
                                }

                                PreferenceManager.setvandorAdd(
                                    this@MainActivity,
                                    response.body()!!.result.vendorList[0].add
                                )
                                PreferenceManager.setVendorPhoneNumber(
                                    this@MainActivity,
                                    response.body()!!.result.vendorList[0].phone
                                )
                                PreferenceManager.setPlsk(
                                    this@MainActivity,
                                    response.body()!!.result.vendorList[0].plpk
                                )


                                PreferenceManager.setDefaultPaymentType(
                                    this@MainActivity,
                                    response.body()!!.result.vendorList[0].defaultPaymentType
                                )
                                PreferenceManager.setDefaultTypeBank(
                                    this@MainActivity,
                                    response.body()!!.result.vendorList[0].defaultTypeBank
                                )
                                response.body()?.result?.vendorList?.get(0)?.paymentTypeName?.let {
                                    PreferenceManager.setPaymentTypeName(
                                        this@MainActivity,
                                        it
                                    )
                                }

                                response.body()?.result?.vendorList?.get(0)?.pck?.let {
                                    PreferenceManager.setPCK(
                                        this@MainActivity,
                                        it
                                    )
                                }


                                Picasso.get().load(
                                    "http://207.244.244.64/LogoImage/" + PreferenceManager.getvendorImage(
                                        this@MainActivity
                                    )
                                ).into(img_vendor)

                                txt_vendorName.text = response.body()!!.result.vendorList[0].appName

                                behavior?.state = BottomSheetBehavior.STATE_EXPANDED
                            }
                            if (!fromOrderDetail) {

                                loadFragment(HomeFragment())
                            } else {
                                fromOrderDetail = false
                            }
                        }

                    } else {


                    }
            }

            override fun onFailure(call: Call<VendorListResponse>, t: Throwable) {


                Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_SHORT).show()

            }
        })
    }

    override fun onItemClick(
        vendorId: String?,
        vendorName: String,
        vendorImage: String,
        pck: List<PCK>,
        sck: String,
        plpf: String,
        plpk: String,
        Phone: String,
        defaultPaymentType: String,
        defaultTypeBank: PCK,
        paymentTypeName: String,
        add:String
    ) {

        PreferenceManager.setvendorName(this@MainActivity, vendorName)
        PreferenceManager.setvendorImage(this@MainActivity, vendorImage)
        PreferenceManager.setVendorId(this, vendorId)
        PreferenceManager.setVendorPhoneNumber(this@MainActivity, Phone)
        PreferenceManager.setDefaultPaymentType(
            this@MainActivity,
            defaultPaymentType
        )
        PreferenceManager.setDefaultTypeBank(
            this@MainActivity,
            defaultTypeBank
        )
        PreferenceManager.setPaymentTypeName(
            this@MainActivity,
            paymentTypeName
        )
        PreferenceManager.setPCK(
            this@MainActivity,
            pck
        )
        PreferenceManager.setvandorAdd(
            this@MainActivity,
            add
        )

        if (!sck.isNullOrBlank()) {
            PreferenceManager.setSck(this@MainActivity, sck)
        } else {

            callCcsApi(
                PreferenceManager.getname(this@MainActivity),
                PreferenceManager.getEmail(this@MainActivity),
                PreferenceManager.getmobileNumber(this@MainActivity),
                PreferenceManager.getVendorId(this@MainActivity)
            )
        }


        Picasso.get()
            .load("http://207.244.244.64/LogoImage/" + PreferenceManager.getvendorImage(this))
            .into(img_vendor)

        txt_vendorName.text = vendorName

        loadFragment(HomeFragment())
        behavior!!.state = BottomSheetBehavior.STATE_COLLAPSED


    }


    fun openMenuScreen() {
        bottom_navigation.selectedItemId = R.id.bottom_menu
        loadFragment(HomeFragment())

    }

    fun setImage() {
        var header: View = nav_view.getHeaderView(0)
        var iv = header.findViewById<ImageView>(R.id.profileImage)
        if (!PreferenceManager.getuserImage(this).equals("")) {
            Picasso.get()
                .load("http://207.244.244.64/UserImages/" + PreferenceManager.getuserImage(this))
                .into(iv)
        }
    }


    fun callCcsApi(username: String, email: String, mobile: String, vendorId: String) {

        val myJason = JSONObject()
        myJason.put("VRS", vendorId)
        myJason.put("CRS", username)
        myJason.put("CRSM", email)
        myJason.put("CRSP", mobile)

        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject = jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClientForSGP.client.create(ApiInterface::class.java)
        val call = apiService.createUserStripe(gsonObject)
        call.enqueue(object : Callback<CreateUserStripeResponse> {
            override fun onFailure(call: Call<CreateUserStripeResponse>, t: Throwable) {


            }

            override fun onResponse(
                call: Call<CreateUserStripeResponse>,
                response: Response<CreateUserStripeResponse>
            ) {
                if (response.isSuccessful) {
                    PreferenceManager.setSck(this@MainActivity, response.body()?.result?.SCK)
                    callUpdateCustomerKeysApi(PreferenceManager.getVendorId(this@MainActivity))
                } else {
                    Toast.makeText(this@MainActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }
        })

    }


    fun callUpdateCustomerKeysApi(vendorId: String) {

//        Params: {
//            "appId": "", (Required)
//            "userId": "", (Required)
//            "vendorId": "", (Required)
//            "pck": "", (Required)
//            "sck": "" (Required)
//        }
        val myJason = JSONObject()
        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("userId", PreferenceManager.getId(this))
        myJason.put("vendorId", vendorId)
        myJason.put("pck", "")
        myJason.put("sck", PreferenceManager.getSck(this))

        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject = jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.updateCustomerKeys(gsonObject)
        call.enqueue(object : Callback<UpdateCustomerKeyResponse> {
            override fun onFailure(call: Call<UpdateCustomerKeyResponse>, t: Throwable) {


            }

            override fun onResponse(
                call: Call<UpdateCustomerKeyResponse>,
                response: Response<UpdateCustomerKeyResponse>
            ) {
                if (response.isSuccessful) {

                } else {
//                    Toast.makeText(this@MainActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }
        })

    }
}
