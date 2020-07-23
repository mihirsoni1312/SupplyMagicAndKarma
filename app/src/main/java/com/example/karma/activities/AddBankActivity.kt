package com.example.karma.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.karma.R
import com.example.karma.responseModel.getPlaidToken.GetPlaidToken
import com.example.karma.retrofit.ApiClient
import com.example.karma.retrofit.ApiInterface
import com.example.karma.utils.AppIDConfig
import com.example.karma.utils.CustomProgressbar
import com.example.karma.utils.PreferenceManager
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.plaid.link.Plaid
import com.plaid.link.configuration.AccountSubtype
import com.plaid.link.configuration.PlaidEnvironment
import com.plaid.link.configuration.PlaidProduct
import com.plaid.link.linkConfiguration
import com.plaid.link.openPlaidLink
import com.plaid.link.result.LinkExit
import com.plaid.link.result.PlaidLinkResultHandler
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.util.*
import kotlin.reflect.KParameter

class AddBankActivity : AppCompatActivity() {

    var progressDialog: Dialog? = null

    var plpt = ""
    var acid = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bank)
        Plaid.initialize(application)
        openLink()
    }

    private val resultHandler = PlaidLinkResultHandler(
        onSuccess = { it ->

            plpt = it.publicToken
            acid = it.metadata.accounts.first().accountId

            apiCall()
        },
        onExit = { it: LinkExit ->

            Toast.makeText(this@AddBankActivity, "Please try again later", Toast.LENGTH_SHORT).show()

            finish()
        }
    )

    private fun openLink() {
        this@AddBankActivity.openPlaidLink(
            linkConfiguration = linkConfiguration {
                clientName = "Stripe/Plaid Test"
                products = listOf(PlaidProduct.AUTH)
                publicKey = PreferenceManager.getPlsk(this@AddBankActivity)
//                token = PreferenceManager.getPlsk(this@AddBankActivity)
                environment = PlaidEnvironment.SANDBOX
            }
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultHandler.onActivityResult(requestCode, resultCode, data)) {
            return
        } else {
            Log.i("plaid", "Not handled")
        }

//         if (!myPlaidResultHandler.onActivityResult(requestCode, resultCode, data)) {
//             Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show()
//         }
    }

    fun apiCall() {
        progressDialog = CustomProgressbar().show(this@AddBankActivity)
        val myJason = JSONObject()

        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        myJason.put("vendorId", PreferenceManager.getVendorId(this))
        myJason.put("userId", PreferenceManager.getId(this))
        myJason.put("plpt", plpt)
        myJason.put("acid", acid)
        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.getPlaidToken(gsonObject)
        call.enqueue(object : retrofit2.Callback<GetPlaidToken> {
            override fun onFailure(call: Call<GetPlaidToken>, t: Throwable) {
                progressDialog?.dismiss()
                finish()
            }

            override fun onResponse(
                call: Call<GetPlaidToken>,
                response: Response<GetPlaidToken>
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.responseCode == 200) {
                        progressDialog?.dismiss()
                        Toast.makeText(this@AddBankActivity, "Your bank has been added successfully.", Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
            }
        })

    }

}