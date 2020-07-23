package com.example.karma.response

import com.google.gson.annotations.SerializedName

data class CommonSimpleResponse (

    @SerializedName("responseCode")
    var responseCode: Int,

    @SerializedName("message")
    var message: String,

    @SerializedName("result")
    var result: addtocartresult,

    @SerializedName("status")
    var status: String

)