package com.example.karma.response

import com.google.gson.annotations.SerializedName

data class Helplist(

    @SerializedName("responseCode")
    var responseCode: Int,

    @SerializedName("message")
    var message: String,

    @SerializedName("status")
    var status: String

)