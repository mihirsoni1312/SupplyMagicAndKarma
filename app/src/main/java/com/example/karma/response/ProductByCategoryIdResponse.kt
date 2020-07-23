package com.example.karma.response

import com.example.karma.responseModel.ProductByCategoryModel
import com.google.gson.annotations.SerializedName

data class ProductByCategoryIdResponse(

    @SerializedName("status")
    var status: String,

    @SerializedName("responseCode")
    var responseCode: Int,

    @SerializedName("result")
    var result: ProductByCategoryModel

)