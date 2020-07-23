package com.example.karma.response

import com.google.gson.annotations.SerializedName

data class ChangePasswordResponse (

    @SerializedName("responseCode")
    var responseCode: Int,

    @SerializedName("message")
    var message: String,


    @SerializedName("result")
    var result: String


)