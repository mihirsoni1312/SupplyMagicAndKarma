package com.example.karma.response

import com.google.gson.annotations.SerializedName

data class Deletecartresponse (

    @SerializedName("responseCode")
    var responseCode: Int,

    @SerializedName("result")
    var result: String,

    @SerializedName("status")
    var status: String
)