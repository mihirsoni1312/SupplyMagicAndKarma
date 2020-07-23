package com.example.karma.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.karma.R
import com.example.karma.retrofit.AppConfig.BASE_URL_SE
import com.example.karma.utils.CustomProgressbar
import com.example.karma.utils.PreferenceManager
import kotlinx.android.synthetic.main.activity_new_card_web_view.*


class NewCardWebViewActivity : AppCompatActivity() {
    var progressDialog: Dialog? = null
    var sck = ""
    var vrs = ""
    var url =
        "${BASE_URL_SE}CSC?VRS=5ee11c4bc2c65605081f2ff9&SCK=WOpre\$HWI06v7nJ6M4yt4S"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_card_web_view)
        title_address.setTextColor(Color.parseColor(PreferenceManager.getFontColor(this)))

        url = url.replace("5ee11c4bc2c65605081f2ff9", PreferenceManager.getVendorId(this))
        url = url.replace("WOpre\$HWI06v7nJ6M4yt4S", PreferenceManager.getSck(this))


        webview.settings.javaScriptEnabled = true
        webview.loadUrl(url)
        progressDialog = CustomProgressbar().show(this@NewCardWebViewActivity)

        Handler().postDelayed({ /* Create an Intent that will start the Menu-Activity. */
            progressDialog?.dismiss()
        }, 3000)

        webview.setWebViewClient(object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {

            }
            override fun onPageFinished(view: WebView, url: String) {
                progressDialog?.dismiss()
            }
        })


        webview.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url?.startsWith("intent://?id=success")!!) {
                    Toast.makeText(
                        this@NewCardWebViewActivity,
                        "Your card has been added successfully.",
                        Toast.LENGTH_SHORT
                    ).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                    return true
                } else {
                    //                    webview.stopLoading();
                    //                    webview.goBack();
//                    Toast.makeText(
//                        this@NewCardWebViewActivity,
//                        "Problem during save card details. Please try again",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
                return false

            }

        }

        btn_back_webview.setOnClickListener {
            onBackPressed()
        }

    }
}