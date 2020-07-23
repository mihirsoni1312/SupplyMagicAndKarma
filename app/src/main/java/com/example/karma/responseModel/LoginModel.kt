package com.example.karma.responseModel

import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("sToken")
    var sToken: String,

    @SerializedName("_id")
    var _id: String,

    @SerializedName("email")
    var email: String,

    @SerializedName("appId")
    var appId: String,

    @SerializedName("mobileNumber")
    var mobileNumber: String,



    @SerializedName("profile")
    var profile: LoginProfileModel

)