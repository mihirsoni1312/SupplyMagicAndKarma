package com.example.karma.responseModel.productByProductId

import com.google.gson.annotations.SerializedName

data class ProductByProductMainModel(

    @SerializedName("_id")
    var _id: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("qty")
    var qty: String,

    @SerializedName("sdesc")
    var sdesc: String,

    @SerializedName("desc")
    var desc: String,

    @SerializedName("i1")
    var i1: String,
    @SerializedName("i2")
    var i2: String,
    @SerializedName("i3")
    var i3: String,
    @SerializedName("iA")
    var iA: String,

    @SerializedName("iAv")
    var iAv: Boolean,

    @SerializedName("iMP")
    var iMP: Boolean,

    @SerializedName("pI")
    var pI: PIModel,

    @SerializedName("aI")
    var aI: ArrayList<AIModel>,

    @SerializedName("cP")
    var cP: ArrayList<CPModel>,

    @SerializedName("isFavPro")
    var isFavPro: Boolean


)