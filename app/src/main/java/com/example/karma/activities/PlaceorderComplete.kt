package com.example.karma.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.karma.R
import com.example.karma.utils.PreferenceManager
import kotlinx.android.synthetic.main.activity_placeorder_complete.*
import kotlinx.android.synthetic.main.fragment_cart.view.*

class PlaceorderComplete : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placeorder_complete)

        title_productList.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))
        tvdeliveryon.text = " ${intent.getStringExtra("deliveryon")} on"
        tv_date.text = intent.getStringExtra("date")
        tv_time.text = intent.getStringExtra("time")
        tv_total.text = intent.getStringExtra("total")
        tv_deliverydate.text = intent.getStringExtra("dateandtime")

        if (PreferenceManager.getIsRestaurentApp(this@PlaceorderComplete)) {
            imgeview.setImageResource(R.drawable.imgpsh)
            btn_close.text = "ORDER NOW"
            ll_bottom.visibility = View.GONE
        }

        tv_date.setTextColor(Color.parseColor(PreferenceManager.getBackgroundColor(this)))
        tv_time.setTextColor(Color.parseColor(PreferenceManager.getBackgroundColor(this)))

        btn_close.setBackgroundColor(Color.parseColor(PreferenceManager.getBackgroundColor(this)))
        btn_close.setTextColor(Color.parseColor(PreferenceManager.getButtonFontColor(this)))

        btn_close.setOnClickListener {
            val mainIntent = Intent(this@PlaceorderComplete, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }
    }

    override fun onBackPressed() {
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(mainIntent)
    }


}
