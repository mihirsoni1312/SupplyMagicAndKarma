package com.example.karma.responseModel.productByProductId

import com.google.gson.annotations.SerializedName

data class PriceInfoModelCart(

    @SerializedName("o")
    var old: String,

    @SerializedName("n")
    var new: String,

    @SerializedName("c")
    var c: String,

    @SerializedName("dp")
    var dp: String,

    @SerializedName("da")
    var da: String

)