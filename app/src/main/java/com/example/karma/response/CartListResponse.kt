package com.example.karma.response

import com.example.karma.responseModel.CartResponseModel
import com.google.gson.annotations.SerializedName

data class CartListResponse(

    @SerializedName("responseCode")
    var responseCode: Int,

    @SerializedName("message")
    var message: String,


    @SerializedName("result")
    var result: CartResponseModel


)