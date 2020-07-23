package com.example.karma.response

import com.google.gson.annotations.SerializedName

data class promoresult(

    @SerializedName("promoCode")
    var promoCode: String,

    @SerializedName("discountRate")
    var discountRate: String,

    @SerializedName("totalDiscount")
    var totalDiscount: String,

    @SerializedName("amountAfterDiscount")
    var amountAfterDiscount: String,

    @SerializedName("currency")
    var currency: String

)