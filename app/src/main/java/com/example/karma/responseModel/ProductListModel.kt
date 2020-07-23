package com.example.karma.responseModel

import com.example.karma.response.AI
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductListModel(

    @SerializedName("_id")
    var _id: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("i1")
    var img: String,

    @SerializedName("pI")
    var priceInfo: PriceInfoModel?,

    @SerializedName("sdesc")
    var sdesc: String,

    @SerializedName("iAv")
    var iAv: Boolean,

    @SerializedName("rp")
    var relatedProduct: List<RelatedProduct>?,

    @SerializedName("aI")
    @Expose
    var aI: List<AI>


)