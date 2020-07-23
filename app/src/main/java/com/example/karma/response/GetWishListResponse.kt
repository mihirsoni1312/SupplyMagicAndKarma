package com.example.karma.response

import com.example.karma.responseModel.CartResponseModel
import com.example.karma.responseModel.WishListModel
import com.google.gson.annotations.SerializedName

data class GetWishListResponse (

    @SerializedName("responseCode")
    var responseCode: Int,

    @SerializedName("message")
    var message: String,


    @SerializedName("result")
    var result: WishListModel

)