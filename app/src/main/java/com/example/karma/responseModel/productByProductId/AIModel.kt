package com.example.karma.responseModel.productByProductId

import com.google.gson.annotations.SerializedName

data class AIModel(

    @SerializedName("_id")
    var _id: String,

    @SerializedName("name")
    var name: String,


    @SerializedName("image")
    var image: String


)