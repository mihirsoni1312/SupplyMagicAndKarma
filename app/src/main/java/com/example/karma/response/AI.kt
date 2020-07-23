package com.example.karma.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AI {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("image")
    @Expose
    var image: String? = null

}