package com.example.karma.responseModel.orderDetailResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PromoCode {
    @SerializedName("promoCode")
    @Expose
    var promoCode: String? = null

    @SerializedName("discountRate")
    @Expose
    var discountRate: String? = null

    @SerializedName("discountAmount")
    @Expose
    var discountAmount: String? = null

    @SerializedName("amountBeforeDiscount")
    @Expose
    var amountBeforeDiscount: String? = null

    @SerializedName("amountAfterDiscount")
    @Expose
    var amountAfterDiscount: String? = null

}