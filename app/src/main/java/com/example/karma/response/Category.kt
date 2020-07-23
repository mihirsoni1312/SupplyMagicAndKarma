package com.example.karma.response

import com.google.gson.annotations.SerializedName

data class Category(

    @SerializedName("_id")
    var id: String,

    @SerializedName("cName")
    var cName: String,

    @SerializedName("count")
    var count: String

)