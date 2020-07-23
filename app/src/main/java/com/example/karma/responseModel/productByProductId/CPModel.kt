package com.example.karma.responseModel.productByProductId

import com.google.gson.annotations.SerializedName

data class CPModel(

    @SerializedName("_id")
    var _id: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("pI")
    var pI: CPPIModel,
    @SerializedName("iA")
    var iA: Boolean,
    @SerializedName("iAv")
    var iAv: Boolean,
    @SerializedName("isFavPro")
    var isFavPro: Boolean,
    @SerializedName("qty")
    var qty: String

)