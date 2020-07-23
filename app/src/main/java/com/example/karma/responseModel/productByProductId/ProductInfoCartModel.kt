package com.example.karma.responseModel.productByProductId

import com.example.karma.response.AI
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductInfoCartModel(

    @SerializedName("_id")
    var _id: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("i1")
    var i1: String,

    @SerializedName("pI")
    var pI: PriceInfoModelCart,

    @SerializedName("iMP")
    var iMP: Boolean,

    @SerializedName("sdesc")
    var sdesc: String,

    @SerializedName("iAv")
    var iAv: Boolean,

    @SerializedName("iA")
    var iA: Boolean,
    @SerializedName("qty")
    var qty: Int,

    @SerializedName("aI")
    @Expose
    var aI: List<AI>,

    @SerializedName("orderId")
    @Expose
    var orderId: String

)