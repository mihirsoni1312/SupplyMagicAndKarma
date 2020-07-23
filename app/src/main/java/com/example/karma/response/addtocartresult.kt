package com.example.karma.response

import com.google.gson.annotations.SerializedName

data class addtocartresult(

    @SerializedName("cartId")
    var cartId: String,

    @SerializedName("appId")
    var appId: String,

    @SerializedName("vendorId")
    var vendorId: String
)
