package com.example.karma.response

import com.example.karma.responseModel.ResultResponseModel
import com.google.gson.annotations.SerializedName

data class HomeScreenResponse(

    @SerializedName("responseCode")
    var responseCode: Int,

    @SerializedName("message")
    var message: String,

    @SerializedName("status")
    var status: String,

    @SerializedName("result")
    var result: ResultResponseModel

)