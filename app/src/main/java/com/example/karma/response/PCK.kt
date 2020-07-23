package com.example.karma.response

import com.google.gson.annotations.SerializedName

data class PCK(

    @SerializedName("cId")
    var cld: String,
    @SerializedName("bId")
    var bld: String
)