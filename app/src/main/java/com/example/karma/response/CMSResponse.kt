package com.example.karma.response

import com.example.karma.responseModel.CMSModel
import com.google.gson.annotations.SerializedName

data class CMSResponse(

    @SerializedName("status")
    var status: String,
    @SerializedName("responseCode")
    var responseCode: Int,
    @SerializedName("result")
    var result: CMSModel?

)