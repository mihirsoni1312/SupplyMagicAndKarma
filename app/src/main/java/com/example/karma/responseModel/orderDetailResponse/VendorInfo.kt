package com.example.karma.responseModel.orderDetailResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VendorInfo {
    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("mobileNumber")
    @Expose
    var mobileNumber: String? = null

    @SerializedName("zipCode")
    @Expose
    var zipCode: String? = null

}