package com.example.karma.response

import com.example.karma.responseModel.SearchResultModel
import com.google.gson.annotations.SerializedName

data class SearchResponse(

    @SerializedName("status")
    var status: String,

    @SerializedName("responseCode")
    var responseCode: Int,

    @SerializedName("result")
    var result: SearchResultModel

)