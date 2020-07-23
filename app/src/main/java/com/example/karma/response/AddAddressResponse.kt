package com.example.karma.response

import com.google.gson.annotations.SerializedName

data class AddAddressResponse(

    @SerializedName("status")
    var status: String,

    @SerializedName("responseCode")
    var responseCode: Int,

    @SerializedName("message")
    var message: String
)