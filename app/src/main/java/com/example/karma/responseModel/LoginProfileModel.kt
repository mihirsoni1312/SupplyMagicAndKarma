package com.example.karma.responseModel

import com.google.gson.annotations.SerializedName

data class LoginProfileModel(

    @SerializedName("name")
    var name: String,
    @SerializedName("address")
    var address: String,
    @SerializedName("zipCode")
    var zipCode: String,
    @SerializedName("isUserVerified")
    var isVerified: Boolean,
    @SerializedName("userImage")
    var userImage: String

)