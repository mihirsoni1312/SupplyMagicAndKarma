package com.example.karma.response

import com.google.gson.annotations.SerializedName

data class CommonResponse (

    @SerializedName("responseCode")
    var responseCode: Int,

    @SerializedName("message")
    var message: String,

    @SerializedName("status")
    var status: String,

    @SerializedName("result")
    var result: String,

    @SerializedName("errorType")
    var errorType: String

)