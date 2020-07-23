package com.example.karma.retrofit

import android.app.Application
import android.content.Context
import com.example.karma.utils.HeaderInterceptor

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

        private var retrofit: Retrofit? = null

        val client: Retrofit
            get() {
                if (retrofit == null) {
                    retrofit = Retrofit.Builder()
                        .baseUrl(AppConfig.BASE_URL)
                        .client(
                            OkHttpClient.Builder().addInterceptor(HeaderInterceptor())
                                .writeTimeout(180, TimeUnit.SECONDS)
                                .connectTimeout(30, TimeUnit.MINUTES)
                                .readTimeout(30, TimeUnit.MINUTES).build()
                        )
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()
                }
                return retrofit!!
            }

}