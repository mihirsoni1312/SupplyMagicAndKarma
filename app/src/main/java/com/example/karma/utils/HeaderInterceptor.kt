package com.example.karma.utils

import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()/*eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbiI6IjVmMTFlZWIxNTA2YmJkMTdlMDRkOGIwZSJ9.uiUD10CbalN5_bpABWsy7HqFhiMSqgBnRlmYt5WM-9A*/
                .addHeader("Authorization", "Bearer " + PreferenceManager.getsTokan().toString())

                .build()
        )
    }
}