package com.example.karma.response

import com.example.karma.responseModel.VendorResultModel
import com.google.gson.annotations.SerializedName

data class VendorListResponse (

    @SerializedName("status")
    var status: String,

    @SerializedName("responseCode")
    var responseCode: Int,

    @SerializedName("result")
    var result: VendorResultModel

)