package com.example.karma.response

import com.example.karma.responseModel.PaymentCardModel
import com.google.gson.annotations.SerializedName

data class PaymentResponse(

    @SerializedName("responseCode")
    var responseCode: Int,

    @SerializedName("status")
    var status: String,

    @SerializedName("result")
    var result: List<PaymentCardModel>

)