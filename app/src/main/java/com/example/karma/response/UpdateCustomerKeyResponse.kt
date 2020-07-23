package com.example.karma.response

import com.example.karma.responseModel.CustomerKeyModel
import com.google.gson.annotations.SerializedName

data class UpdateCustomerKeyResponse(

    @SerializedName("responseCode")
    var responseCode: Int,

    @SerializedName("status")
    var status: String,

    @SerializedName("result")
    var result: CustomerKeyModel

)