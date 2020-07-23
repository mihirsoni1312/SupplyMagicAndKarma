package com.example.karma.responseModel.orderDetailResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PI {
    @SerializedName("o")
    @Expose
    var o: String? = null

    @SerializedName("n")
    @Expose
    var n: String? = null

    @SerializedName("c")
    @Expose
    var c: String? = null

    @SerializedName("cP")
    @Expose
    var cP: String? = null

    @SerializedName("tC")
    @Expose
    var tC: TC? = null

}