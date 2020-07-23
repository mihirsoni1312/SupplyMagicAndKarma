package com.example.karma.responseModel.productByProductId

import com.google.gson.annotations.SerializedName

data class PIModel(


    @SerializedName("o")
    var o: String,


    @SerializedName("n")
    var n: String,
    @SerializedName("c")
    var c: String,

    @SerializedName("dp")
    var dp: String,

    @SerializedName("da")
    var da: String


)