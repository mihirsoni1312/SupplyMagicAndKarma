package com.example.karma.responseModel.getPlaidToken

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetPlaidToken {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("responseCode")
    @Expose
    var responseCode: Int? = null

    @SerializedName("result")
    @Expose
    var result: Result? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

}