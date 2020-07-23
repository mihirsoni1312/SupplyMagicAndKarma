package com.example.karma.response.deliverySlotResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DeliverySlotResponse {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("responseCode")
    @Expose
    var responseCode: Int? = null

    @SerializedName("result")
    @Expose
    var result: List<Result>? =
        null

}