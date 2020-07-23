package com.example.karma.response

import com.example.karma.responseModel.GetAddressResultResponseModel
import com.google.gson.annotations.SerializedName

data class GetAddressListResponse(
    @SerializedName("status")
    var status: String,

    @SerializedName("responseCode")
    var responseCode: Int,


    @SerializedName("result")
    var result: GetAddressResultResponseModel
)