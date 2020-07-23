package com.example.karma.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.example.karma.R
import com.example.karma.base.BaseActivity
import com.example.karma.response.ConfigAppIdResponse
import com.example.karma.retrofit.ApiClient
import com.example.karma.retrofit.ApiInterface
import com.example.karma.utils.AppIDConfig
import com.example.karma.utils.PreferenceManager
import com.example.karma.utils.ProgressBarAnimation
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_karma_splash_screen.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import kotlin.concurrent.thread


class KarmaSplashScreenActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_karma_splash_screen)




        checkAppPermissions()


    }

    private fun config() {
        val myJason = JSONObject()
        myJason.put("appId", AppIDConfig.SupplyMagicAppId)
        val obj: JSONObject = myJason
        val jsonParser = JsonParser()
        val gsonObject =
            jsonParser.parse(obj.toString()) as JsonObject

        val apiService = ApiClient.client.create(ApiInterface::class.java)
        val call = apiService.getAppConfigByAppId(gsonObject)
        call.enqueue(object : Callback<ConfigAppIdResponse> {
            @RequiresApi(Build.VERSION_CODES.N)
            @SuppressLint("ShowToast")
            override fun onResponse(
                call: Call<ConfigAppIdResponse>,
                response: Response<ConfigAppIdResponse>
            ) {
                if (response.body()!!.responseCode == 200) {

                    PreferenceManager.setId(
                        this@KarmaSplashScreenActivity,
                        response.body()!!.result._id
                    )
                    PreferenceManager.setAppName(
                        this@KarmaSplashScreenActivity,
                        response.body()!!.result.appName
                    )
                    PreferenceManager.setAppId(
                        this@KarmaSplashScreenActivity,
                        response.body()!!.result.appId
                    )
                    PreferenceManager.setBackgroundColor(
                        this@KarmaSplashScreenActivity,
                        response.body()!!.result.backgroundColor
                    )
                    PreferenceManager.setFontColor(
                        this@KarmaSplashScreenActivity,
                        response.body()!!.result.fontColor
                    )
                    PreferenceManager.setslideMenuBackGroundColor(
                        this@KarmaSplashScreenActivity,
                        response.body()!!.result.slideMenuBackGroundColor
                    )
                    PreferenceManager.setslideMenuFontColor(
                        this@KarmaSplashScreenActivity,
                        response.body()!!.result.slideMenuFontColor
                    )
                    PreferenceManager.setslideMenuIconColor(
                        this@KarmaSplashScreenActivity,
                        response.body()!!.result.slideMenuIconColor
                    )
/*
                    "slideMenuBackGroundColor": "#000000",
                    "slideMenuFontColor": "#ffffff",
                    "slideMenuIconColor": "#454545",*/
                    PreferenceManager.setButtonFontColor(
                        this@KarmaSplashScreenActivity,
                        response.body()!!.result.buttonFontColor
                    )
                    PreferenceManager.setLoginScreeLogo(
                        this@KarmaSplashScreenActivity,
                        response.body()!!.result.loginScreeLogo
                    )
                    PreferenceManager.setRegistrationScreenLogo(
                        this@KarmaSplashScreenActivity,
                        response.body()!!.result.registrationScreenLogo
                    )
                    thread {

                        val loginPath: File =
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

                        PRDownloader.download(
                            response.body()!!.result.loginScreeLogo,
                            loginPath.absolutePath,
                            "loginbgKarma.jpg"
                        )
                            .build()
                            .setOnStartOrResumeListener {

                            }
                            .setOnPauseListener {

                            }
                            .setOnCancelListener {

                            }
                            .setOnProgressListener {

                            }
                            .start(object : OnDownloadListener {
                                override fun onDownloadComplete() {

                                    PreferenceManager.setLoginAbusulatPath(
                                        this@KarmaSplashScreenActivity,
                                        loginPath.absolutePath
                                    )

                                    if (PreferenceManager.getisLogin(this@KarmaSplashScreenActivity)) {
                                        val mainIntent =
                                            Intent(
                                                this@KarmaSplashScreenActivity,
                                                MainActivity::class.java
                                            )
                                        startActivity(mainIntent)
                                        finish()
                                    } else {
                                        val mainIntent =
                                            Intent(
                                                this@KarmaSplashScreenActivity,
                                                LoginActivity::class.java
                                            )
                                        startActivity(mainIntent)
                                        finish()
                                    }
                                }

                                override fun onError(error: com.downloader.Error?) {
                                    Toast.makeText(
                                        this@KarmaSplashScreenActivity,
                                        error.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            })

                        /**
                         * Login Image Download....
                         */

                        val registerPath: File =
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

                        PRDownloader.download(
                            response.body()!!.result.registrationScreenLogo,
                            registerPath.absolutePath,
                            "registerbgKarma.jpg"
                        )
                            .build()
                            .setOnStartOrResumeListener {

                            }
                            .setOnPauseListener {

                            }
                            .setOnCancelListener {

                            }
                            .setOnProgressListener {


                            }
                            .start(object : OnDownloadListener {
                                override fun onDownloadComplete() {

                                    PreferenceManager.setRegisterAbusulatPath(
                                        this@KarmaSplashScreenActivity,
                                        registerPath.absolutePath
                                    )

                                }

                                override fun onError(error: com.downloader.Error?) {
                                    Toast.makeText(
                                        this@KarmaSplashScreenActivity,
                                        error.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            })

                    }


                } else {

                    Toast.makeText(this@KarmaSplashScreenActivity, "Fail", Toast.LENGTH_SHORT)
                        .show()

                }
            }

            override fun onFailure(call: Call<ConfigAppIdResponse>, t: Throwable) {
                Toast.makeText(
                    this@KarmaSplashScreenActivity,
                    "" + t.message,
                    Toast.LENGTH_SHORT
                )
            }
        })
    }

    fun checkAppPermissions() {
        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            ) !== PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_NETWORK_STATE
            )
                    !== PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
                    !== PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
                    !== PackageManager.PERMISSION_GRANTED)
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.INTERNET
                ) &&
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_NETWORK_STATE
                ) &&
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) &&
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                go_next()
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    1
                )
            }
        } else {
            go_next()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                checkAppPermissions()
            } else {
                checkAppPermissions()
            }
        }
    }

    private fun go_next() {

        config()

        val anim = ProgressBarAnimation(progressbar, 0f, 100f)
        anim.duration = 5000
        progressbar.startAnimation(anim)

    }
}
