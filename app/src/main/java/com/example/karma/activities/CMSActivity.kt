package com.example.karma.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import com.example.karma.R
import com.example.karma.base.BaseActivity
import com.example.karma.response.CMSResponse
import com.example.karma.retrofit.ApiClient
import com.example.karma.retrofit.ApiInterface
import com.example.karma.utils.AppIDConfig
import com.example.karma.utils.PreferenceManager
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_c_m_s.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CMSActivity : BaseActivity() {

    var type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c_m_s)

        btn_back_webview.setOnClickListener {
            finish()
        }

        title_webview.setTextColor(Color.parseColor(PreferenceManager.getFontColor(context)))
        type = intent.getStringExtra("type")

        if (type.equals("2")) {
            title_webview.text = "Terms & Condtion"
        } else if (type.equals("5")) {
            title_webview.text = "Privacy Policy"
        } else if (type.equals("7")) {
            title_webview.text = "Licence"
        }

        webdataLoad()

    }

    private fun webdataLoad() {
        val myJason = JSONObject()
        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("vendorId", PreferenceManager.getTempVendorId(this))
        myJason.put("type", type)

        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.cmsData(gsonObject)
        call.enqueue(object : Callback<CMSResponse> {
            @SuppressLint("ShowToast")
            override fun onResponse(
                call: Call<CMSResponse>,
                response: Response<CMSResponse>
            ) {

                if (response.body()!!.responseCode == 200) {

                    webview.loadData(response.body()!!.result!!.text, "text/html", "UTF-8")


                }
            }

            override fun onFailure(call: Call<CMSResponse>, t: Throwable) {

            }
        })
    }
}