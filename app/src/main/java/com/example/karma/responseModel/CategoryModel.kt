package com.example.karma.responseModel

import com.google.gson.annotations.SerializedName

data class CategoryModel(

    @SerializedName("catId")
    var categoryId: String,
    @SerializedName("appId")
    var appId: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("displayOrder")
    var displayOrder: String,
    @SerializedName("desc")
    var desc: String,
    @SerializedName("image")
    var image: String


)