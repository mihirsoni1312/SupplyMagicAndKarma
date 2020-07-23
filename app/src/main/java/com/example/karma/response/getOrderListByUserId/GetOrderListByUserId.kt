package com.example.karma.response.getOrderListByUserId

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetOrderListByUserId {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("responseCode")
    @Expose
    var responseCode: Int? = null

    @SerializedName("result")
    @Expose
    var result: Result? = null

}