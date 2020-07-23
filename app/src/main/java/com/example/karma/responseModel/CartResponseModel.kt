package com.example.karma.responseModel

import com.example.karma.responseModel.productByProductId.ProductInfoCartModel
import com.google.gson.annotations.SerializedName

data class CartResponseModel(

    @SerializedName("cartId")
    var cartId: String,

  @SerializedName("orderId")
    var orderId: String,

    @SerializedName("productInfo")
    var productInfoList: ArrayList<ProductInfoCartModel>

)