package com.example.karma.response.getOrderListByUserId

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SystemDate {
    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null

    @SerializedName("modifiedAt")
    @Expose
    var modifiedAt: String? = null

}