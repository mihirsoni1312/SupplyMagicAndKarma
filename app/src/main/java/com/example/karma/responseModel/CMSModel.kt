package com.example.karma.responseModel

import com.google.gson.annotations.SerializedName

data class CMSModel (
    @SerializedName("_id")
    var _id: String,
    @SerializedName("appId")
    var appId: String,
    @SerializedName("vendorId")
    var vendorId: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("type")
    var type: String,
    @SerializedName("text")
    var text: String
)