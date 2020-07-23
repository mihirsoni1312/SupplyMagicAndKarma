package com.example.karma.response

import com.example.karma.responseModel.productByProductId.ProductByProductMainModel
import com.google.gson.annotations.SerializedName

data class ProductByProductIdResponse(

    @SerializedName("status")
    var status: String,

    @SerializedName("responseCode")
    var responseCode: Int,

    @SerializedName("result")
    var result: ProductByProductMainModel

)