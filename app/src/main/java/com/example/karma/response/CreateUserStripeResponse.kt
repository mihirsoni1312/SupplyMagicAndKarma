package com.example.karma.response

import com.example.karma.responseModel.CreteUserStripeModel
import com.google.gson.annotations.SerializedName

data class CreateUserStripeResponse(

    @SerializedName("status")
    var status: String,

    @SerializedName("responseCode")
    var responseCode: Int,

    @SerializedName("result")
    var result: CreteUserStripeModel

)