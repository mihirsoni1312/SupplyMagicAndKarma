package com.example.karma.response.updateUserByIdResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Profile {
    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("userImage")
    @Expose
    var userImage: String? = null

    @SerializedName("isVerified")
    @Expose
    var isVerified: Boolean? = null

}