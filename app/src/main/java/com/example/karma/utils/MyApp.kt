package com.example.karma.utils

import android.app.Application
import android.content.Context
import com.plaid.link.Plaid

class MyApp : Application() {

    companion object {

      lateinit var  appcontext:Context

        fun getApplicationContext():Context{
            return appcontext
        }
    }

    override fun onCreate() {
        super.onCreate()
        appcontext = applicationContext

        Plaid.initialize(this)
    }
}