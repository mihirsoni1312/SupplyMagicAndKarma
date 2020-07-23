package com.example.karma.responseModel.orderDetailResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TC {
    @SerializedName("catId")
    @Expose
    var catId: String? = null

    @SerializedName("percentage")
    @Expose
    var percentage: String? = null

}