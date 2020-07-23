package com.example.karma.response

import com.google.gson.annotations.SerializedName

data class Promocoderesponse(

    @SerializedName("status")
    var status: String,

    @SerializedName("responseCode")
    var responseCode: Int,

    @SerializedName("result")
    var result: promoresult,

    @SerializedName("message")
    var message: String

)