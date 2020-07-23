package com.example.karma.responseModel

import com.google.gson.annotations.SerializedName

data class ProductByCategoryModel(

    @SerializedName("totalProducts")
    var totalProducts: Int,

    @SerializedName("categoryId")
    var categoryId: String,

    @SerializedName("productCount")
    var productCount: Int,
    @SerializedName("totalPages")
    var totalPages: Int,

    @SerializedName("hasMoreProducts")
    var hasMoreProducts: Boolean,

    @SerializedName("productList")
    var productList: ArrayList<ProductListModel>

)