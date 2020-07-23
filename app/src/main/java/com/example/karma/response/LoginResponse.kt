package com.example.karma.response

import com.example.karma.responseModel.LoginModel
import com.google.gson.annotations.SerializedName

data class LoginResponse (

    @SerializedName("status")
    var status: String,

    @SerializedName("message")
    var message: String,

    @SerializedName("responseCode")
    var responseCode: Int,
    @SerializedName("result")
    var result: LoginModel

)