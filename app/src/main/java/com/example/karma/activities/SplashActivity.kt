package com.example.karma.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.*
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.downloader.Error
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
import kotlinx.android.synthetic.main.activity_splash.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.concurrent.thread


class SplashActivity : BaseActivity() {

    private val progressBar: ProgressBar? = null
    private var progressStatus = 0
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)

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

                 /*   PreferenceManager.setId(
                        this@SplashActivity,
                        response.body()!!.result._id
                    )*/
                    PreferenceManager.setAppName(
                        this@SplashActivity,
                        response.body()!!.result.appName
                    )
                    PreferenceManager.setAppId(
                        this@SplashActivity,
                        response.body()!!.result.appId
                    )
                    PreferenceManager.setBackgroundColor(
                        this@SplashActivity,
                        response.body()!!.result.backgroundColor
                    )
                    PreferenceManager.setFontColor(
                        this@SplashActivity,
                        response.body()!!.result.fontColor
                    )
                    PreferenceManager.setslideMenuBackGroundColor(
                        this@SplashActivity,
                        response.body()!!.result.slideMenuBackGroundColor
                    )
                    PreferenceManager.setslideMenuFontColor(
                        this@SplashActivity,
                        response.body()!!.result.slideMenuFontColor
                    )
                    PreferenceManager.setslideMenuIconColor(
                        this@SplashActivity,
                        response.body()!!.result.slideMenuIconColor
                    )
                    PreferenceManager.setIsRestaurentApp(
                        this@SplashActivity,
                        response.body()!!.result.isRestaurentApp
                    )

                    PreferenceManager.setButtonFontColor(
                        this@SplashActivity,
                        response.body()!!.result.buttonFontColor
                    )
                    PreferenceManager.setLoginScreeLogo(
                        this@SplashActivity,
                        response.body()!!.result.loginScreeLogo
                    )
                    PreferenceManager.setRegistrationScreenLogo(
                        this@SplashActivity,
                        response.body()!!.result.registrationScreenLogo
                    )
                    PreferenceManager.setTempVendorId(
                        this@SplashActivity,
                        response.body()!!.result.defaultVendorId
                    )


                    if (PreferenceManager.getisLogin(this@SplashActivity)) {
                        val mainIntent =
                            Intent(
                                this@SplashActivity,
                                MainActivity::class.java
                            )
                        startActivity(mainIntent)
                        finish()
                    } else {
                        val mainIntent =
                            Intent(
                                this@SplashActivity,
                                LoginActivity::class.java
                            )
                        startActivity(mainIntent)
                        finish()
                    }

                } else {

                    Toast.makeText(this@SplashActivity, "Fail", Toast.LENGTH_SHORT)
                        .show()

                }
            }

            override fun onFailure(call: Call<ConfigAppIdResponse>, t: Throwable) {

            }
        })
    }

    private fun saveToInternalStorage(bitmapImage: Bitmap): String? {
        val cw = ContextWrapper(applicationContext)
        val directory: File = cw.getDir("imageDir", Context.MODE_PRIVATE)
        // Create imageDir
        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder()
            .permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val mypath = File(directory, "loginbg.jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return directory.absolutePath
    }

    private fun saveToInternalStorageRegister(bitmapImage: Bitmap): String? {
        val cw = ContextWrapper(applicationContext)
        val directory: File = cw.getDir("imageDir", Context.MODE_PRIVATE)
        // Create imageDir
        val mypath = File(directory, "registerbg.jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return directory.absolutePath
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

        val anim = ProgressBarAnimation(progressbarSupplyMagic, 0f, 100f)
        anim.duration = 3500
        progressbarSupplyMagic.startAnimation(anim)

    }
}
