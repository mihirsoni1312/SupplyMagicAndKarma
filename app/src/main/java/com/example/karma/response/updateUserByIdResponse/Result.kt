package com.example.karma.response.updateUserByIdResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Result {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("appId")
    @Expose
    var appId: String? = null

    @SerializedName("mobileNumber")
    @Expose
    var mobileNumber: String? = null

    @SerializedName("profile")
    @Expose
    var profile: Profile? = null

}