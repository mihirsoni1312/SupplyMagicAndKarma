package com.example.karma.responseModel.getPlaidToken

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Result {
    @SerializedName("customerId")
    @Expose
    var customerId: String? = null

}