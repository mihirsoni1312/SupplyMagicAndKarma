package com.example.karma.response

import com.example.karma.responseModel.ConfigAppModel
import com.google.gson.annotations.SerializedName

data class ConfigAppIdResponse(

    @SerializedName("status")
    var status: String,

    @SerializedName("responseCode")
    var responseCode: Int,

    @SerializedName("result")
    var result: ConfigAppModel

)