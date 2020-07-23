package com.example.karma.responseModel.orderDetailResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OrderTimeSlot {
    @SerializedName("deliveryDate")
    @Expose
    var deliveryDate: String? = null

    @SerializedName("from")
    @Expose
    var from: String? = null

    @SerializedName("to")
    @Expose
    var to: String? = null

}