package com.example.karma.response.deliverySlotResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Result {
    @SerializedName("DateDisplay")
    @Expose
    var dateDisplay: String? = null

    @SerializedName("OrderDate")
    @Expose
    var orderDate: String? = null

    @SerializedName("Type")
    @Expose
    var type: String? = null

    @SerializedName("CheckOutReplaceEndTimeDisplay")
    @Expose
    var checkOutReplaceEndTimeDisplay: String? = null

    @SerializedName("CheckOutReplaceEndTime")
    @Expose
    var checkOutReplaceEndTime: String? = null

    @SerializedName("StartTime")
    @Expose
    var startTime: String? = null

    @SerializedName("EndTime")
    @Expose
    var endTime: String? = null

    @SerializedName("DeliveryFee")
    @Expose
    var deliveryFee: String? = null

}