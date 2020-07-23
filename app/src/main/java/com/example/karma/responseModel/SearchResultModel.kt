package com.example.karma.responseModel

import com.google.gson.annotations.SerializedName

data class SearchResultModel(


    @SerializedName("productCount")
    var productCount: Int,

    @SerializedName("currentPage")
    var currentPage: Int,

    @SerializedName("totalProducts")
    var totalProducts: Int,

    @SerializedName("hasMoreProducts")
    var hasMoreProducts: Boolean,

    @SerializedName("totalPages")
    var totalPages: Int,

    @SerializedName("productList")
    var productList: ArrayList<ProductListModel>

    )