package com.example.karma.responseModel

import com.google.gson.annotations.SerializedName

data class ProductListSearchModel (

    @SerializedName("_id")
    var _id: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("img")
    var img: String,

    @SerializedName("priceInfo")
    var priceInfo: PriceInfoModel?

)