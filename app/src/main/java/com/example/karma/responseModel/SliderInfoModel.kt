package com.example.karma.responseModel

import com.google.gson.annotations.SerializedName

data class SliderInfoModel(

    @SerializedName("catId")
    var catId: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("displayOrder")
    var displayOrder: String,
    @SerializedName("image")
    var image: String

)