package com.example.karma.responseModel.updateDefaultPaymentType

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Result {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("appId")
    @Expose
    var appId: String? = null

}