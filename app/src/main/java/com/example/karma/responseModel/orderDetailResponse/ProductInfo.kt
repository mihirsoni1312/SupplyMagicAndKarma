package com.example.karma.responseModel.orderDetailResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductInfo {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("i1")
    @Expose
    var i1: String? = null

    @SerializedName("pI")
    @Expose
    var pI: PI? = null

    @SerializedName("qty")
    @Expose
    var qty: String? = null

    @SerializedName("tot")
    @Expose
    var tot: String? = null

    @SerializedName("iAv")
    @Expose
    var iAv: Boolean? = null

    @SerializedName("tax")
    @Expose
    var tax: String? = null

}