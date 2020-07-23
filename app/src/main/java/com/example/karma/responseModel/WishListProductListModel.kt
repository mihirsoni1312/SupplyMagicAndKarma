package com.example.karma.responseModel

import com.example.karma.response.AI
import com.example.karma.responseModel.productByProductId.PriceInfoModelCart
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WishListProductListModel(

    @SerializedName("_id")
    var _id: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("image")
    var image: String,

    @SerializedName("pI")
    var pI: PriceInfoModelCart,

    @SerializedName("iA")
    var iA: Boolean,

    @SerializedName("iAv")
    var iAv: Boolean,

    @SerializedName("sdesc")
    var sdesc: String,

    @SerializedName("rp")
    var relatedProduct: List<RelatedProduct>?,

    @SerializedName("aI")
    @Expose
    var aI: List<AI>


)